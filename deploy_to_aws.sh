
#!/bin/bash

echo 'Starting to Deploy...'
ssh ec2-user@ec2-18-198-47-240.eu-central-1.compute.amazonaws.com << HERE
        cd ProduseSiServicii
        sudo docker-compose down
        sudo docker image prune -f 
        sudo docker rmi -f findthebusinessfe
        sudo docker rmi -f findthebusinessbe
        git fetch origin
        git reset --hard origin/master
        
        export NODE_ENV=${NODE_ENV}
        export VUE_APP_STRIPE_PUBLIC_KEY=${VUE_APP_STRIPE_PUBLIC_KEY}
        export VUE_APP_FACEBOOK_CLIENT_ID=${VUE_APP_FACEBOOK_CLIENT_ID}
        export VUE_APP_BACKEND=${VUE_APP_BACKEND}
        export VUE_APP_FRONTEND=${VUE_APP_FRONTEND}
        export SPRING_ENV=${SPRING_ENV}
        export JDBC_DATABASE_URL=${JDBC_DATABASE_URL}
        export JDBC_DATABASE_NAME=${JDBC_DATABASE_NAME}
        export JDBC_DATABASE_PORT=${JDBC_DATABASE_PORT}
        export JDBC_DATABASE_USERNAME=${JDBC_DATABASE_USERNAME}
        export JDBC_DATABASE_PASSWORD=${JDBC_DATABASE_PASSWORD}
        export AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}
        export AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}
        export AWS_S3_BUCKET=${AWS_S3_BUCKET}
        export AWS_S3_REGION=${AWS_S3_REGION}
        export FACEBOOK_APP_ID=${FACEBOOK_APP_ID}
        export FACEBOOK_SECRET=${FACEBOOK_SECRET}
        export SPRING_APP_STRIPE=${SPRING_APP_STRIPE}
        export SPRING_APP_FRONTEND_1=${SPRING_APP_FRONTEND_1}
        export SPRING_APP_FRONTEND_2=${SPRING_APP_FRONTEND_2}
        export SPRING_APP_TOKEN_SECRET_REFRESH_TOKEN=${SPRING_APP_TOKEN_SECRET_REFRESH_TOKEN}
        export SPRING_APP_TOKEN_SECRET_ACCESS_TOKEN=${SPRING_APP_TOKEN_SECRET_ACCESS_TOKEN}
        export SPRING_APP_TOKEN_SECRET_CHANGE_PASSWORD=${SPRING_APP_TOKEN_SECRET_CHANGE_PASSWORD}
        export SPRING_APP_TOKEN_SECRET_CHANGE_INFO=${SPRING_APP_TOKEN_SECRET_CHANGE_INFO}
        export EMAIL_APP_PASS=${EMAIL_APP_PASS}
        export CRYPT_KEY="${CRYPT_KEY}"
        docker-compose up -d --build
HERE
echo 'Deployment completed successfully'
