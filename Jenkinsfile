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
                        error "Missing credentials: ${missing.join(', ')}. Please configure them in Jenkins."
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

                        echo "🔧 Creating .env file..."
                        sh """
                            echo "POSTGRES_DB=${POSTGRES_DB}" > .env
                            echo "POSTGRES_USER=${POSTGRES_USER}" >> .env
                            echo "POSTGRES_PASSWORD=${POSTGRES_PASSWORD}" >> .env
                            echo "PGADMIN_DEFAULT_EMAIL=${PGADMIN_EMAIL}" >> .env
                            echo "PGADMIN_DEFAULT_PASSWORD=${PGADMIN_PASSWORD}" >> .env
                            echo "VITE_API_URL=${VITE_API_URL}" >> .env
                            echo "POSTGRES_PORT=${env.POSTGRES_PORT}" >> .env
                            echo "PGADMIN_PORT=${env.PGADMIN_PORT}" >> .env
                            echo "FRONTEND_PORT=${env.FRONTEND_PORT}" >> .env
                            echo "BACKEND_PORT=${env.BACKEND_PORT}" >> .env
                            echo "REVERSE_PROXY_PORT=${REVERSE_PROXY_PORT}" >> .env
                        """

                        sh '[ -s .env ] || { echo ".env file is empty or missing."; exit 1; }'
                        echo "✅ .env file created successfully"

                        echo "🛠 Bringing down existing containers..."
                        sh "docker compose -f ${env.COMPOSE_FILE} down --remove-orphans --volumes || true"

                        echo "🚀 Building Docker images..."
                        sh "docker compose -f ${env.COMPOSE_FILE} build --no-cache"

                        echo "🚀 Starting Docker containers..."
                        sh "docker compose -f ${env.COMPOSE_FILE} up -d"

                        echo "⏳ Waiting for services to initialize..."
                        sleep 40

                        echo "🔍 Checking running containers..."
                        sh "docker compose -f ${env.COMPOSE_FILE} ps --format table"

                        echo "🔎 Running health checks..."

                        // Nginx Health
                        sh """
                            for i in {1..5}; do
                                echo "Checking Nginx on http://localhost:${REVERSE_PROXY_PORT} (attempt \$i)..."
                                curl -sS --fail http://localhost:${REVERSE_PROXY_PORT} && exit 0 || true
                                sleep 5
                            done
                            echo "❌ Nginx health check failed after multiple attempts."
                            docker compose -f ${env.COMPOSE_FILE} logs nginx-proxy --tail=20 || true
                            exit 1
                        """

                        // Backend Health
                        sh """
                            for i in {1..5}; do
                                echo "Checking Backend health on /api/actuator/health (attempt \$i)..."
                                curl -sS --fail http://localhost:${REVERSE_PROXY_PORT}/api/actuator/health | grep '\"status\":\"UP\"' && exit 0 || true
                                sleep 5
                            done
                            echo "❌ Backend health check failed after multiple attempts."
                            docker compose -f ${env.COMPOSE_FILE} logs backend --tail=20 || true
                            exit 1
                        """

                        echo "✅ All services are healthy!"
                    }
                }
            }
        }
    }

    post {
        always {
            echo "📌 Pipeline completed. Running final steps..."
        }
        failure {
            echo '❌ Deployment failed!'
            dir(pwd()) {
                sh "docker compose -f ${env.COMPOSE_FILE} ps --format table || true"
                sh "docker compose -f ${env.COMPOSE_FILE} logs --tail=50 || true"
                echo "🧹 Attempting to bring down services after failure..."
                sh "docker compose -f ${env.COMPOSE_FILE} down --remove-orphans --volumes || true"
            }
        }
        success {
            echo '✅ Deployment successful!'
            dir(pwd()) {
                sh "docker compose -f ${env.COMPOSE_FILE} ps --format table"
                sh """
                    echo "--- Access Endpoints ---"
                    echo "Frontend: http://localhost:${env.REVERSE_PROXY_PORT}"
                    echo "API: http://localhost:${env.REVERSE_PROXY_PORT}/api"
                    echo "PgAdmin: http://localhost:${env.REVERSE_PROXY_PORT}/pgadmin"
                    echo "------------------------"
                """
                echo "🧹 Cleaning up .env file after successful deployment..."
                sh 'rm -f .env'
            }
        }
    }
}