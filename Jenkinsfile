pipeline {
    agent any
    tools {
            gradle 'Gradle 8.10.2'
        }
    stages {
    stage('Check Gradle Version') {
                steps {
                    sh 'gradle -v'
                }
            }
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    sh './scripts/run-tests.sh'
                }
            }
        }
    }
    post {
        success {
            mail to: 'alina.brusya@gmail.com',
                 subject: "Build Success",
                 body: "The Jenkins build has completed successfully"
        }
        failure {
            mail to: 'alina.brusya@gmail.com',
                 subject: "Build Failure",
                 body: "The Jenkins build has failed. Please check the logs."
        }
    }
}
