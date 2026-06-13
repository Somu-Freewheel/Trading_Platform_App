pipeline {
    agent any

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
                sh 'docker build -t trading-app:${BUILD_NUMBER} .'
            }
        }

        stage('Deploy DEV') {
            steps {
                sh 'docker compose -f docker-compose-dev.yml up -d'
            }
        }

        stage('Deploy STAGE') {
            steps {
                input 'Deploy to STAGE?'

                sh 'docker compose -f docker-compose-stage.yml up -d'
            }
        }

        stage('Deploy PROD') {
            steps {
                input 'Deploy to PROD?'

                sh 'docker compose -f docker-compose-prod.yml up -d'
            }
        }
    }
}