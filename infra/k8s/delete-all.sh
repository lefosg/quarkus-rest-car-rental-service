kubectl delete deployment fleet-deployment
kubectl delete deployment rent-deployment
kubectl delete deployment user-deployment
kubectl delete deployment hello-default-deployment

kubectl delete svc default-http-backend
kubectl delete svc fleet-service
kubectl delete svc rent-service
kubectl delete svc user-service

kubectl delete ingress car-rental-service-ingress

# kubectl delete namespace ingress-nginx