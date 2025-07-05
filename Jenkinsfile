pipeline {
    agent any

    environment {
        COMPOSE_FILE = 'docker-compose.prod.yml'
        COMPOSE_PROJECT_NAME = 'caravan'

        POSTGRES_PORT = '5432'
        PGADMIN_PORT = '5050'
        FRONTEND_PORT = '3000'
        BACKEND_PORT = '8080'
        REVERSE_PROXY_PORT = '3080'
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
                        'caravan-api-url',
                        'caravan-reverse-proxy-port'
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
                        string(credentialsId: 'caravan-api-url', variable: 'VITE_API_URL'),
                        string(credentialsId: 'caravan-reverse-proxy-port', variable: 'REVERSE_PROXY_PORT')
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
                                REVERSE_PROXY_PORT=${REVERSE_PROXY_PORT}
                            """.stripIndent()

                            writeFile file: '.env', text: envVars
                            echo "Successfully created .env file in ${pwd()}/.env"

                            sh '[ -s .env ] || { echo "❌ .env file is empty or missing."; exit 1; }'

                            echo "Attempting to bring down existing containers and remove orphans AND VOLUMES..."
                            sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} down --remove-orphans --volumes || true"

                            echo "Building Docker images..."
                            sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} build --no-cache"

                            echo "Starting Docker containers..."
                            sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} up -d"

                            echo "Waiting for services to come online..."
                            // Increased sleep duration for backend startup
                            sleep 40 // Adjusted from 20 to 40 seconds, might need more

                            echo "Verifying running containers..."
                            sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} ps"

                            echo "🏥 Running Health Checks..."
                            def nginxHealth = false
                            def backendHealth = false

                            // Check Nginx Proxy (Frontend + API)
                            for (int i = 0; i < 6; i++) {
                                try {
                                    sh "curl -sS --fail http://localhost:${env.REVERSE_PROXY_PORT} || true"
                                    echo "✅ Nginx Proxy (Frontend) is healthy."
                                    nginxHealth = true
                                    break
                                } catch (Exception e) {
                                    echo "Nginx Proxy not healthy yet, retrying in 10 seconds... (${i+1}/6)"
                                    sleep 10
                                }
                            }
                            if (!nginxHealth) {
                                sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} logs nginx-proxy || true"
                                error "❌ Nginx Proxy health check failed after multiple attempts."
                            }

                            // Check Backend through Nginx Proxy
                            for (int i = 0; i < 6; i++) {
                                try {
                                    sh "curl -sS --fail http://localhost:${env.REVERSE_PROXY_PORT}/api/actuator/health 2>&1 | grep '\"status\":\"UP\"'"
                                    echo "✅ Backend (via Nginx Proxy) is healthy."
                                    backendHealth = true
                                    break
                                } catch (Exception e) {
                                    echo "Backend not healthy yet, retrying in 10 seconds... (${i+1}/6)"
                                    sleep 10
                                }
                            }
                            if (!backendHealth) {
                                sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} logs backend || true"
                                error "❌ Backend health check failed after multiple attempts."
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
            // Keep this block minimal or for very generic info.
            // DO NOT put .env cleanup here, as 'always' runs BEFORE success/failure.
            echo "Pipeline finished. Performing post-build actions."
        }
        failure {
            echo '❌ Deployment failed!'
            echo 'Gathering diagnostic information...'
            dir(pwd()) {
                // The .env file will still be present here.
                sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} ps || true"
                sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} logs || true" // Get ALL logs for debugging
                echo "Attempting to bring down services after failure..."
                sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} down --remove-orphans --volumes || true"
                // IMPORTANT: DO NOT remove .env here on failure. It helps with debugging the workspace.
            }
        }
        success {
            echo '✅ Deployment successful!'
            dir(pwd()) {
                sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} ps"
                sh """
                    echo "--- Access Endpoints ---"
                    echo "Frontend: http://localhost:${env.REVERSE_PROXY_PORT}"
                    echo "API: http://localhost:${env.REVERSE_PROXY_PORT}/api"
                    echo "PgAdmin: http://localhost:${env.REVERSE_PROXY_PORT}/pgadmin"
                    echo "------------------------"
                """
                echo "Cleaning up .env file after successful deployment..."
                sh 'rm -f .env' // ONLY remove .env on successful runs
            }
        }
        // No 'finally' block here, as it's not a valid declarative post condition.
        // Cleanup on failure is handled by leaving .env in the workspace for diagnostics.
    }
}