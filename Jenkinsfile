pipeline {
    agent any

    environment {
        COMPOSE_FILE = 'docker-compose.prod.yml'
        COMPOSE_PROJECT_NAME = 'caravan'
        POSTGRES_PORT = '5432'
        PGADMIN_PORT = '5050'
        FRONTEND_PORT = '3000'
        BACKEND_PORT = '8080'
        PROD_FRONTEND_PORT = '3080'
        PROD_BACKEND_PORT = '8086'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Create .env') {
            steps {
                script {
                    // Sichere .env Datei Erstellung mit withCredentials
                    withCredentials([
                        string(credentialsId: 'caravan-postgres-db', variable: 'POSTGRES_DB'),
                        string(credentialsId: 'caravan-postgres-user', variable: 'POSTGRES_USER'),
                        string(credentialsId: 'caravan-postgres-password', variable: 'POSTGRES_PASSWORD'),
                        string(credentialsId: 'caravan-pgadmin-email', variable: 'PGADMIN_DEFAULT_EMAIL'),
                        string(credentialsId: 'caravan-pgadmin-password', variable: 'PGADMIN_DEFAULT_PASSWORD'),
                        string(credentialsId: 'caravan-api-url', variable: 'VITE_API_URL')
                    ]) {
                        sh '''
                        cat > .env << EOF
                        # Database Configuration
                        POSTGRES_DB=${POSTGRES_DB}
                        POSTGRES_USER=${POSTGRES_USER}
                        POSTGRES_PASSWORD=${POSTGRES_PASSWORD}
                        POSTGRES_PORT=${POSTGRES_PORT}
                        
                        # PgAdmin Configuration
                        PGADMIN_DEFAULT_EMAIL=${PGADMIN_DEFAULT_EMAIL}
                        PGADMIN_DEFAULT_PASSWORD=${PGADMIN_DEFAULT_PASSWORD}
                        PGADMIN_PORT=${PGADMIN_PORT}
                        
                        # API Configuration
                        VITE_API_URL=${VITE_API_URL}
                        
                        # Port Configuration
                        FRONTEND_PORT=${FRONTEND_PORT}
                        BACKEND_PORT=${BACKEND_PORT}
                        PROD_FRONTEND_PORT=${PROD_FRONTEND_PORT}
                        PROD_BACKEND_PORT=${PROD_BACKEND_PORT}
                        EOF
                        '''
                        
                        // Zeige .env Inhalt (ohne Passwörter)
                        sh '''
                            echo "=== .env created successfully ==="
                            echo "POSTGRES_DB: ${POSTGRES_DB}"
                            echo "POSTGRES_USER: ${POSTGRES_USER}"
                            echo "POSTGRES_PASSWORD: [HIDDEN]"
                            echo "PGADMIN_EMAIL: ${PGADMIN_DEFAULT_EMAIL}"
                            echo "PGADMIN_PASSWORD: [HIDDEN]"
                            echo "API_URL: ${VITE_API_URL}"
                        '''
                    }
                }
            }
        }

        stage('Validate Environment') {
            steps {
                script {
                    // Validiere, dass .env Datei existiert und nicht leer ist
                    sh '''
                        if [ ! -f .env ]; then
                            echo "❌ .env file not found!"
                            exit 1
                        fi
                        
                        if [ ! -s .env ]; then
                            echo "❌ .env file is empty!"
                            exit 1
                        fi
                        
                        echo "✅ .env file is valid"
                    '''
                }
            }
        }

        stage('Build & Deploy') {
            steps {
                script {
                    // Stoppe alte Container
                    sh "docker compose -f ${env.COMPOSE_FILE} down --remove-orphans || true"
                    
                    // Baue Images
                    sh """
                        docker compose -f ${env.COMPOSE_FILE} build --no-cache
                    """
                    
                    // Starte Services
                    sh "docker compose -f ${env.COMPOSE_FILE} up -d"
                    
                    // Warte auf Health Checks
                    sh """
                        echo "Waiting for services to be healthy..."
                        sleep 30
                        docker compose -f ${env.COMPOSE_FILE} ps
                    """
                }
            }
        }

        stage('Health Check') {
            steps {
                script {
                    // Prüfe ob alle Services laufen
                    sh """
                        echo "=== Health Check ==="
                        docker compose -f ${env.COMPOSE_FILE} ps
                        
                        # Prüfe Backend Health
                        echo "Checking backend health..."
                        curl -f http://localhost:${PROD_BACKEND_PORT}/actuator/health || echo "Backend health check failed"
                        
                        # Prüfe Frontend
                        echo "Checking frontend..."
                        curl -f http://localhost:${PROD_FRONTEND_PORT} || echo "Frontend check failed"
                    """
                }
            }
        }
    }

    post {
        always {
            // Cleanup .env file
            sh 'rm -f .env'
        }
        failure {
            echo '❌ Deployment failed - cleaning up...'
            sh "docker compose -f ${env.COMPOSE_FILE} down --remove-orphans"
            sh 'rm -f .env'
        }
        success {
            echo '✅ Deployment successful!'
            sh """
                echo "=== Final Status ==="
                docker compose -f ${env.COMPOSE_FILE} ps
                echo "Frontend: http://localhost:${PROD_FRONTEND_PORT}"
                echo "Backend: http://localhost:${PROD_BACKEND_PORT}"
                echo "PgAdmin: http://localhost:${PGADMIN_PORT}"
            """
        }
    }
}
