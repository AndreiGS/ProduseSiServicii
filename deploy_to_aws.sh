
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
        cd FindTheBusinessFE
        touch .env
        echo "
        NODE_ENV=${NODE_ENV}
        VUE_APP_STRIPE_PUBLIC_KEY=${VUE_APP_STRIPE_PUBLIC_KEY}
        VUE_APP_FACEBOOK_CLIENT_ID=${VUE_APP_FACEBOOK_CLIENT_ID}
        VUE_APP_BACKEND=${VUE_APP_BACKEND}
        VUE_APP_FRONTEND=${VUE_APP_FRONTEND}
        " > .env.production
        cd ..
        echo "
        #client
        NODE_ENV=${NODE_ENV}
        VUE_APP_STRIPE_PUBLIC_KEY=${VUE_APP_STRIPE_PUBLIC_KEY}
        VUE_APP_FACEBOOK_CLIENT_ID=${VUE_APP_FACEBOOK_CLIENT_ID}
        VUE_APP_BACKEND=${VUE_APP_BACKEND}
        VUE_APP_FRONTEND=${VUE_APP_FRONTEND}
        
        #server
        SPRING_ENV=${SPRING_ENV}
        JDBC_DATABASE_URL=${JDBC_DATABASE_URL}
        JDBC_DATABASE_NAME=${JDBC_DATABASE_NAME}
        JDBC_DATABASE_PORT=${JDBC_DATABASE_PORT}
        JDBC_DATABASE_USERNAME=${JDBC_DATABASE_USERNAME}
        JDBC_DATABASE_PASSWORD=${JDBC_DATABASE_PASSWORD}
        AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}
        AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}
        AWS_S3_BUCKET=${AWS_S3_BUCKET}
        AWS_S3_REGION=${AWS_S3_REGION}
        FACEBOOK_APP_ID=${FACEBOOK_APP_ID}
        FACEBOOK_SECRET=${FACEBOOK_SECRET}
        SPRING_APP_STRIPE=${SPRING_APP_STRIPE}
        SPRING_APP_FRONTEND_1=${SPRING_APP_FRONTEND_1}
        SPRING_APP_FRONTEND_2=${SPRING_APP_FRONTEND_2}
        SPRING_APP_TOKEN_SECRET_REFRESH_TOKEN=${SPRING_APP_TOKEN_SECRET_REFRESH_TOKEN}
        SPRING_APP_TOKEN_SECRET_ACCESS_TOKEN=${SPRING_APP_TOKEN_SECRET_ACCESS_TOKEN}
        SPRING_APP_TOKEN_SECRET_CHANGE_PASSWORD=${SPRING_APP_TOKEN_SECRET_CHANGE_PASSWORD}
        SPRING_APP_TOKEN_SECRET_CHANGE_INFO=${SPRING_APP_TOKEN_SECRET_CHANGE_INFO}
        EMAIL_APP_PASS=${EMAIL_APP_PASS}
        CRYPT_KEY=${CRYPT_KEY}
        " > .env
        docker-compose up -d --build
HERE
echo 'Deployment completed successfully'
