docker build -t containercloud .
docker rm containercloud
docker run -it -d -v containercloud:/home -v /var/run/docker.sock:/var/run/docker.sock -v /home/user/.docker:/home/user/.docker --name containercloud containercloud