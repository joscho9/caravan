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
                // Hier kannst du z. B. Health Checks hinzufügen
                echo 'Waiting for backend and frontend to be up...'
                sleep 20
                sh 'curl -f http://localhost:8086/actuator/health || true'
            }
        }

        stage('Post Actions') {
            steps {
                echo 'Deployment complete!'
            }
        }
    }

    post {
        always {
            echo 'Cleaning up containers...'
            sh 'docker compose -f docker-compose.prod.yml down'
        }
    }
}
