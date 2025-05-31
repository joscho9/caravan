pipeline {
    agent any

    environment {
        BACKEND_IMAGE = 'spring-boot-caravan-image'
        FRONTEND_IMAGE = 'react-caravan-frontend'
        COMPOSE_PROJECT_NAME = 'caravan'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Backend') {
            steps {
                dir('backend') {
                    script {
                        docker.build(env.BACKEND_IMAGE)
                    }
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    script {
                        docker.build(env.FRONTEND_IMAGE, '.')
                    }
                }
            }
        }

        stage('Run Services (Prod)') {
            steps {
                script {
                    sh 'docker compose -f docker-compose.prod.yml up -d --build'
                }
            }
        }

        stage('Verify Services') {
            steps {
                script {
                    echo 'Warte auf DB und Backend…'
                    retry(5) {  // bis zu 5 Versuche
                        sleep 10
                        sh 'curl -f http://localhost:8086/actuator/health'
                    }
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
            sh 'docker compose -f docker-compose.prod.yml down'
        }
        success {
            echo 'Deployment erfolgreich – lasse Container am Leben.'
            // Hier keine cleanup-Anweisung – Container bleiben aktiv
        }
    }
}
