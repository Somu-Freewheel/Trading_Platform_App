pipeline {
    agent any
    environment {
        REGISTRY = 'your-docker-registry'
        IMAGE_NAME = 'trading-app'
        IMAGE_TAG = "${BUILD_NUMBER}"
    }
    stages {

        stage('Build') {
            steps {
                sh 'mvn clean package'
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
                    sh 'docker build -t ${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG} .'
                    sh 'docker tag ${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG} ${REGISTRY}/${IMAGE_NAME}:latest'
                }
            }
        }

        stage('Deploy DEV') {
            when {
                    branch 'develop'
            }
            steps {
                sh 'docker compose -f docker-compose-dev.yml up -d'
            }
        }

        stage('Deploy STAGE') {
            when {
                    branch 'staging'
            }
            steps {
                input 'Deploy to STAGE?'
                sh 'docker compose -f docker-compose-stage.yml up -d'
            }
        }

        stage('Deploy PROD') {
            when {
                    branch 'main'
            }
            steps {
                input 'Deploy to PROD?'
                sh 'docker compose -f docker-compose-prod.yml up -d'
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
}