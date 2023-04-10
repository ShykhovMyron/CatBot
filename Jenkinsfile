pipeline {
    agent any
    environment {
        //env
        CAT_BOT_TOKEN = "${env.CAT_BOT_TOKEN}"
        DOCKER_HUB_CREDENTIALS_ID = "${env.DOCKER_HUB_CREDENTIALS_ID}"
        GIT_CREDENTIALS_ID = "${env.GIT_CREDENTIALS_ID}"
        IMAGE_NAME = "${env.IMAGE_NAME}"
        REMOTE_MACHINE_PRIVATE_KEY_PATH = "${env.REMOTE_MACHINE_PRIVATE_KEY_PATH}"
        REMOTE_MACHINE_PUBLIC_DNS = "${env.REMOTE_MACHINE_PUBLIC_DNS}"
        REMOTE_MACHINE_SSH_CONFIG = "${env.REMOTE_MACHINE_SSH_CONFIG}"
        REMOTE_MACHINE_USERNAME = "${env.REMOTE_MACHINE_USERNAME}"

        SERVER_USERNAME_HOSTNAME = "${REMOTE_MACHINE_USERNAME}@${REMOTE_MACHINE_PUBLIC_DNS}"
        TEMP_DIR = "/tmp/jenkins-temp-dir"
        PATH_TO_LOCAL_DOCKER_COMPOSE = "/docker/prod/docker-compose.yaml"
        PATH_TO_REMOTE_HOME_DIR = "~"
        PATH_TO_REMOTE_DOCKER_COMPOSE = "/catbot/docker/prod/docker-compose.yaml"
    }
    stages {
        stage("Deploy") {
            stages {
                stage("Clone CatBot-master repo") {
                    steps {
                        dir(TEMP_DIR) {
                            git branch: "master",
                                    credentialsId: "${GIT_CREDENTIALS_ID}",
                                    url: "${GIT_REPOSITORY}"
                        }
                    }
                }
                stage("Build Docker image") {
                    steps {
                        dir(TEMP_DIR) {
                            sh "docker rmi ${IMAGE_NAME} || true"
                            sh "docker build -t ${IMAGE_NAME} ."
                        }
                    }
                }
                stage("Push Docker image to Docker Hub") {
                    steps {
                        withCredentials([usernamePassword(credentialsId: "${DOCKER_HUB_CREDENTIALS_ID}", usernameVariable: "DOCKER_HUB_USERNAME", passwordVariable: "DOCKER_HUB_PASSWORD")]) {
                            sh "docker login -u ${DOCKER_HUB_USERNAME} -p ${DOCKER_HUB_PASSWORD}"
                            sh "docker push ${IMAGE_NAME}"
                        }
                    }
                }
                stage("Deploy to Remote Machine") {
                    stages {
                        stage("Crete working directory if not exist") {
                            steps {
                                runSSHCommand("mkdir -p \$(dirname '${PATH_TO_REMOTE_HOME_DIR}${PATH_TO_REMOTE_DOCKER_COMPOSE}')")
                            }
                        }
                        stage("Stopping a past version of an application") {
                            steps {
                                runSSHCommand("""
                                        export IMAGE_NAME=${IMAGE_NAME}
                                        docker-compose -f ${PATH_TO_REMOTE_HOME_DIR}${PATH_TO_REMOTE_DOCKER_COMPOSE} down || true
                                """)
                            }
                        }
                        stage("Copying a new docker-compose file to a remote machine (will override if exist)") {
                            steps {
                                sh "scp -i ${REMOTE_MACHINE_PRIVATE_KEY_PATH} ${TEMP_DIR}${PATH_TO_LOCAL_DOCKER_COMPOSE} ${SERVER_USERNAME_HOSTNAME}:${PATH_TO_REMOTE_HOME_DIR}${PATH_TO_REMOTE_DOCKER_COMPOSE}"
                            }
                        }
                        stage("Starting new version of application") {
                            steps {
                                runSSHCommand("docker rmi ${IMAGE_NAME} || true")
                                runSSHCommand("""
                                        export CAT_BOT_TOKEN=${CAT_BOT_TOKEN}
                                        export IMAGE_NAME=${IMAGE_NAME}
                                        docker-compose -f ${PATH_TO_REMOTE_HOME_DIR}${PATH_TO_REMOTE_DOCKER_COMPOSE} up -d
                                """)
                            }
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

def runSSHCommand(command) {
    sh "ssh -i ${REMOTE_MACHINE_PRIVATE_KEY_PATH} ${SERVER_USERNAME_HOSTNAME} '${command}'"
}
