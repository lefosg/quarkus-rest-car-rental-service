# myk8scluster.ddns.net # the sample hostname
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
# --namespace ingress-nginx nginx-ingress-controller.1.11.2.yaml

# open policy agent
kubectl create clusterrolebinding cluster-admin-binding \
  --clusterrole cluster-admin \
  --user lefos

helm repo add gatekeeper https://open-policy-agent.github.io/gatekeeper/charts
helm install gatekeeper/gatekeeper --name-template=gatekeeper --namespace gatekeeper-system --create-namespace

kubectl apply -f nginx/manifests/nginx-ingress-controller.1.11.2.yaml
kubectl apply -f nginx/nginx-ingress-routes.yaml

helm repo add jetstack https://charts.jetstack.io --force-update  # cert-manager
helm install \
  cert-manager jetstack/cert-manager \
  --namespace cert-manager \
  --create-namespace \
  --version v1.16.0 \
  --set crds.enabled=true
  
kubectl -n ingress-nginx get deploy ingress-nginx-controller  -o yaml | istioctl kube-inject -f - | kubectl apply -f - # deploy a sidecar proxy for nginx ingress controller specifically

# kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

cd helm/microservice
helm install rent-msvc microservice --values=microservice/values-rent.yaml
helm install user-msvc microservice --values=microservice/values-user.yaml
helm install fleet-msvc microservice --values=microservice/values-fleet.yaml 
helm install hello-default-deployment microservice --values=microservice/values.yaml

cd ../../
kubectl apply -n istio-system -f metrics/prometheus.yaml
kubectl apply -n istio-system -f metrics/grafana.yaml
kubectl apply -f metrics/kiali.yaml

kubectl -n istio-system port-forward svc/grafana 3000
kubectl -n istio-system port-forward svc/kiali 20001