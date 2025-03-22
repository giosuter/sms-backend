#!/bin/bash

# Variables
WAR_NAME="sms.war"
LOCAL_WAR_PATH="/Users/giovannisuter/dev/projects/sms/back-end/workspace/sms-backend/target/$WAR_NAME"
# Corrected Variables
TOMCAT_WEBAPPS="/Users/giovannisuter/dev/tools/apache-tomcat-10.1.33/webapps"
TOMCAT_BIN="/Users/giovannisuter/dev/tools/apache-tomcat-10.1.33/bin"
PORT=8080
LOG_FILE="sms_backend.log"

# Function to check if the port is in use
is_port_in_use() {
    lsof -i :$PORT > /dev/null
}

# Function to stop Tomcat
stop_tomcat() {
    if is_port_in_use; then
        echo "Stopping Tomcat on port $PORT..."
        $TOMCAT_BIN/shutdown.sh
        sleep 5

        if is_port_in_use; then
            echo "Force killing process using port $PORT..."
            kill -9 $(lsof -t -i :$PORT)
        fi
    fi
}

# Build the project
echo "Building the project..."
cd /Users/giovannisuter/dev/projects/sms/back-end/workspace/sms-backend || { echo "Project directory not found!"; exit 1; }
./mvnw clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "Build failed! Exiting..."
    exit 1
fi

# Stop Tomcat if running
stop_tomcat

# Deploy the WAR file
echo "Deploying $WAR_NAME to Tomcat..."
rm -rf "$TOMCAT_WEBAPPS/sms"
rm -f "$TOMCAT_WEBAPPS/$WAR_NAME"
cp "$LOCAL_WAR_PATH" "$TOMCAT_WEBAPPS/"

# Start Tomcat
echo "Starting Tomcat..."
$TOMCAT_BIN/startup.sh

# Wait for Tomcat to start
sleep 5

# Check if the application started
if is_port_in_use; then
    echo "Application started successfully on port $PORT."
    echo "Access it at http://localhost:8080/sms/"
else
    echo "Failed to start the application. Check logs at $LOG_FILE."
    exit 1
fi

# Send e-mail if deployment was successful or if it failed.
echo "Local deployment started on $(hostname)" | mail -s "Local Deployment Started" giovanni.suter@me.com

# Run deployment commands here...

if [ $? -ne 0 ]; then
    echo "Local deployment failed on $(hostname)" | mail -s "Local Deployment Failed" giovanni.suter@me.com
    exit 1
fi

echo "Local deployment successful on $(hostname)" | mail -s "Local Deployment Successful" giovanni.suter@me.com