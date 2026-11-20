# Etapa 1: Compilación del .war y descarga segura del driver JDBC con Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
RUN mvn dependency:copy -Dartifact=org.postgresql:postgresql:42.7.3 -DoutputDirectory=/app/target

# Etapa 2: Servidor WildFly en producción
FROM quay.io/wildfly/wildfly:31.0.1.Final-jdk17

# Copiar el driver descargado por Maven hacia /tmp/postgresql.jar
COPY --from=build /app/target/postgresql-42.7.3.jar /tmp/postgresql.jar

# Ejecutar el script CLI para configurar PostgreSQL y ControlDS
COPY configurar-wildfly.cli /tmp/configurar-wildfly.cli
RUN /opt/jboss/wildfly/bin/jboss-cli.sh --file=/tmp/configurar-wildfly.cli && \
    rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history/current/*

# Copiar el archivo .war compilado al directorio de despliegues de WildFly
COPY --from=build /app/target/SistemaElectoral.war /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
