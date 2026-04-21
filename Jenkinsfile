pipeline {
  agent any
  
  tools {
    // Make sure these match your Jenkins Global Tool Configuration
    maven 'Maven-3.9.9'  // Change if needed
    jdk 'JDK-17'         // Change if needed
  }
  
  environment {
    DOCKER_IMAGE = 'milanvadhavana/employee-api'
    DOCKER_TAG = "${BUILD_NUMBER}"
  }

  stages {
    stage('Checkout') {
      steps {
        git branch: 'main',
            url: 'https://github.com/milankvadhavana/Employee-Rest-API.git'
      }
    }

    stage('Build & Test') {
      steps {
        sh 'mvn clean test'
      }
      post {
        always {
          junit '**/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Docker Build') {
      steps {
        sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
        sh "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest"
      }
    }

    stage('Docker Push') {
      steps {
        script {
          // Using withCredentials properly
          withCredentials([usernamePassword(
            credentialsId: 'docker-credss',
            usernameVariable: 'DOCKER_USERNAME',
            passwordVariable: 'DOCKER_PASSWORD'
          )]) {
            // Better Docker login method
            sh '''
              echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin
            '''
            sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
            sh "docker push ${DOCKER_IMAGE}:latest"
          }
        }
      }
    }

    stage('Deploy') {
      steps {
        script {
          sh '''
            docker stop springboot-app || true
            docker rm springboot-app || true
            docker run -d -p 9090:8080 --name springboot-app ${DOCKER_IMAGE}:latest
          '''
        }
      }
    }
  }

  post {
    success { 
      echo 'Pipeline succeeded!' 
    }
    failure { 
      echo 'Pipeline failed — check logs!'
      // Optional: Clean up on failure
      script {
        sh 'docker logout || true'
      }
    }
  }
}