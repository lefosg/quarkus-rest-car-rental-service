# minikube addons enable ingress
istioctl install

kubectl create namespace ingress-nginx
# kubectl create namespace argocd

kubectl label namespace default istio-injection=enabled

# helm upgrade --install ingress-nginx ingress-nginx \
#   --repo https://kubernetes.github.io/ingress-nginx \
#   --namespace ingress-nginx --create-namespace
# helm search repo ingress-nginx --versions

# CHART_VERSION="4.11.2"  # change this accordingly
# APP_VERSION="1.11.2"  # change this accordingly

# helm template ingress-nginx ingress-nginx \
# --repo https://kubernetes.github.io/ingress-nginx \
# --version ${CHART_VERSION} \
# --namespace ingress-nginginx-ingress.1.11.2.yamlnginx-ingress.yaml

kubectl -n ingress-nginx get deploy ingress-nginx-controller  -o yaml | istioctl kube-inject -f - | kubectl apply -f - # deploy a sidecar proxy for nginx ingress controller specifically

# kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

helm install rent-msvc microservice --values=microservice/values-rent.yaml
helm install user-msvc microservice --values=microservice/values-user.yaml
helm install fleet-msvc microservice --values=microservice/values-fleet.yaml 
helm install hello-default-deployment microservice --values=microservice/values.yaml

kubectl apply -n istio-system -f prometheus.yaml
kubectl apply -n istio-system -f grafana.yaml
kubectl apply -f kiali.yaml

kubectl -n istio-system port-forward svc/grafana 3000
kubectl -n istio-system port-forward svc/kiali 20001