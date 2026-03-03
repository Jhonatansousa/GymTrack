# ------------------------------------------  
# FASE 1: BUILD (compila o projeto com Maven)  
# ------------------------------------------  
FROM maven:3.9.6-eclipse-temurin-21 AS build  
# Usa uma imagem oficial do Maven com o Java 21 (Eclipse Temurin é mantido pela Adoptium)  
  
WORKDIR /app  
# Define o diretório de trabalho dentro do container  
  
COPY . .  
# Copia todos os arquivos do seu projeto para dentro do container  
  
RUN mvn clean package -DskipTests  
# Compila o projeto e gera o .jar dentro da pasta /app/target  
# O "-DskipTests" pula os testes para acelerar o build no Render, o github actions já vai rodar os testes.  
# O ideal em projetos reais não é pular, pois funcionaria como uma "dupla verificação"  
  
# ------------------------------------------  
# FASE 2: RUNTIME (roda o app)  
# ------------------------------------------  
FROM eclipse-temurin:21-jdk-jammy  
# Imagem leve apenas com o Java 21 necessário para rodar o app  
  
WORKDIR /app  
# Define o diretório de trabalho na imagem final  
  
EXPOSE 8080  
# Informa que sua aplicação Spring Boot roda na porta 8080  
  
COPY --from=build /app/target/*.jar app.jar  
# Copia o .jar gerado na fase anterior para a imagem final  
  
ENTRYPOINT ["java", "-jar", "app.jar"]  
# Comando padrão para iniciar a aplicação quando o container subir