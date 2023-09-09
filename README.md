# ktor-rinha-backend-mongo

## Tecnologias utilizadas
```
ktor
kotlin
kmongo
mongodb
koin
flapdoodle mongo
```

## Projeto para atender aos requisitos da rinha de backend 2023
[INSTRUÇÕES DA RINHA](https://github.com/zanfranceschi/rinha-de-backend-2023-q3/blob/main/INSTRUCOES.md)

## Como rodar 
```
docker-compose up
```

## Exemplo de request apontando para o Load Balancer (NGINX)

```bash
curl --location 'http://localhost:9999/contagem-pessoas'
```

## Executar os testes
```
./gradlew test
```