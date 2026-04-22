pipeline {
    agent any
    
    tools {
        maven 'Maven'  
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
                    credentialsId: 'github-credentails'
            }
        }

        stage('Check Docker') {
            steps {
                bat '''
                    echo Checking Docker installation...
                    docker --version
                    if %errorlevel% neq 0 (
                        echo Docker not installed!
                        exit /b 1
                    )
                    
                    echo Checking Docker daemon...
                    docker ps
                    if %errorlevel% neq 0 (
                        echo ========================================
                        echo ERROR: Docker daemon is not running!
                        echo ========================================
                        echo Please start Docker Desktop first.
                        echo Then restart Jenkins service.
                        echo ========================================
                        exit /b 1
                    )
                    echo Docker is ready!
                '''
            }
        }

        stage('Build & Test') {
            steps {
                bat 'mvn clean test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                bat "docker build -t %DOCKER_IMAGE%:%DOCKER_TAG% ."
                bat "docker tag %DOCKER_IMAGE%:%DOCKER_TAG% %DOCKER_IMAGE%:latest"
            }
        }

     stage('Docker Push') {
    	steps {
        	withCredentials([usernamePassword(
            	credentialsId: 'dockerhub-credentials',
            	usernameVariable: 'DOCKER_USERNAME',
            	passwordVariable: 'DOCKER_PASSWORD'
        	)]) {
            bat '''
                echo %DOCKER_PASSWORD% | docker login -u %DOCKER_USERNAME% --password-stdin
                docker push %DOCKER_IMAGE%:%DOCKER_TAG%
                docker push %DOCKER_IMAGE%:latest
                docker logout
            '''
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
    stage('Deploy') {
      steps {
        bat """
          docker stop springboot-app || exit 0
          docker rm springboot-app || exit 0
          docker run -d -p 9090:9090 --name springboot-app %DOCKER_IMAGE%:latest
        """
      }
    post {
        success { 
            echo '========================================'
            echo '✅ Pipeline completed successfully!'
            echo '🌐 Application: http://localhost:9090'
            echo '========================================'
        }
        failure { 
            echo '========================================'
            echo '❌ Pipeline failed!'
            echo '========================================'
        }
>>>>>>> 817681312d2f18422be3b24b95b35fb1e68eb6f8
    }
}