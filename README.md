# Microservice Architecture Домашние задания

## 1. Основы работы с Docker
### Собрать проект
`mvn package`
### Собрать docker образ
`docker image build --platform linux/amd64 -t otus-architecture:latest .`
### Запустить контейнер
`docker run -p 8000:8080 otus-architecture:latest`