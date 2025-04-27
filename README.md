# Microservice Architecture Домашние задания

## 2. Основы работы с Docker
### Собрать проект
`mvn package`
### Собрать docker образ
`docker image build --platform linux/amd64 -t podomnina/otus-architecture:task1 .`
### Запустить контейнер
`docker run -p 8000:8080 podomnina/otus-architecture:task1`
### Запушить образ
`docker push podomnina/otus-architecture:task1`

## 3. Основы работы с Kubernetes
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

## 4. Работа с Helm-ом
### Запустить minikube
`minikube start --cpus=4 --memory=8192`
### Создать namespace
`kubectl create namespace otus`
### Установить чарт для PostgreSQL
`helm install otus-postgres ./charts/components/postgres --namespace otus -f ./charts/components/postgres/values.yaml`
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

## 5. Prometheus. Grafana
### Установить Prometheus
```
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update
helm install prometheus prometheus-community/prometheus --namespace monitoring --create-namespace
```
### Для доступа до Prometheus
`kubectl port-forward svc/prometheus-server 9090:80 -n monitoring`

### Установить Grafana
```
helm repo add grafana https://grafana.github.io/helm-charts
helm repo update
helm install grafana grafana/grafana -n monitoring
```
### Получить временный пароль
`kubectl get secret --namespace monitoring grafana -o jsonpath="{.data.admin-password}" | base64 --decode ; echo`
`WgFNuB3OJpM1heLa5sS2DmNKhozqVvs5KoFpLgpy`
### Для доступа до Grafana
`kubectl port-forward svc/grafana 3000:80 -n monitoring`
### Добавить сбор метрик с nginx
`helm upgrade --install nginx ingress-nginx/ingress-nginx --namespace otus -f ./charts/ingress/values.yaml`
`helm upgrade prometheus prometheus-community/prometheus --namespace monitoring -f ./charts/monitoring/prometheus-values.yaml`

## 6. Backend for frontends. Apigateway
### Собрать проект
`mvn package`
### Собрать и запушить docker образы приложений
```
docker image build --platform linux/amd64 -t podomnina/auth-service:7 .
docker image build --platform linux/amd64 -t podomnina/auth-gateway:7 .
docker push podomnina/auth-service:7
docker push podomnina/auth-gateway:7
```
### Обновить сервисы в minikube
```
helm upgrade -i auth-gateway ./charts/apps/auth-gateway --namespace otus --atomic -f ./charts/apps/auth-gateway/values.yaml
helm upgrade -i auth-service ./charts/apps/auth-service --namespace otus --atomic -f ./charts/apps/auth-service/values.yaml
```
### Установить nginx
```
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx/
helm repo update
helm install nginx ingress-nginx/ingress-nginx --namespace otus -f nginx-ingress.yaml
kubectl apply -f ./charts/ingress/ingress.yaml
helm upgrade -i nginx ingress-nginx/ingress-nginx --namespace otus -f ./charts/ingress/values.yaml
```
### Запустить postman коллекцию
`newman run ./postman/OTUS.postman_collection.json --folder "19. Backend for frontends"`



## ПРОЕКТНАЯ РАБОТА
### Собрать проект
`mvn package`
### Собрать и запушить docker образы приложений
```
docker image build --platform linux/amd64 -t podomnina/auth-gateway:10 ./ms/auth-gateway/auth-gateway
docker push podomnina/auth-gateway:10

docker image build --platform linux/amd64 -t podomnina/auth-service:10 ./ms/auth-service/auth-service
docker push podomnina/auth-service:10

docker image build --platform linux/amd64 -t podomnina/inventory-service:9 ./ms/inventory-service/inventory-service
docker push podomnina/inventory-service:9

docker image build --platform linux/amd64 -t podomnina/menu-service:9 ./ms/menu-service/menu-service
docker push podomnina/menu-service:9

docker image build --platform linux/amd64 -t podomnina/notification-service:9 ./ms/notification-service/notification-service
docker push podomnina/notification-service:9

docker image build --platform linux/amd64 -t podomnina/order-service:9 ./ms/order-service/order-service
docker push podomnina/order-service:9

docker image build --platform linux/amd64 -t podomnina/payment-service:9 ./ms/payment-service/payment-service
docker push podomnina/payment-service:9
```
### Установить ingress
```
helm install nginx ingress-nginx/ingress-nginx --namespace otus -f nginx-ingress.yaml
kubectl apply -f ./charts/ingress/ingress.yaml
helm upgrade --install nginx ingress-nginx/ingress-nginx --namespace otus -f ./charts/ingress/values.yaml
```
### Установить постгрес
```
helm install otus-postgres ./charts/components/postgres --namespace otus -f ./charts/components/postgres/values.yaml
```
### Установить Kafka
```
helm upgrade my-kafka oci://registry-1.docker.io/bitnamicharts/kafka -n otus --set service.port=9092 --set auth.enabled=false --set auth.clientProtocol=plaintext --set kafka.autoCreateTopicsEnable=true --set listeners.client.protocol=plaintext
```
### Установить Redis
```
kubectl apply -f ./charts/components/redis -n otus
```
### Установить приложения
```
helm upgrade -i auth-gateway ./charts/apps/auth-gateway --namespace otus --atomic -f ./charts/apps/auth-gateway/values.yaml
helm upgrade -i auth-service ./charts/apps/auth-service --namespace otus --atomic -f ./charts/apps/auth-service/values.yaml
helm upgrade -i inventory-service ./charts/apps/inventory-service --namespace otus --atomic -f ./charts/apps/inventory-service/values.yaml
helm upgrade -i menu-service ./charts/apps/menu-service --namespace otus --atomic -f ./charts/apps/menu-service/values.yaml
helm upgrade -i notification-service ./charts/apps/notification-service --namespace otus --atomic -f ./charts/apps/notification-service/values.yaml
helm upgrade -i order-service ./charts/apps/order-service --namespace otus --atomic -f ./charts/apps/order-service/values.yaml
helm upgrade -i payment-service ./charts/apps/payment-service --namespace otus --atomic -f ./charts/apps/payment-service/values.yaml
```
### Установить Prometheus, Grafana
```
helm install prometheus prometheus-community/prometheus --namespace monitoring --create-namespace
helm install grafana grafana/grafana -n monitoring
```
### Получить временный пароль
`kubectl get secret --namespace monitoring grafana -o jsonpath="{.data.admin-password}" | base64 --decode ; echo`
`o9Nb14D7f0p3RANaGHHuTD8MUjtP3VcL1BwytDWf`
### Добавить сбор метрик с nginx
`helm upgrade prometheus prometheus-community/prometheus --namespace monitoring -f ./charts/monitoring/prometheus-values.yaml`
### Добавить сбор метрик с Kafka
```
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts

helm upgrade --install kafka-exporter prometheus-community/prometheus-kafka-exporter --set service.labels."app\.kubernetes\.io/name"=kafka-exporter --namespace otus -f ./charts/monitoring/kafka-exporter-values.yaml
```

```
### Подключиться к сервисам в яндекс облаке
`kubectl port-forward service/otus-postgres-postgres 30000:5432 --namespace otus`
`kubectl port-forward svc/grafana 3000:80 -n monitoring`
`kubectl port-forward svc/prometheus-server 9090:80 -n monitoring`