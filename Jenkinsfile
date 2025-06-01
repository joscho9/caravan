pipeline {
    agent any

    environment {
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
                    // Setze API-URL für das Frontend, wie sie im Compose verwendet wird
                    def apiUrl = "http://backend:8080/api"

                    // Build-Argumente setzen
                    sh """
                        docker compose -f docker-compose.prod.yml build \
                          --build-arg REACT_APP_API_URL=${apiUrl}
                    """

                    // Container hochfahren (bzw. neu starten)
                    sh 'docker compose -f docker-compose.prod.yml up -d'
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
            sh 'docker compose -f docker-compose.yml down'
        }
        success {
            echo 'Deployment erfolgreich – Container laufen weiter.'
        }
    }
}
