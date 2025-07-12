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
                        'caravan-reverse-proxy-port',
                        'caravan-mail-username',
                        'caravan-mail-password',
                        'caravan-admin-email'
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
                    echo "All required credentials are present."
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
                        string(credentialsId: 'caravan-reverse-proxy-port', variable: 'REVERSE_PROXY_PORT'),
                        string(credentialsId: 'caravan-mail-username', variable: 'MAIL_USERNAME'),
                        string(credentialsId: 'caravan-mail-password', variable: 'MAIL_PASSWORD'),
                        string(credentialsId: 'caravan-admin-email', variable: 'ADMIN_EMAIL')
                    ]) {
                        dir(pwd()) {
                            sh '''
                                cat > .env <<EOF
POSTGRES_DB=$POSTGRES_DB
POSTGRES_USER=$POSTGRES_USER
POSTGRES_PASSWORD=$POSTGRES_PASSWORD
PGADMIN_DEFAULT_EMAIL=$PGADMIN_EMAIL
PGADMIN_DEFAULT_PASSWORD=$PGADMIN_PASSWORD
VITE_API_URL=$VITE_API_URL
POSTGRES_PORT=$POSTGRES_PORT
PGADMIN_PORT=$PGADMIN_PORT
FRONTEND_PORT=$FRONTEND_PORT
BACKEND_PORT=$BACKEND_PORT
REVERSE_PROXY_PORT=$REVERSE_PROXY_PORT
MAIL_USERNAME=$MAIL_USERNAME
MAIL_PASSWORD=$MAIL_PASSWORD
ADMIN_EMAIL=$ADMIN_EMAIL
EOF
                            '''
                            echo ".env file created successfully"

                            sh '[ -s .env ] || { echo ".env file is empty or missing."; exit 1; }'

                            echo "Stopping existing containers..."
                            sh 'set -a && . ./.env && docker compose -f $COMPOSE_FILE down --remove-orphans || true'

                            echo "Building Docker images..."
                            sh 'set -a && . ./.env && docker compose -f $COMPOSE_FILE build --no-cache'

                            echo "Starting Docker containers..."
                            sh 'set -a && . ./.env && docker compose -f $COMPOSE_FILE up -d'

                            echo "Waiting for services to come online..."
                            sleep 15

                            echo "Verifying running containers..."
                            sh 'set -a && . ./.env && docker compose -f $COMPOSE_FILE ps --format table'

                            echo "Running health checks..."
                            def nginxHealthy = false
                            def backendHealthy = false

                            // NGINX health check
                            for (int i = 0; i < 3; i++) {
                                def rc = sh(script: 'curl -sS --fail http://localhost:$REVERSE_PROXY_PORT || true', returnStatus: true)
                                if (rc == 0) {
                                    echo "Nginx Proxy is healthy."
                                    nginxHealthy = true
                                    break
                                }
                                echo "Nginx not healthy yet, retrying in 5 seconds... (${i+1}/3)"
                                sleep 5
                            }
                            if (!nginxHealthy) {
                                sh 'set -a && . ./.env && docker compose -f $COMPOSE_FILE logs nginx-proxy --tail=20 || true'
                                error "Nginx Proxy health check failed."
                            }

                            // Backend health check
                            for (int i = 0; i < 3; i++) {
                                def rc = sh(script: 'curl -sS --fail http://localhost:$REVERSE_PROXY_PORT/api/actuator/health | grep \'"status":"UP"\' || true', returnStatus: true)
                                if (rc == 0) {
                                    echo "Backend is healthy."
                                    backendHealthy = true
                                    break
                                }
                                echo "Backend not healthy yet, retrying in 5 seconds... (${i+1}/3)"
                                sleep 5
                            }
                            if (!backendHealthy) {
                                sh 'set -a && . ./.env && docker compose -f $COMPOSE_FILE logs backend --tail=20 || true'
                                error "Backend health check failed."
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
            echo "Pipeline finished. Post build actions."
        }
        failure {
            echo '❌ Deployment failed!'
            dir(pwd()) {
                sh 'set -a && . ./.env && docker compose -f $COMPOSE_FILE ps --format table || true'
                sh 'set -a && . ./.env && docker compose -f $COMPOSE_FILE logs --tail=50 || true'
                echo "Attempting cleanup after failure..."
                sh 'set -a && . ./.env && docker compose -f $COMPOSE_FILE down --remove-orphans || true'
            }
        }
        success {
            echo '✅ Deployment successful!'
            dir(pwd()) {
                sh 'set -a && . ./.env && docker compose -f $COMPOSE_FILE ps --format table'
                sh '''
                    echo "--- Access Endpoints ---"
                    echo "Frontend: http://localhost:$REVERSE_PROXY_PORT"
                    echo "API:      http://localhost:$REVERSE_PROXY_PORT/api"
                    echo "PgAdmin:  http://localhost:$REVERSE_PROXY_PORT/pgadmin"
                    echo "-------------------------"
                '''
                echo "Cleaning up .env file after successful deployment..."
                sh 'rm -f .env'
            }
        }
    }
}