setup:
	@kind create cluster --config kubernetes/config/config.yaml
	 @kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml
	 @kubectl wait --namespace ingress-nginx \
	 	--for=condition=ready pod \
	 	--selector=app.kubernetes.io/component=controller \
	 	--timeout=270s

teardown:
	@kind delete clusters compose-example-cluster