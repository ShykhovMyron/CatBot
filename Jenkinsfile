pipeline {
    agent any
    environment {
        TEMP_DIR = "/tmp/jenkins-temp-dir"
        SERVER_USERNAME_HOSTNAME = "${env.REMOTE_MACHINE_USERNAME}@${env.REMOTE_MACHINE_PUBLIC_DNS}"
        PATH_TO_LOCAL_DOCKER_COMPOSE = "/docker/prod/docker-compose.yml"
        PATH_TO_REMOTE_DOCKER_COMPOSE = "/catbot/docker/prod/docker-compose.yml"

        // required variables:GIT_CREDENTIALS_ID, GIT_REPOSITORY, IMAGE_NAME, DOCKER_HUB_CREDENTIALS_ID, CAT_BOT_TOKEN
    }
    stages {
        stage("Deploy") {
            stages {
//                stage("Clone CatBot-master repo") {
//                    steps {
//                        dir(TEMP_DIR) {
//                            git branch: "master",
//                                    credentialsId: "${env.GIT_CREDENTIALS_ID}",
//                                    url: "${env.GIT_REPOSITORY}"
//                        }
//                    }
//                }
//                stage("Build Docker image") {
//                    steps {
//                        dir(TEMP_DIR) {
//                            sh "docker rmi ${env.IMAGE_NAME} || true"
//                            sh "docker build -t ${env.IMAGE_NAME} ."
//                        }
//                    }
//                }
//                stage("Push Docker image to Docker Hub") {
//                    steps {
//                        withCredentials([usernamePassword(credentialsId: "${env.DOCKER_HUB_CREDENTIALS_ID}", usernameVariable: "DOCKER_HUB_USERNAME", passwordVariable: "DOCKER_HUB_PASSWORD")]) {
//                            sh "docker login -u ${DOCKER_HUB_USERNAME} -p ${DOCKER_HUB_PASSWORD}"
//                            sh "docker push ${env.IMAGE_NAME}"
//                        }
//                    }
//                }
                stage("Deploy to Remote Machine") {
                    steps {
                        runSSHCommand("mkdir -p \$(dirname '${PATH_TO_REMOTE_DOCKER_COMPOSE}')")
                        runSSHCommand("docker-compose -f ${PATH_TO_REMOTE_DOCKER_COMPOSE} down || true")
                        sh "scp -i ${env.REMOTE_MACHINE_PRIVATE_KEY_PATH} ${TEMP_DIR}${PATH_TO_LOCAL_DOCKER_COMPOSE} ${SERVER_USERNAME_HOSTNAME}:${PATH_TO_REMOTE_DOCKER_COMPOSE}"
                        runSSHCommand("docker rmi ${env.IMAGE_NAME} || true")
                        runSSHCommand("docker-compose -f ${PATH_TO_REMOTE_DOCKER_COMPOSE} up -d -e CAT_BOT_TOKEN=${env.CAT_BOT_TOKEN} -e IMAGE_NAME=${env.IMAGE_NAME}")
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

def runSSHCommand(command) {
    sh "ssh -i ${env.REMOTE_MACHINE_PRIVATE_KEY_PATH} ${SERVER_USERNAME_HOSTNAME} '${command}'"
}
