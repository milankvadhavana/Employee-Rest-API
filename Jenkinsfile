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
        bat '''
            echo ========================================
            echo Deploying container...
            echo ========================================
            
            echo Current containers:
            docker ps -a
            
            echo.
            echo Removing old container if exists...
            docker stop springboot-app 2>nul || echo Container not running
            docker rm springboot-app 2>nul || echo Container does not exist
            
            echo.
            echo Pulling latest image...
            docker pull %DOCKER_IMAGE%:latest
            
            echo.
            echo Starting new container...
            docker run -d -p 9090:9090 --name springboot-app %DOCKER_IMAGE%:latest
            
            echo.
            echo Waiting for container to start...
            timeout /t 3 /nobreak >nul
            
            echo.
            echo Checking container status...
            docker ps --filter "name=springboot-app"
            
            echo.
            echo Checking container logs...
            docker logs springboot-app --tail 20
            
            echo.
            echo Testing endpoint...
            curl http://localhost:9090/employees 2>nul || echo Endpoint not responding yet
        '''
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