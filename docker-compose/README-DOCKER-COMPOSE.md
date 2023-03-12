# JHipster generated Docker-Compose configuration

## Usage

Launch all your infrastructure by running: `docker-compose up -d`.

## Configured Docker services

### Service registry and configuration server:

- [JHipster Registry](http://localhost:8761)

### Applications and dependencies:

- collections (microservice application)
- collections's mongodb database
- gateway (gateway application)
- gateway's postgresql database
- payment (microservice application)
- payment's mongodb database
- reconciliation (microservice application)
- reconciliation's mongodb database

### Additional Services:

- Kafka
- Zookeeper
- [Prometheus server](http://localhost:9090)
- [Prometheus Alertmanager](http://localhost:9093)
- [Grafana](http://localhost:3000)
