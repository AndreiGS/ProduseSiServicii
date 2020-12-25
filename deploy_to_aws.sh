#!/bin/bash

echo 'Starting to Deploy...'
ssh ec2-user@ec2-18-198-47-240.eu-central-1.compute.amazonaws.com " 
        sudo docker image prune -f 
        cd ProduseSiServicii
        sudo docker-compose down
        git fetch origin
        git reset --hard origin/master  &&  echo 'You are doing well'
        sudo docker-compose build && sudo docker-compose up -d
"
echo 'Deployment completed successfully'
