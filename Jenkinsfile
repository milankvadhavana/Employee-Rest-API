pipeline {
  agent any
  
  tools {
    maven 'Maven'  // Use the Maven installation name from Jenkins
  }
  
  environment {
    DOCKER_IMAGE = 'milanvadhavana/employee-api'
    DOCKER_TAG = "${BUILD_NUMBER}"
  }

  stages {
    stage('Checkout') {
      steps {
        git branch: 'main',
            url: 'https://github.com/milankvadhavana/Employee-Rest-API.git',
            credentialsId: 'github-credss'
      }
    }

    stage('Build & Test') {
      steps {
        bat '''
          echo Building with Maven...
          mvn --version
          mvn clean compile
          mvn test
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Docker Build') {
      steps {
        bat """
          docker build -t ${env.DOCKER_IMAGE}:${env.DOCKER_TAG} .
          docker tag ${env.DOCKER_IMAGE}:${env.DOCKER_TAG} ${env.DOCKER_IMAGE}:latest
        """
      }
    }

    stage('Docker Push') {
      steps {
        withCredentials([usernamePassword(
            credentialsId: 'docker-credss',
            usernameVariable: 'DOCKER_USERNAME',
            passwordVariable: 'DOCKER_PASSWORD'
        )]) {
          bat """
            echo %DOCKER_PASSWORD% | docker login -u %DOCKER_USERNAME% --password-stdin
            docker push %DOCKER_IMAGE%:%DOCKER_TAG%
            docker push %DOCKER_IMAGE%:latest
          """
        }
      }
    }

    stage('Deploy') {
      steps {
        bat """
          docker stop springboot-app || exit 0
          docker rm springboot-app || exit 0
          docker run -d -p 9090:9090 --name springboot-app %DOCKER_IMAGE%:latest
        """
      }
    }
  }

  post {
    success { 
      echo 'Pipeline completed successfully!' 
    }
    failure { 
      echo 'Pipeline failed! Check the logs above.'
    }
  }
}