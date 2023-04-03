# InfCatBot

#### Preview https://t.me/infcatbot

InfCatBot is a simple Telegram bot that sends you a random cat GIF using the https://cataas.com API. The bot currently has one command, `/cat`, which triggers the sending of a GIF.

## Prerequisites

Before running the bot, you'll need to install the following software:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

## Installation

To install and run the bot:

1. Clone this repository to your local machine:  
   `git clone https://github.com/your-username/infcatbot.git`

2. Build the Docker image using the provided Dockerfile:  
   `docker build -t catbot .`

3. Set the following environment variables in Docker Compose prod file (<project-dir>/docker/prod/docker-compose.yaml):   
   `CAT_BOT_TOKEN=<your Telegram bot token>`

4. Start the bot using Docker Compose:  
   `cd ./docker/prod`  
   `docker-compose up -d`


5. Send the `/cat` command to the bot in Telegram to receive a random cat GIF.

## Contributing

If you'd like to contribute to this project, please fork the repository and create a new branch for your changes. After making your changes, submit a pull request and your changes will be reviewed for inclusion in the main project.
    
