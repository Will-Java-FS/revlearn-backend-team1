pipeline {
    agent any

    environment {
        AWS_REGION = 'us-east-1'
        GIT_CREDENTIALS = 'github-api-token'
        GIT_URL = 'https://github.com/Will-Java-FS/revlearn-backend-team1'
        S3_BUCKET_PREFIX = 'elasticbeanstalk'
        JAR_NAME = 'team1-0.0.1-SNAPSHOT.jar'
        AWS_CREDENTIALS_ID = 'aws-credentials-id'
        BEANSTALK_ENV_NAME = 'revlearn-springboot-env'
        BEANSTALK_APP_NAME = 'revlearn-springboot-app'
        DB_PORT = '5432'
        DB_NAME = 'revlearn'
        KAFKA_PORT = '9093'
        SPRING_PORT = '8080'
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
                        env.KAFKA_URL = sh(script: '''
                            aws secretsmanager get-secret-value --secret-id revlearn/urls \
                            --query SecretString --output text | jq -r .kafka_url
                        ''', returnStdout: true).trim() + ":${KAFKA_PORT}"

                        // Fetch Database URL and Credentials
                        env.DB_URL = sh(script: '''
                            aws secretsmanager get-secret-value --secret-id revlearn/urls \
                            --query SecretString --output text | jq -r .rds_url
                        ''', returnStdout: true).trim()

                        env.JDBC_URL = "jdbc:postgresql://${DB_URL}:${DB_PORT}/${DB_NAME}"
                        echo "JDBC URL: ${env.JDBC_URL}"

                        def dbCredentials = sh(script: '''
                            aws secretsmanager get-secret-value --secret-id revlearn/db_creds \
                            --query SecretString --output text | jq -r '.username,.password'
                        ''', returnStdout: true).trim().split('\n')

                        env.DB_USERNAME = dbCredentials[0]
                        env.DB_PASSWORD = dbCredentials[1]

                        // Fetch application secrets (SECRET_KEY and STRIPE_API_KEY)
                        env.SECRET_KEY = sh(script: '''
                            aws secretsmanager get-secret-value --secret-id revlearn/spring_env \
                            --query SecretString --output text | jq -r .secret_key
                        ''', returnStdout: true).trim()

                        env.STRIPE_API_KEY = sh(script: '''
                            aws secretsmanager get-secret-value --secret-id revlearn/spring_env \
                            --query SecretString --output text | jq -r .stripe_key
                        ''', returnStdout: true).trim()

                        // Fetch Frontend URL
                        env.FRONTEND_URL = sh(script: '''
                            aws secretsmanager get-secret-value --secret-id revlearn/urls \
                            --query SecretString --output text | jq -r .frontend_url
                        ''', returnStdout: true).trim()

                        echo "Frontend URL: ${env.FRONTEND_URL}"

                        // Fetch Backend URL and append /api/v1
                        env.BACKEND_URL = sh(script: '''
                            aws secretsmanager get-secret-value --secret-id revlearn/urls \
                            --query SecretString --output text | jq -r .backend_url
                        ''', returnStdout: true).trim() + "/api/v1"

                        echo "Backend URL: ${env.BACKEND_URL}"
                    }
                }
            }
        }

        stage('Build') {
            steps {
                echo 'Building the project with production profile...'
                sh 'mvn clean package -Pprod -DskipTests'
            }
        }

        stage('Upload to S3') {
            steps {
                echo 'Uploading JAR to S3...'
                script {
                    def jarFile = "target/${JAR_NAME}"
                    if (fileExists(jarFile)) {
                        echo "JAR file exists: ${jarFile}, proceeding with upload."
                        withAWS(credentials: "${AWS_CREDENTIALS_ID}", region: "${AWS_REGION}") {
                            sh "aws s3 cp ${jarFile} s3://${S3_BUCKET_NAME}/${JAR_NAME} --region ${AWS_REGION}"
                        }
                    } else {
                        error "JAR file does not exist: ${jarFile}, aborting upload."
                    }
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
                    --option-settings file://<(echo '[{"Namespace": "aws:elasticbeanstalk:application:environment", "OptionName": "SPRING_DATASOURCE_URL", "Value": "'${JDBC_URL}'"},
                                                 {"Namespace": "aws:elasticbeanstalk:application:environment", "OptionName": "SPRING_DATASOURCE_USERNAME", "Value": "'${DB_USERNAME}'"},
                                                 {"Namespace": "aws:elasticbeanstalk:application:environment", "OptionName": "SPRING_DATASOURCE_PASSWORD", "Value": "'${DB_PASSWORD}'"},
                                                 {"Namespace": "aws:elasticbeanstalk:application:environment", "OptionName": "KAFKA_URL", "Value": "'${KAFKA_URL}'"},
                                                 {"Namespace": "aws:elasticbeanstalk:application:environment", "OptionName": "SECRET_KEY", "Value": "'${SECRET_KEY}'"},
                                                 {"Namespace": "aws:elasticbeanstalk:application:environment", "OptionName": "STRIPE_API_KEY", "Value": "'${STRIPE_API_KEY}'"},
                                                 {"Namespace": "aws:elasticbeanstalk:application:environment", "OptionName": "CLIENT_URL", "Value": "'${FRONTEND_URL}'"},
                                                 {"Namespace": "aws:elasticbeanstalk:application:environment", "OptionName": "SPRING_API_URL", "Value": "'${BACKEND_URL}'"},
                                                 {"Namespace": "aws:elasticbeanstalk:application:environment", "OptionName": "PORT", "Value": "'${SPRING_PORT}'"}]')
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
