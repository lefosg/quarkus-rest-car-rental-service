# minikube addons enable ingress
# istioctl install

# kubectl create namespace ingress-nginx
# kubectl create namespace argocd

# kubectl label namespace ingress-nginx istio-injection=enabled
# kubectl label namespace default istio-injection=enabled

kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v0.34.1/deploy/static/provider/cloud/deploy.yaml
# kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

kubectl apply -f rent-msvc.yaml
kubectl apply -f fleet-msvc.yaml
kubectl apply -f user_management-msvc.yaml 
kubectl apply -f hello-msvc-default.yaml
kubectl apply -f nginx-ingress.yaml
kubectl apply -f jaeger.yaml
