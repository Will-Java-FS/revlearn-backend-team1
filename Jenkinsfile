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

        stage('Push Docker Image to ECR Public') {
            steps {
                script {
                    // Replace the public registry URL and repository details as needed
                    def publicEcrRegistryUrl = 'public.ecr.aws/e2q7w1u7'
                    def repositoryName = 'revlearn/springboot'
                    def imageTag = "${env.BUILD_ID}"

                    withAWS(region: "${AWS_REGION}", credentials: "${AWS_CREDENTIALS_ID}") {
                        sh """
                            # Authenticate Docker to the public ECR registry
                            aws ecr-public get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${publicEcrRegistryUrl}

                            # Build the Docker image
                            docker build -t ${repositoryName}:latest .

                            # Tag the Docker image
                            docker tag ${repositoryName}:latest ${publicEcrRegistryUrl}/${repositoryName}:${imageTag}

                            # Push the Docker image to the public ECR registry
                            docker push ${publicEcrRegistryUrl}/${repositoryName}:${imageTag}
                        """
                    }
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
                        // Add EC2 instance's SSH key to known hosts
                        sh "ssh-keyscan -H ${env.SPRING_BOOT_PUBLIC_DNS} >> ~/.ssh/known_hosts"
                        
                        // Define environment variables securely
                        withEnv([
                            "SPRING_DATASOURCE_URL=${env.SPRING_DATASOURCE_URL}",
                            "SPRING_DATASOURCE_USERNAME=${env.POSTGRES_USER}",
                            "SPRING_DATASOURCE_PASSWORD=${env.POSTGRES_PASSWORD}",
                            "SPRING_API_URL=${env.SPRING_API_URL}",
                            "SECRET_KEY=${env.SECRET_KEY}",
                            "INIT_DATA=${env.INIT_DATA}",
                            "KAFKA_BROKER=${env.KAFKA_BROKER}",
                            "STRIPE_API_KEY=${env.STRIPE_API_KEY}",
                            "CLIENT_URL=${env.CLIENT_URL}"
                        ]) {
                            sh """
                                ssh -i ${env.PEM_FILE_PATH} ec2-user@${env.SPRING_BOOT_PUBLIC_DNS} << 'EOF'
                                $(aws ecr get-login-password --region ${env.AWS_REGION} | docker login --username AWS --password-stdin ${env.AWS_ACCOUNT_ID}.dkr.ecr.${env.AWS_REGION}.amazonaws.com)
                                sudo docker pull ${env.AWS_ACCOUNT_ID}.dkr.ecr.${env.AWS_REGION}.amazonaws.com/${env.ECR_REPO}:${env.BUILD_ID}
                                sudo docker stop ${env.DOCKER_IMAGE} || true
                                sudo docker rm ${env.DOCKER_IMAGE} || true
                                sudo docker run -d --name ${env.DOCKER_IMAGE} \
                                    -p ${env.SPRING_PORT}:8080 \
                                    -e SPRING_DATASOURCE_URL=${env.SPRING_DATASOURCE_URL} \
                                    -e SPRING_DATASOURCE_USERNAME=${env.POSTGRES_USER} \
                                    -e SPRING_DATASOURCE_PASSWORD=${env.POSTGRES_PASSWORD} \
                                    -e SPRING_API_URL=${env.SPRING_API_URL} \
                                    -e SECRET_KEY=${env.SECRET_KEY} \
                                    -e INIT_DATA=${env.INIT_DATA} \
                                    -e KAFKA_BROKER=${env.KAFKA_BROKER} \
                                    -e STRIPE_API_KEY=${env.STRIPE_API_KEY} \
                                    -e CLIENT_URL=${env.CLIENT_URL} \
                                    ${env.AWS_ACCOUNT_ID}.dkr.ecr.${env.AWS_REGION}.amazonaws.com/${env.ECR_REPO}:${env.BUILD_ID}
                                EOF
                            """
                        }
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