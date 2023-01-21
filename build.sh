#  #!/usr/bin/sh

mkdir --mode=777 -p files
docker build . -t neo4jtp
docker compose up
