# Microservice Architecture Домашние задания

## 1. Основы работы с Docker
### Собрать проект
`mvn package`
### Собрать docker образ
`docker image build --platform linux/amd64 -t podomnina/otus-architecture:task1 .`
### Запустить контейнер
`docker run -p 8000:8080 podomnina/otus-architecture:task1`
### Запушить образ
`docker push podomnina/otus-architecture:task1`

## 2. Основы работы с Kubernetes
### Запустить minikube
`minikube start --cpus=4 --memory=8192`
### Создать namespace
`kubectl create namespace otus`
### Установить ingress-nginx
```
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx/
helm repo update
helm install nginx ingress-nginx/ingress-nginx --namespace otus -f nginx-ingress.yaml
```
`minikube tunnel`
### Создать/обновить ресурсы в /devops
`kubectl apply -f ./devops`
### Вызвать запрос
```
curl --location 'http://127.0.0.1:80/health' \
--header 'Host: arch.homework'
```

## 3. Работа с Helm-ом
### Запустить minikube
`minikube start --cpus=4 --memory=8192`
### Создать namespace
`kubectl create namespace otus`
### Установить чарт для PostgreSQL
`helm install otus-postgres ./charts/postgres --namespace otus -f ./charts/postgres/values.yaml`
### Установить чарт для auth-service
`helm install auth-service ./charts/apps/auth-service --namespace otus -f ./charts/apps/auth-service/values.yaml`
### Установить ingress-nginx
```
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx/
helm repo update
helm install nginx ingress-nginx/ingress-nginx --namespace otus -f nginx-ingress.yaml
cd charts
kubectl apply -f ingress.yaml
```
`minikube tunnel`

### Для доступа до БД по localhost
`kubectl port-forward svc/otus-postgres-postgres 5432:5432`