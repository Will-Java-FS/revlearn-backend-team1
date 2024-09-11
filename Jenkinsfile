pipeline {
    agent any

    environment {
        AWS_REGION = 'us-east-1'
        GIT_CREDENTIALS = 'github-api-token'
        GIT_URL = 'https://github.com/Will-Java-FS/revlearn-backend-team1'
        JAR_NAME = 'team1-0.0.1-SNAPSHOT.jar'
        AWS_CREDENTIALS_ID = 'aws-credentials-id'
        DB_PORT = '5432'
        DB_NAME = 'revlearn'
        KAFKA_PORT = '9093'
        SPRING_PORT = '8080'
        INIT_DATA = false
        DOCKER_IMAGE = 'revlearn-springboot'
        ECR_REPO = 'revlearn-springboot'
        SPRINGBOOT_TAG = 'springboot-ec2'
    }

    tools {
        maven 'maven'
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    // Checkout from GitHub
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
                    docker.build("${DOCKER_IMAGE}:${env.BUILD_ID}")
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
