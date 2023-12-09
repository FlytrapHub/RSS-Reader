upstream back-server {
   server was-dev:8080;
}

upstream front-server {
   server 172.17.0.1:3000; # TODO : front 서버 url로 변경할 것
}

server {
    listen 80;
    server_name 15.165.223.16;

    location / {
        proxy_pass http://front-server;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;

        proxy_buffer_size          128k;
        proxy_buffers              4 256k;
        proxy_busy_buffers_size    256k;

        proxy_http_version 1.1;
        add_header Access-Control-Allow-Credentials "true";
        add_header Access-Control-Allow-Origin *;

    }

    location /api/ {
        proxy_pass http://back-server;

        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "Upgrade";
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_read_timeout 21600000;
        proxy_send_timeout 21600000;
    }
 }
