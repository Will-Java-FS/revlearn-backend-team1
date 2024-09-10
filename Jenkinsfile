pipeline {
    agent any

    environment {
        AWS_REGION = 'us-east-1'  // Adjust your region
        GIT_CREDENTIALS = 'github-api-token' // Replace with the ID of your GitLab credentials in Jenkins
        GIT_URL = 'https://github.com/Will-Java-FS/revlearn-backend-team1'
        S3_BUCKET_PREFIX = 'elasticbeanstalk'  // Replace with your S3 bucket name
        JAR_NAME = 'team1-0.0.1-SNAPSHOT.jar'  // This should align with the profile name
        AWS_CREDENTIALS_ID = 'aws-credentials-id'  // The AWS credentials ID in Jenkins
        BEANSTALK_ENV_NAME = 'revlearn-springboot-env'
        BEANSTALK_APP_NAME = 'revlearn-springboot-app'
        DB_PORT = '5432'
        DB_NAME = 'revlearn'
        KAFKA_PORT = '9093'
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
                    // Checkout main repository
                    checkout([$class: 'GitSCM',
                            branches: [[name: 'develop']],
                            userRemoteConfigs: [[url: "${GIT_URL}", credentialsId: "${GIT_CREDENTIALS}"]]])
                }
            }
        }

        stage('Fetch Account ID') {
            steps {
                script {
                    echo 'Fetching AWS Account ID...'
                    def accountId = sh(script: 'aws sts get-caller-identity --query Account --output text', returnStdout: true).trim()
                    env.ACCOUNT_ID = accountId
                    env.S3_BUCKET_NAME = "${S3_BUCKET_PREFIX}-${AWS_REGION}-${ACCOUNT_ID}"
                    echo "S3 Bucket Name: ${env.S3_BUCKET_NAME}"
                }
            }
        }

        stage('Fetch Secrets') {
            steps {
                echo 'Fetching secrets from AWS Secrets Manager...'
                withAWS(credentials: "${AWS_CREDENTIALS_ID}", region: "${AWS_REGION}") {
                    script {
                        // Fetch Kafka URL
                        def kafkaUrl = sh(script: 'aws secretsmanager get-secret-value --secret-id revlearn/urls --query SecretString --output text | jq -r .kafka_url', returnStdout: true).trim()
                        env.KAFKA_URL = kafkaUrl + ':' + KAFKA_PORT

                        def dbUrl = sh(script: 'aws secretsmanager get-secret-value --secret-id revlearn/urls --query SecretString --output text | jq -r .rds_url', returnStdout: true).trim()
                        env.DB_URL = dbUrl

                        // Construct JDBC URL
                        def jdbcUrl = "jdbc:postgresql://${env.DB_URL}:${env.DB_PORT}/${env.DB_NAME}"
                        env.JDBC_URL = jdbcUrl
                        echo "JDBC URL: ${env.JDBC_URL}"

                        // Fetch DB credentials
                        def dbCredentials = sh(script: 'aws secretsmanager get-secret-value --secret-id revlearn/db_creds --query SecretString --output text | jq -r .username,.password', returnStdout: true).trim().split('\n')
                        env.DB_USERNAME = dbCredentials[0]
                        env.DB_PASSWORD = dbCredentials[1]
                    }
                }
            }
        }

        stage('Build') {
            steps {
                echo 'Building the project with production profile...'
                sh 'mvn validate -Pprod'
                sh 'mvn clean package -Pprod -DskipTests'  // Using the 'prod' profile
            }
        }

        stage('Upload to S3') {
            steps {
                echo 'Uploading JAR to S3...'
                withAWS(credentials: "${AWS_CREDENTIALS_ID}", region: "${AWS_REGION}") {
                    sh 'aws s3 cp target/${JAR_NAME} s3://${S3_BUCKET_NAME}/ --region ${AWS_REGION}'
                }
            }
        }

        stage('Update Elastic Beanstalk Environment Variables') {
            steps {
                echo 'Updating environment variables in Elastic Beanstalk...'
                withAWS(credentials: "${AWS_CREDENTIALS_ID}", region: "${AWS_REGION}") {
                    sh '''
                    aws elasticbeanstalk update-environment --environment-name "${BEANSTALK_ENV_NAME}" \
                    --application-name "${BEANSTALK_APP_NAME}" \
                    --option-settings Namespace=aws:elasticbeanstalk:application:environment,OptionName=SPRING_DATASOURCE_URL,Value=${JDBC_URL} \
                    Namespace=aws:elasticbeanstalk:application:environment,OptionName=SPRING_DATASOURCE_USERNAME,Value=${DB_USERNAME} \
                    Namespace=aws:elasticbeanstalk:application:environment,OptionName=SPRING_DATASOURCE_PASSWORD,Value=${DB_PASSWORD} \
                    Namespace=aws:elasticbeanstalk:application:environment,OptionName=KAFKA_URL,Value=${KAFKA_URL}
                    '''
                }
            }
        }

        stage('Deploy to Elastic Beanstalk') {
            steps {
                script {
                    echo 'Creating application version in Elastic Beanstalk...'
                    withAWS(credentials: "${AWS_CREDENTIALS_ID}", region: "${AWS_REGION}") {
                        sh '''
                        aws elasticbeanstalk create-application-version --application-name "${BEANSTALK_APP_NAME}" \
                        --version-label "v${BUILD_NUMBER}" \
                        --source-bundle S3Bucket=${S3_BUCKET_NAME},S3Key=${JAR_NAME}
                        '''
                    }
                }
            }
        }

        stage('Wait for Application Version') {
            steps {
                script {
                    def versionLabel = "v${BUILD_NUMBER}"
                    def maxRetries = 10
                    def retryInterval = 30 // seconds
                    
                    for (int i = 0; i < maxRetries; i++) {
                        def status = sh(script: "aws elasticbeanstalk describe-application-versions --application-name ${BEANSTALK_APP_NAME} --version-label ${versionLabel} --query 'ApplicationVersions[0].Status' --output text", returnStdout: true).trim()
                        
                        echo "Current status of application version ${versionLabel}: ${status}"
                        
                        if (status == "Processed") {
                            echo "Application version ${versionLabel} is processed."
                            break
                        } else {
                            echo "Waiting for application version ${versionLabel} to be processed..."
                            sleep retryInterval
                        }
                    }
                    
                    if (status != "Processed") {
                        error "Application version ${versionLabel} did not become processed after ${maxRetries} retries."
                    }
                }
            }
        }

        stage('Update Elastic Beanstalk Environment') {
            steps {
                echo 'Updating Elastic Beanstalk environment...'
                withAWS(credentials: "${AWS_CREDENTIALS_ID}", region: "${AWS_REGION}") {
                    sh '''
                    aws elasticbeanstalk update-environment --application-name "${BEANSTALK_APP_NAME}" \
                    --environment-name "${BEANSTALK_ENV_NAME}" \
                    --version-label "v${BUILD_NUMBER}"
                    '''
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
