#!/bin/bash

printenv

# Variables
WAR_NAME="sms.war"
LOCAL_WAR_PATH="/Users/giovannisuter/dev/projects/sms/back-end/workspace/sms-backend/target/$WAR_NAME"
TOMCAT_WEBAPPS="/Users/giovannisuter/dev/tools/apache-tomcat-10.1.33/webapps"
TOMCAT_BIN="/Users/giovannisuter/dev/tools/apache-tomcat-10.1.33/bin"
PORT=8080
LOG_FILE="$TOMCAT_WEBAPPS/../logs/sms_backend.log"  # Adjusted to Tomcat’s log dir

# Function to check if the port is in use
is_port_in_use() {
    lsof -i :"$PORT" > /dev/null 2>&1
    return $?
}

# Function to stop Tomcat
stop_tomcat() {
    if is_port_in_use; then
        echo "Stopping Tomcat on port $PORT..."
        "$TOMCAT_BIN/shutdown.sh" > /dev/null 2>&1
        sleep 5

        if is_port_in_use; then
            echo "Force killing process using port $PORT..."
            kill -9 "$(lsof -t -i :$PORT)" 2>/dev/null
        fi
    else
        echo "Tomcat is not running on port $PORT."
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
rm -rf "$TOMCAT_WEBAPPS/sms" "$TOMCAT_WEBAPPS/$WAR_NAME"
cp "$LOCAL_WAR_PATH" "$TOMCAT_WEBAPPS/" || { echo "Failed to copy WAR file!"; exit 1; }

# Start Tomcat
echo "Starting Tomcat..."
"$TOMCAT_BIN/startup.sh" > /dev/null 2>&1

# Wait for Tomcat to start
sleep 10  # Increased for stability

# Check if the application started
if is_port_in_use; then
    echo "Application started successfully on port $PORT."
    echo "Access it at http://localhost:$PORT/sms/"
    # Send success email via msmtp
    echo -e "Subject: local deployment SUCCESSFUL\n\nDeployment succeeded on $(hostname) at $(date)." | msmtp -a icloud giovanni.suter@me.com
else
    echo "Failed to start the application. Check logs at $LOG_FILE."
    # Send failure email via msmtp
    echo -e "Subject: local deployment FAILED\n\nDeployment failed on $(hostname) at $(date). Check logs at $LOG_FILE." | msmtp -a icloud giovanni.suter@me.com
    exit 1
fi