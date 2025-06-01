pipeline {
    agent any

    environment {
        COMPOSE_FILE = 'docker-compose.prod.yml'
        COMPOSE_PROJECT_NAME = 'caravan'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Deploy') {
            steps {
                script {
                    def apiUrl = "http://backend:8080/api"

                    sh """
                        docker compose -f ${env.COMPOSE_FILE} build --build-arg REACT_APP_API_URL=${apiUrl}
                    """

                    sh "docker compose -f ${env.COMPOSE_FILE} up -d"
                }
            }
        }

        stage('Post Actions') {
            steps {
                echo 'Deployment complete!'
            }
        }
    }

    post {
        failure {
            echo 'Fehler beim Deployment – räume auf.'
            sh "docker compose -f ${env.COMPOSE_FILE} down"
        }
        success {
            echo 'Deployment erfolgreich – Container laufen weiter.'
        }
    }
}
