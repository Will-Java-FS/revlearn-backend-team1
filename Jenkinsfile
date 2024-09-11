pipeline {
    agent any

    environment {
        AWS_REGION = 'us-east-1'
        GIT_CREDENTIALS = 'github-api-token'
        GIT_URL = 'https://github.com/Will-Java-FS/revlearn-backend-team1'
        JAR_NAME = 'team1-0.0.1-SNAPSHOT.jar'
        AWS_CREDENTIALS_ID = 'aws-credentials-id'
        KAFKA_PORT = '9093'
        SPRING_PORT = '8080'
        INIT_DATA = false
        DOCKER_IMAGE = 'revlearn-springboot'
        ECR_REPO = 'revlearn-springboot'
        SPRINGBOOT_TAG = 'springboot-ec2'
    }

    tools {
        maven 'maven'
        dockerTool 'docker'
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    checkout([$class: 'GitSCM',
                            branches: [[name: 'develop']],
                            userRemoteConfigs: [[url: "${GIT_URL}", credentialsId: "${GIT_CREDENTIALS}"]]])
                }
            }
        }

        stage('Build Maven Package') {
            steps {
                sh "mvn clean package -DskipTests"
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def dockerImage = "${DOCKER_IMAGE}:${env.BUILD_ID}"
                    echo "Building Docker image: ${dockerImage}"
                    sh "docker build -t ${dockerImage} ."
                }
            }
        }

        stage('Fetch Secrets from revlearn/urls') {
            steps {
                script {
                    echo 'Fetching secrets from AWS Secrets Manager (revlearn/urls)...'
                    def secretsJson = sh(script: 'aws secretsmanager get-secret-value --secret-id revlearn/urls --query SecretString --output text', returnStdout: true).trim()

                    def secrets = readJSON(text: secretsJson)

                    env.SPRING_API_URL = secrets.backend_url + '/api/v1'
                    env.CLIENT_URL = secrets.frontend_url
                    env.KAFKA_BROKER = "${secrets.kafka_url}:${env.KAFKA_PORT}"
                    
                    echo "Fetched secrets from revlearn/urls and set environment variables."
                }
            }
        }

        stage('Fetch Secrets from revlearn/spring_env') {
            steps {
                script {
                    echo 'Fetching secrets from AWS Secrets Manager (revlearn/spring_env)...'
                    def springEnvSecretsJson = sh(script: 'aws secretsmanager get-secret-value --secret-id revlearn/spring_env --query SecretString --output text', returnStdout: true).trim()

                    def springEnvSecrets = readJSON(text: springEnvSecretsJson)

                    env.SECRET_KEY = springEnvSecrets.secret_key
                    env.STRIPE_API_KEY = springEnvSecrets.stripe_key
                    
                    echo "Fetched additional secrets from revlearn/spring_env and set environment variables."
                }
            }
        }

        stage('Fetch Database Credentials from revlearn/db_creds') {
            steps {
                script {
                    echo 'Fetching database credentials from AWS Secrets Manager (revlearn/db_creds)...'
                    def dbCredsJson = sh(script: 'aws secretsmanager get-secret-value --secret-id revlearn/db_creds --query SecretString --output text', returnStdout: true).trim()

                    def dbCreds = readJSON(text: dbCredsJson)

                    env.POSTGRES_USER = dbCreds.username
                    env.POSTGRES_PASSWORD = dbCreds.password
                    env.RDS_HOST = dbCreds.host
                    env.RDS_PORT = dbCreds.port
                    env.RDS_DB_NAME = dbCreds.dbname
                    
                    // Update the datasource URL based on fetched credentials
                    env.SPRING_DATASOURCE_URL = "jdbc:postgresql://${env.RDS_HOST}:${env.RDS_PORT}/${env.RDS_DB_NAME}"
                    
                    echo "Fetched database credentials from revlearn/db_creds and set environment variables."
                }
            }
        }


        stage('Fetch Account ID') {
            steps {
                script {
                    echo 'Fetching AWS Account ID...'
                    def accountId = sh(script: 'aws sts get-caller-identity --query Account --output text', returnStdout: true).trim()
                    env.AWS_ACCOUNT_ID = accountId
                }
            }
        }

        stage('Push Docker Image to ECR') {
            steps {
                withAWS(region: "${AWS_REGION}", credentials: "${AWS_CREDENTIALS_ID}") {
                    sh """
                        aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com
                        docker tag ${DOCKER_IMAGE}:${env.BUILD_ID} ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}:${env.BUILD_ID}
                        docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}:${env.BUILD_ID}
                    """
                }
            }
        }

        stage('Find EC2 Instance') {
            steps {
                script {
                    echo 'Finding EC2 instance for deployment...'
                    def springBootInstanceId = sh(script: "aws ec2 describe-instances --filters \"Name=tag:Name,Values=${env.SPRINGBOOT_TAG}\" --query \"Reservations[*].Instances[*].InstanceId\" --output text", returnStdout: true).trim()
                    if (springBootInstanceId) {
                        env.SPRING_BOOT_INSTANCE_ID = springBootInstanceId
                        def springBootPublicDns = sh(script: "aws ec2 describe-instances --instance-ids ${springBootInstanceId} --query \"Reservations[*].Instances[*].PublicDnsName\" --output text", returnStdout: true).trim()
                        env.SPRING_BOOT_PUBLIC_DNS = springBootPublicDns
                        echo "Spring Boot EC2 Public DNS: ${springBootPublicDns}"
                    } else {
                        error 'No Spring Boot EC2 instance found'
                    }
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                script {
                    echo 'Deploying Docker container to EC2 instance...'
                    withCredentials([file(credentialsId: 'springboot-pem', variable: 'PEM_FILE_PATH')]) {
                        def sshCommand = """
                            ssh -i ${PEM_FILE_PATH} ec2-user@${env.SPRING_BOOT_PUBLIC_DNS} << 'EOF'
                            sudo docker pull ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}:${env.BUILD_ID}
                            sudo docker stop ${DOCKER_IMAGE} || true
                            sudo docker rm ${DOCKER_IMAGE} || true
                            sudo docker run -d --name ${DOCKER_IMAGE} \\
                                -p ${SPRING_PORT}:8080 \\
                                -e SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL} \\
                                -e SPRING_DATASOURCE_USERNAME=${POSTGRES_USER} \\
                                -e SPRING_DATASOURCE_PASSWORD=${POSTGRES_PASSWORD} \\
                                -e SPRING_API_URL=${SPRING_API_URL} \\
                                -e SECRET_KEY=${SECRET_KEY} \\
                                -e INIT_DATA=${INIT_DATA} \\
                                -e KAFKA_BROKER=${KAFKA_BROKER} \\
                                -e STRIPE_API_KEY=${STRIPE_API_KEY} \\
                                -e CLIENT_URL=${CLIENT_URL} \\
                                ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}:${env.BUILD_ID}
                            EOF
                        """
                        sh(script: sshCommand)
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Build and deployment succeeded!'
        }
        failure {
            echo 'Build or deployment failed.'
        }
    }
}