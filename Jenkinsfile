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

        stage('Validate Credentials') {
            steps {
                script {
                    def requiredCredentials = [
                        'caravan-postgres-db',
                        'caravan-postgres-user',
                        'caravan-postgres-password',
                        'caravan-pgadmin-email',
                        'caravan-pgadmin-password',
                        'caravan-api-url'
                    ]

                    def missing = []
                    for (def credId : requiredCredentials) {
                        try {
                            credentials(credId)
                        } catch (Exception e) {
                            missing.add(credId)
                        }
                    }
                    if (!missing.isEmpty()) {
                        error "🚨 Missing credentials: ${missing.join(', ')}. Please configure them in Jenkins."
                    }
                    echo "✅ All required credentials are present."
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    withCredentials([
                        string(credentialsId: 'caravan-postgres-db', variable: 'POSTGRES_DB'),
                        string(credentialsId: 'caravan-postgres-user', variable: 'POSTGRES_USER'),
                        string(credentialsId: 'caravan-postgres-password', variable: 'POSTGRES_PASSWORD'),
                        string(credentialsId: 'caravan-pgadmin-email', variable: 'PGADMIN_EMAIL'),
                        string(credentialsId: 'caravan-pgadmin-password', variable: 'PGADMIN_PASSWORD'),
                        string(credentialsId: 'caravan-api-url', variable: 'VITE_API_URL')
                    ]) {
                        dir(pwd()) {
                            def envVars = """
                                POSTGRES_DB=${POSTGRES_DB}
                                POSTGRES_USER=${POSTGRES_USER}
                                POSTGRES_PASSWORD=${POSTGRES_PASSWORD}
                                PGADMIN_DEFAULT_EMAIL=${PGADMIN_EMAIL}
                                PGADMIN_DEFAULT_PASSWORD=${PGADMIN_PASSWORD}
                                VITE_API_URL=${VITE_API_URL}
                                POSTGRES_PORT=${env.POSTGRES_PORT}
                                PGADMIN_PORT=${env.PGADMIN_PORT}
                                FRONTEND_PORT=${env.FRONTEND_PORT}
                                BACKEND_PORT=${env.BACKEND_PORT}
                                PROD_FRONTEND_PORT=${env.PROD_FRONTEND_PORT}
                                PROD_BACKEND_PORT=${env.PROD_BACKEND_PORT}
                            """.stripIndent()

                            writeFile file: '.env', text: envVars
                            echo "Successfully created .env file in ${pwd()}/.env"

                            sh '[ -s .env ] || { echo "❌ .env file is empty or missing."; exit 1; }'

                            echo "Attempting to bring down existing containers and remove orphans AND VOLUMES..."
                            // Source .env for down as well to avoid warnings if it needs to parse it for volume names etc.
                            sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} down --remove-orphans --volumes || true"

                            echo "Building Docker images..."
                            // Build doesn't strictly need .env for image building, but harmless to include
                            sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} build --no-cache"

                            echo "Starting Docker containers..."
                            // CRITICAL: Ensure .env is sourced for 'up' to provide secrets
                            sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} up -d"

                            echo "Waiting for services to come online..."
                            sleep 20

                            echo "Verifying running containers..."
                            // Source .env for 'ps' to avoid warnings
                            sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} ps"

                            echo "🏥 Running Health Checks..."
                            def backendHealth = false
                            def frontendHealth = false

                            for (int i = 0; i < 3; i++) {
                                try {
                                    // Health checks might not need .env, but keeping the pattern
                                    sh "curl -f http://localhost:${env.PROD_BACKEND_PORT}/actuator/health | grep '\"status\":\"UP\"'" // More robust check
                                    echo "✅ Backend is healthy."
                                    backendHealth = true
                                    break
                                } catch (Exception e) {
                                    echo "Backend not healthy yet, retrying in 5 seconds... (${i+1}/3)"
                                    sleep 5
                                }
                            }
                            if (!backendHealth) {
                                error "❌ Backend health check failed after multiple attempts."
                            }

                            for (int i = 0; i < 3; i++) {
                                try {
                                    sh "curl -f http://localhost:${env.PROD_FRONTEND_PORT}" // Basic check for frontend
                                    echo "✅ Frontend is healthy."
                                    frontendHealth = true
                                    break
                                } catch (Exception e) {
                                    echo "Frontend not healthy yet, retrying in 5 seconds... (${i+1}/3)"
                                    sleep 5
                            }
                        }
                            if (!frontendHealth) {
                                error "❌ Frontend health check failed after multiple attempts."
                            }
                            echo "All services are healthy."
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            echo "Cleaning up .env file..."
            dir(pwd()) {
                sh 'rm -f .env'
            }
        }
        failure {
            echo '❌ Deployment failed!'
            echo 'Gathering diagnostic information...'
            dir(pwd()) {
                // Source .env for ps and logs in failure block to avoid warnings
                sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} ps || true"
                sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} logs || true"
                echo "Attempting to bring down services after failure..."
                sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} down --remove-orphans --volumes || true"
            }
        }
        success {
            echo '✅ Deployment successful!'
            dir(pwd()) {
                // Source .env for ps in success block to avoid warnings
                sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} ps"
                sh """
                    echo "--- Access Endpoints ---"
                    echo "Frontend: http://localhost:${env.PROD_FRONTEND_PORT}"
                    echo "Backend: http://localhost:${env.PROD_BACKEND_PORT}"
                    echo "PgAdmin: http://localhost:${env.PGADMIN_PORT}"
                    echo "------------------------"
                """
            }
        }
    }
}