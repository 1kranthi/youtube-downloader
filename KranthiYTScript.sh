#!/bin/bash

# Update and install required packages
sudo yum update -y

# Install Java 11
sudo yum install java-11-openjdk-devel -y

# Install Git
sudo yum install git -y

# Install Maven
sudo yum install maven -y

# Install yt-dlp
sudo yum install python3-pip -y
pip3 install yt-dlp

# Install Nginx
sudo yum install nginx -y

# Build the project using Maven
mvn clean package

# Configure Nginx
sudo bash -c 'cat > /etc/nginx/nginx.conf <<EOF
user nginx;
worker_processes auto;
error_log /var/log/nginx/error.log notice;
pid /run/nginx.pid;

# Load dynamic modules.
include /usr/share/nginx/modules/*.conf;
server {
    listen 81;
    listen [::]:81;
    server_name _;

    location / {
        root /usr/share/nginx/html;
        index index.html index.htm;
    }

    location /api/ {
        proxy_pass http://localhost:8082/;  # Ensure the correct port number is set here
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }

    error_page 404 /404.html;
    location = /404.html {
    }

    error_page 500 502 503 504 /50x.html;
    location = /50x.html {
    }
}
EOF'

# Test Nginx configuration
sudo nginx -t

# Restart Nginx
sudo systemctl restart nginx

# Run the application in the background
cd target || { echo "Directory 'target' does not exist"; exit 1; }
nohup java -jar youtube-downloader-0.0.1-SNAPSHOT.jar > /home/ec2-user/app/app.log 2>&1 &

echo "Setup complete. Your application is running and Nginx is configured."
