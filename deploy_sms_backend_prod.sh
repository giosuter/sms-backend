#!/bin/bash

# === Configuration ===
LOCAL_PROJECT_DIR="/Users/giovannisuter/dev/projects/sms/back-end/workspace/sms-backend"
WAR_FILE="sms.war"
REMOTE_USER="zitatusi"
REMOTE_HOST="zitatusi.myhostpoint.ch"
REMOTE_DEPLOY_DIR="/home/zitatusi/deploy"
REMOTE_TOMCAT_DIR="app/tools/tomcat/apache-tomcat-10.1.33"
REMOTE_WEBAPPS="$REMOTE_TOMCAT_DIR/webapps"
REMOTE_WAR_FILE="$REMOTE_WEBAPPS/$WAR_FILE"
REMOTE_LOGS_DIR="$REMOTE_TOMCAT_DIR/logs"

# === Step 1: Build the Project ===
echo "🔨 Building the project..."
cd "$LOCAL_PROJECT_DIR" || { echo "❌ ERROR: Could not change to project directory!"; exit 1; }
./mvnw clean package -DskipTests || { echo "❌ ERROR: Build failed!"; exit 1; }

# === Step 2: Copy WAR File to Remote Server ===
echo "📤 Copying WAR file to remote server..."
scp "target/$WAR_FILE" "$REMOTE_USER@$REMOTE_HOST:$REMOTE_DEPLOY_DIR/" || { echo "❌ ERROR: File transfer failed!"; exit 1; }

# === Step 3: Deploy on Remote Server ===
echo "🚀 Deploying on remote server..."
ssh "$REMOTE_USER@$REMOTE_HOST" << EOF
    echo "🛑 Stopping Tomcat..."
    pkill -f tomcat || echo "Tomcat was not running."

    echo "🧹 Cleaning old deployment..."
    rm -rf "$REMOTE_WEBAPPS/sms"
    rm -f "$REMOTE_WAR_FILE"

    echo "🗑️ Deleting log files..."
    rm -rf "$REMOTE_LOGS_DIR"/*

    echo "📂 Moving new WAR file..."
    mv "$REMOTE_DEPLOY_DIR/$WAR_FILE" "$REMOTE_WAR_FILE"

    echo "🚀 Starting Tomcat..."
    "$REMOTE_TOMCAT_DIR/bin/startup.sh"

    echo "✅ Deployment complete!"
EOF