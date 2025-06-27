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

        stage('Check Credentials') {
            steps {
                script {
                    echo "🔍 Checking Jenkins credentials..."
                    
                    def missingCredentials = []
                    def requiredCredentials = [
                        'caravan-postgres-db',
                        'caravan-postgres-user', 
                        'caravan-postgres-password',
                        'caravan-pgadmin-email',
                        'caravan-pgadmin-password',
                        'caravan-api-url'
                    ]
                    
                    requiredCredentials.each { credId ->
                        try {
                            def cred = credentials(credId)
                            if (cred == null) {
                                missingCredentials.add(credId)
                                echo "❌ Missing: ${credId}"
                            } else {
                                echo "✅ Found: ${credId}"
                            }
                        } catch (Exception e) {
                            missingCredentials.add(credId)
                            echo "❌ Error accessing: ${credId} - ${e.getMessage()}"
                        }
                    }
                    
                    if (missingCredentials.size() > 0) {
                        error """
                        🚨 Missing Jenkins credentials: ${missingCredentials.join(', ')}
                        
                        Please add these credentials in Jenkins:
                        - Go to: Manage Jenkins > Manage Credentials > System > Global credentials
                        - Add Credentials > Kind: Secret text
                        - Use the exact IDs listed above
                        
                        See JENKINS_SETUP.md for detailed instructions.
                        """
                    }
                    
                    echo "✅ All credentials found!"
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
                        string(credentialsId: 'caravan-pgadmin-email', variable: 'PGADMIN_DEFAULT_EMAIL'),
                        string(credentialsId: 'caravan-pgadmin-password', variable: 'PGADMIN_DEFAULT_PASSWORD'),
                        string(credentialsId: 'caravan-api-url', variable: 'VITE_API_URL')
                    ]) {
                        echo "📝 Creating .env file..."
                        
                        // Erstelle .env Datei mit writeFile (sicherer)
                        writeFile file: '.env', text: """# Database Configuration
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
"""
                        
                        // Validiere .env Datei
                        sh '''
                            if [ ! -f .env ] || [ ! -s .env ]; then
                                echo "❌ .env file is missing or empty!"
                                exit 1
                            fi
                            echo "✅ .env file created successfully"
                            echo "📄 .env content (without passwords):"
                            grep -v PASSWORD .env || true
                        '''
                        
                        echo "🐳 Stopping old containers..."
                        sh "docker compose -f ${env.COMPOSE_FILE} down --remove-orphans || true"
                        
                        echo "🔨 Building images..."
                        sh "docker compose -f ${env.COMPOSE_FILE} build --no-cache"
                        
                        echo "🚀 Starting services..."
                        sh "docker compose -f ${env.COMPOSE_FILE} up -d"
                        
                        echo "⏳ Waiting for services to be healthy..."
                        sh '''
                            sleep 30
                            docker compose -f '''${COMPOSE_FILE}''' ps
                        '''
                        
                        echo "🏥 Running health checks..."
                        sh """
                            echo "=== Health Check ==="
                            docker compose -f ${env.COMPOSE_FILE} ps
                            
                            echo "Checking backend health..."
                            curl -f http://localhost:${PROD_BACKEND_PORT}/actuator/health || echo "Backend health check failed"
                            
                            echo "Checking frontend..."
                            curl -f http://localhost:${PROD_FRONTEND_PORT} || echo "Frontend check failed"
                        """
                    }
                }
            }
        }
    }

    post {
        always {
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
