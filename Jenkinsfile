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
            steps { checkout scm }
        }

        stage('Validate Credentials') {
            steps {
                script {
                    def required = [
                        'caravan-postgres-db',
                        'caravan-postgres-user',
                        'caravan-postgres-password',
                        'caravan-pgadmin-email',
                        'caravan-pgadmin-password',
                        'caravan-api-url'
                    ]
                    def missing = required.findAll {
                        try { credentials(it); false }
                        catch (ignored) { true }
                    }
                    if (missing) error "🚨 Missing credentials: ${missing.join(', ')}"
                    echo "✅ All credentials present"
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
"""

                        writeFile file: '.env', text: envVars
                        sh '[ -s .env ] || { echo "❌ .env fehlt"; exit 1; }'

                        sh "docker compose -f ${env.COMPOSE_FILE} down --remove-orphans || true"
                        sh "docker compose -f ${env.COMPOSE_FILE} build --no-cache"
                        sh "set -a && . .env && docker compose -f ${env.COMPOSE_FILE} up -d"

                        sleep 10
                        sh "docker compose -f ${env.COMPOSE_FILE} ps"

                        echo "🏥 Health Checks"
                        sh "curl -f http://localhost:${env.PROD_BACKEND_PORT}/actuator/health || echo '❌ Backend'"
                        sh "curl -f http://localhost:${env.PROD_FRONTEND_PORT} || echo '❌ Frontend'"
                    }
                }
            }
        }
    }

    post {
        always { sh 'rm -f .env' }
        failure {
            echo '❌ Fehler beim Deployment'
            sh "docker compose -f ${env.COMPOSE_FILE} ps || true"
            sh "docker compose -f ${env.COMPOSE_FILE} logs caravan-postgres || true"
            sh "docker compose -f ${env.COMPOSE_FILE} down --remove-orphans"
        }
        success {
            echo '✅ Deployment erfolgreich!'
            sh """
                docker compose -f ${env.COMPOSE_FILE} ps
                echo "Frontend: http://localhost:${env.PROD_FRONTEND_PORT}"
                echo "Backend: http://localhost:${env.PROD_BACKEND_PORT}"
                echo "PgAdmin: http://localhost:${env.PGADMIN_PORT}"
            """
        }
    }
}