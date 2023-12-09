#!/bin/bash

echo "🏁 Script Start."
echo "👉 Pulling github repository..."
cd RSS-Reader/
git pull origin release

echo "👉 Pulling backend Docker image..."
cd ..
cat TOKEN.txt | docker login https://ghcr.io -u outsideris --password-stdin
sudo docker pull ghcr.io/flytrap-ware/rss-reader:release

echo "👉 Starting Docker Compose..."
cd RSS-Reader/
sudo docker-compose up -d

echo "👉 Cleaning up unused Docker images..."
sudo docker image prune -a -f

echo "🫡  Script execution completed."
