pipeline {
  agent { label 'maven' }
  parameters {
    booleanParam(name: 'BUILD_DOCKER_IMAGES', defaultValue: false, 
      description: 'Triggers the building of docker images required for development')
  }
  options {
    timeout(time: 1, unit: 'HOURS')
    buildDiscarder(logRotator(numToKeepStr: '5')) 
  }

  stages{

    stage('build') {
      steps {
        container('maven-runner') {
          git(url: 'https://github.com/italiangrid/voms-admin-server.git', branch: env.BRANCH_NAME)
          sh 'mvn -B -U -P prod,EMI clean package'
        }
      }
    }

    stage('build-docker-images') {
      agent { label 'docker' }
      when {
        expression { return params.BUILD_DOCKER_IMAGES }
      }
      steps {
        container('docker-runner') {
          sh '''
          pushd docker/voms-admin-server
          sh build-image.sh && sh push-image.sh
          popd
          push docker/voms/centos6
          sh build-image.sh && sh push-image.sh
          popd'''
        }
      }
    }
  }
}
