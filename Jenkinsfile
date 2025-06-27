pipeline {
  agent any

  environment {
    POSTGRES_DB         = credentials('caravan-postgres-db')
    POSTGRES_USER       = credentials('caravan-postgres-user')
    POSTGRES_PASSWORD   = credentials('caravan-postgres-password')
    PGADMIN_EMAIL       = credentials('caravan-pgadmin-email')
    PGADMIN_PASSWORD    = credentials('caravan-pgadmin-password')
    VITE_API_URL        = credentials('caravan-api-url')
  }

  stages {

    stage('Checkout') {
      steps {
        git 'https://github.com/dein-repo/caravan.git'
      }
    }

    stage('Build Images') {
      steps {
        sh 'docker build -t caravan-frontend ./frontend'
        sh 'docker build -t spring-boot-caravan-image ./backend'
      }
    }

    stage('Prepare Environment') {
      steps {
        sh '''
          echo "POSTGRES_DB=$POSTGRES_DB" > .env
          echo "POSTGRES_USER=$POSTGRES_USER" >> .env
          echo "POSTGRES_PASSWORD=$POSTGRES_PASSWORD" >> .env
          echo "PGADMIN_DEFAULT_EMAIL=$PGADMIN_EMAIL" >> .env
          echo "PGADMIN_DEFAULT_PASSWORD=$PGADMIN_PASSWORD" >> .env
          echo "VITE_API_URL=$VITE_API_URL" >> .env
        '''
      }
    }

    stage('Deploy') {
      steps {
        sh 'docker compose --env-file .env -f docker-compose.prod.yml up -d --build'
      }
    }

    stage('Health Check') {
      steps {
        sh 'curl --fail http://localhost:8080/actuator/health'
      }
    }
  }

  post {
    failure {
      echo '❌ Deployment failed - cleaning up...'
      sh 'docker compose -f docker-compose.prod.yml down --remove-orphans'
      sh 'rm -f .env'
    }
    always {
      sh 'rm -f .env'
    }
  }
}