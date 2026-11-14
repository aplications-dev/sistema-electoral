# Etapa 1: Compilación del archivo .war con Maven y Java 17
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Servidor WildFly en producción
FROM quay.io/wildfly/wildfly:31.0.1.Final-jdk17

# Descargar el driver oficial de PostgreSQL
USER root
RUN curl -L https://postgresql.org -o /tmp/postgresql.jar
USER jboss

# Ejecutar el script CLI para configurar PostgreSQL y ElectoralDS
COPY configurar-wildfly.cli /tmp/configurar-wildfly.cli
RUN /opt/jboss/wildfly/bin/jboss-cli.sh --file=/tmp/configurar-wildfly.cli && \
    rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history/current/*

# Copiar el archivo .war compilado al directorio de despliegues de WildFly
COPY --from=build /app/target/SistemaElectoral.war /opt/jboss/wildfly/standalone/deployments/

# Exponer el puerto web de WildFly
EXPOSE 8080

# Iniciar WildFly escuchando en todas las interfaces de red
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
