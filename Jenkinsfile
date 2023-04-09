pipeline {
    agent any
    environment {
        TEMP_DIR = "/tmp/jenkins-temp-dir"

        // required variables:GIT_CREDENTIALS_ID, GIT_REPOSITORY, IMAGE_NAME, DOCKER_HUB_CREDENTIALS_ID
    }
    stages {
        stage("Deploy") {
            stages {
                stage("Clone CatBot-master repo") {
                    steps {
                        dir(TEMP_DIR) {
                            git branch: "master",
                                    credentialsId: "${env.GIT_CREDENTIALS_ID}",
                                    url: "${env.GIT_REPOSITORY}"
                        }
                    }
                }
                stage("Build Docker image") {
                    steps {
                        dir(TEMP_DIR) {
                            sh "docker rmi ${env.IMAGE_NAME}"
                            sh "docker build -t ${env.IMAGE_NAME} ."
                        }
                    }
                }
                stage("Push Docker image to Docker Hub") {
                    steps {
                        withCredentials([string(credentialsId: env.DOCKER_HUB_CREDENTIALS_ID, variable: DOCKER_HUB_CREDENTIALS)]) {
                            sh "docker login -u shykhovmyron -p ${DOCKER_HUB_CREDENTIALS}"
                            sh "docker push ${env.IMAGE_NAME}"
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            sh "rm -rf ${TEMP_DIR}*"
        }
    }
}
