#!/bin/bash

# Variables
WAR_NAME="sms.war"
BASE_DIR="/Users/giovannisuter/dev"
LOCAL_WAR_PATH="$BASE_DIR/projects/sms/back-end/workspace/sms-backend/target/$WAR_NAME"
TOMCAT_WEBAPPS="$BASE_DIR/tools/apache-tomcat-10.1.33/webapps"
TOMCAT_BIN="$BASE_DIR/tools/apache-tomcat-10.1.33/bin"
PORT=8080
LOG_FILE="$TOMCAT_WEBAPPS/../logs/sms_backend.log"
MSMTP_PATH="/opt/homebrew/bin/msmtp"

# Set environment for Jenkins
export JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home"  # Hardcoded Java 21
export PATH="$JAVA_HOME/bin:/opt/homebrew/bin:$PATH"

# Function to log messages with timestamp
log_message() {
    local LEVEL=$1
    local MESSAGE=$2
    local TIMESTAMP=$(date +"%Y-%m-%d %H:%M:%S")
    echo "[$TIMESTAMP] [$LEVEL] $MESSAGE"
    echo "[$TIMESTAMP] [$LEVEL] $MESSAGE" >> "$LOG_FILE"
}

# Function to check if the port is in use
is_port_in_use() {
    lsof -i :"$PORT" > /dev/null 2>&1
    return $?
}

# Function to stop Tomcat
stop_tomcat() {
    if is_port_in_use; then
        log_message "INFO" "Stopping Tomcat on port $PORT..."
        "$TOMCAT_BIN/shutdown.sh" || log_message "WARN" "Shutdown failed, forcing stop..."
        sleep 5
        if is_port_in_use; then
            log_message "INFO" "Force killing process using port $PORT..."
            kill -9 "$(lsof -t -i :$PORT)" 2>/dev/null || log_message "ERROR" "No process to kill."
        else
            log_message "INFO" "Tomcat stopped successfully."
        fi
    else
        log_message "INFO" "Tomcat is not running on port $PORT."
    fi
}

# Build the project
log_message "INFO" "Starting project build..."
cd "$BASE_DIR/projects/sms/back-end/workspace/sms-backend" || { log_message "ERROR" "Project directory not found!"; exit 1; }
chmod +x ./mvnw
./mvnw clean package -DskipTests || { log_message "ERROR" "Build failed!"; exit 1; }
log_message "INFO" "Build completed successfully."

# Stop Tomcat if running
stop_tomcat

# Deploy the WAR file
log_message "INFO" "Deploying $WAR_NAME to Tomcat..."
rm -rf "$TOMCAT_WEBAPPS/sms" "$TOMCAT_WEBAPPS/$WAR_NAME"
cp "$LOCAL_WAR_PATH" "$TOMCAT_WEBAPPS/" || { log_message "ERROR" "Failed to copy WAR file\!"; exit 1; }
log_message "INFO" "$WAR_NAME deployed successfully."

# Start Tomcat
log_message "INFO" "Starting Tomcat..."
"$TOMCAT_BIN/startup.sh" || { log_message "ERROR" "Tomcat startup failed!"; exit 1; }

# Wait for Tomcat to start
sleep 10

# Check if the application started
if is_port_in_use; then
    log_message "INFO" "Application started successfully on port $PORT."
    log_message "INFO" "Access it at http://localhost:$PORT/sms/"
    echo -e "Subject: Local Deployment SUCCESSFUL\n\nDeployment succeeded on $(hostname) at $(date)." | "$MSMTP_PATH" -C /Users/giovannisuter/.msmtprc -a icloud giovanni.suter@me.com || log_message "WARN" "Warning: Email failed to send."
else
    log_message "ERROR" "Failed to start the application. Check logs at $LOG_FILE."
    echo -e "Subject: Local Deployment FAILED\n\nDeployment failed on $(hostname) at $(date). Check logs at $LOG_FILE." | "$MSMTP_PATH" -C /Users/giovannisuter/.msmtprc -a icloud giovanni.suter@me.com || log_message "WARN" "Warning: Email failed to send."
    exit 1
fi