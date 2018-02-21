pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh 'sh ./gradlew shadowJar'
        sh 'gsutil cp gs://ibfs-credentials/email-service/Dockerfile .'
      }
    }
    stage('docker build') {
      steps {
        sh 'docker build -t gcr.io/fleet-pillar-174206/com.indiabizforsale/email-service:${BUILD_NUMBER} .'
        sh 'gcloud docker -- push gcr.io/fleet-pillar-174206/com.indiabizforsale/email-service:${BUILD_NUMBER}'
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
        sh 'gcloud container clusters get-credentials email-service-cluster --zone us-central1-a --project fleet-pillar-174206'
        sh 'kubectl set image deployment/email-service email-service=gcr.io/fleet-pillar-174206/com.indiabizforsale/email-service:${BUILD_NUMBER}'
      }
    }
  }
}