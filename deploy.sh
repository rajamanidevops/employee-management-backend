#!/bin/bash

# ---------- CONFIG ----------
FRONTEND_PATH="/mnt/c/Users/rajamani.gg/employee-app"
BACKEND_PATH="/mnt/d/Desktop/Java programs/employee-backend"
STATIC_PATH="$BACKEND_PATH/src/main/resources/static"
MVNW="$BACKEND_PATH/mvnw"
JAVA_HOME="/usr/lib/jvm/java-21-openjdk-amd64"
# ----------------------------

# Set Java environment
export JAVA_HOME=$JAVA_HOME
export PATH=$JAVA_HOME/bin:$PATH

echo "Building Angular frontend..."
cd "$FRONTEND_PATH" || exit
npm install
ng build --configuration production

echo "Copying Angular build to Spring Boot..."
rm -rf "$STATIC_PATH"/*
mkdir -p "$STATIC_PATH"
cp -r "$FRONTEND_PATH/dist/employee-app/"* "$STATIC_PATH/"

echo "Stopping any running Spring Boot backend..."
PID=$(ps aux | grep '[j]ava.*employee-backend' | awk '{print $2}')
if [ ! -z "$PID" ]; then
    echo "Killing process $PID"
    kill -9 $PID
else
    echo "No running backend process found."
fi

echo "Starting Spring Boot backend..."
cd "$BACKEND_PATH" || exit
nohup $MVNW spring-boot:run > backend.log 2>&1 &

echo "Deployment complete!"
echo "Frontend + Backend running at http://localhost:8080"
