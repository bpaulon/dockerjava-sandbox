# dockerjava-sandbox
docker java examples

Examples on how to build images and run Docker containers using Java and dockerjava library

## build
maven install creates a runnable jar

## run
The app automatically connects to tcp://localhost:1275. Edit the App.java file to match the remote docker host. The daemon must be configured for remote connections

see https://docs.docker.com/engine/install/linux-postinstall/#configuring-remote-access-with-daemonjson
