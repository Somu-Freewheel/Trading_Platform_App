pipeline {
    agent any
    tools {
            maven 'Maven3'
    }
    environment {
        REGISTRY = 'your-docker-registry'
        IMAGE_NAME = 'trading-app'
        IMAGE_TAG = "${BUILD_NUMBER}"
    }
    stages {

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Docker Build') {
            steps {
                script{
                    sh "docker build -t ${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG} ."
                    sh "docker tag ${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG} ${REGISTRY}/${IMAGE_NAME}:latest"
                }
            }
        }

        stage('Deploy DEV') {
            steps {
                sh 'docker rm -f trading-api || true'
                sh 'docker-compose -f docker-compose.yml up -d --force-recreate --no-deps trading-app'
            }
        }

        stage('Deploy STAGE') {
            steps {
                input 'Deploy to STAGE?'
                sh 'docker-compose -f docker-compose-stage.yml up -d'
            }
        }

        stage('Deploy PROD') {
            steps {
                input 'Deploy to PROD?'
                sh 'docker-compose -f docker-compose-prod.yml up -d'
            }
        }
    }
    post {
        always {
            cleanWs()
        }
        failure {
            echo 'Pipeline failed - Notify team'
        }
        success {
            echo 'Pipeline succeeded!'
        }
    }
}