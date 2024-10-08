kubectl delete deployment fleet-deployment
kubectl delete deployment rent-deployment
kubectl delete deployment user-deployment
kubectl delete deployment jaeger-deployment
kubectl delete deployment hello-default-deployment

kubectl delete svc default-http-backend
kubectl delete svc fleet-service
kubectl delete svc rent-service
kubectl delete svc user-service
kubectl delete svc jaeger-service

kubectl delete ingress car-rental-service-ingress

# kubectl delete namespace ingress-nginx
# kubectl delete namespace argocd
# kubectl label namespace ingress-nginx istio-injection-
# kubectl label namespace default istio-injection-
helm uninstall fleet-msvc
helm uninstall user-msvc
helm uninstall rent-msvc
helm uninstall hello-default-deployment