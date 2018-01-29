pipeline {
  agent any
  stages {
    stage('Build') {
      parallel {
        stage('Build') {
          steps {
            sh 'sh ./gradlew shadowJar'
          }
        }
        stage('upload to sonarqube') {
          steps {
            sh 'sh ./gradlew sonarqube -Dsonar.host.url=http://sonar.indiabizforsale.com -Dsonar.login=dba561fd9e8e2158f9947c15706159d6ef074259'
          }
        }
      }
    }
    stage('docker build') {
      steps {
        sh 'docker build -t gcr.io/fleet-pillar-174206/com.indiabizforsale/email-service:${BUILD_NUMBER} '
      }
    }
    stage('upload to kubernentes') {
      steps {
        sh '''gcloud config set project \'fleet-pillar-174206\'
gcloud config set compute/zone us-central1-a'''
      }
    }
    stage('deploy image') {
      steps {
        sh 'gcloud container clusters get-credentials email-service --zone us-central1-a --project fleet-pillar-174206'
        sh 'kubectl set image deployment/email-service email-service=gcr.io/fleet-pillar-174206/com.indiabizforsale/email-service:${BUILD_NUMBER}'
      }
    }
  }
}