kubectl expose service prometheus-server --type=NodePort --target-port=9090 --name=prometheus-server-np
