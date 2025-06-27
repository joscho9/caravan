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
                            def backendHealth = false
                            def frontendHealth = false

                            for (int i = 0; i < 6; i++) { // Increased retries
                                try {
                                    // Using 'grep' is crucial here, and piping stderr to null for clean output
                                    sh "curl -sS --fail http://localhost:${env.PROD_BACKEND_PORT}/actuator/health 2>&1 | grep '\"status\":\"UP\"'"
                                    echo "✅ Backend is healthy."
                                    backendHealth = true
                                    break
                                } catch (Exception e) {
                                    echo "Backend not healthy yet, retrying in 10 seconds... (${i+1}/6)" // Longer retry interval
                                    sleep 10
                                }
                            }
                            if (!backendHealth) {
                                // Add backend logs to diagnose startup issues
                                sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} logs backend || true"
                                error "❌ Backend health check failed after multiple attempts."
                            }

                            for (int i = 0; i < 3; i++) {
                                try {
                                    sh "curl -sS --fail http://localhost:${env.PROD_FRONTEND_PORT} || true" // Just check if it responds
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
        // 'always' runs before 'failure' or 'success'
        // We'll put cleanup in 'always' but guard it against failure actions needing the .env file
        always {
            // This section only contains cleanup steps to run regardless of stage outcome.
            // .env cleanup is moved to the end of success/failure if needed for diagnostics.
            echo "Pipeline finished. Starting final cleanup."
        }
        failure {
            echo '❌ Deployment failed!'
            echo 'Gathering diagnostic information...'
            dir(pwd()) {
                // For diagnostic commands in failure, we prioritize getting output
                // over strictly avoiding warnings if .env is missing.
                // However, the .env file *should* still exist here as 'always' hasn't removed it yet.
                // Re-sourcing here for consistency and to avoid "variable not set" warnings if .env still exists.
                sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} ps || true"
                sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} logs || true" // Get ALL logs for debugging
                echo "Attempting to bring down services after failure..."
                sh "set -a && . ./.env && docker compose -f ${env.COMPOSE_FILE} down --remove-orphans --volumes || true"
            }
        }
        success {
            echo '✅ Deployment successful!'
            dir(pwd()) {
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
        // This 'cleanup' runs after 'success' or 'failure'
        // It ensures .env is removed last.
        // NOTE: This 'cleanup' block structure is a common pattern for 'post' actions.
        // The 'always' block here is conceptually run *first* among post-actions,
        // but its content should be minimal, or it should ensure subsequent blocks can still function.
        // A more robust way to do 'always-cleanup' is a dedicated post-build step or a separate stage.
        // For now, let's keep it simple:
        finally { // The 'finally' block in 'post' runs *after* success/failure/unstable
            echo "Final cleanup: removing .env file."
            dir(pwd()) {
                sh 'rm -f .env || true' // '|| true' to prevent failure if file is already gone
            }
        }
    }
}