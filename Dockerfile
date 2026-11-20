# Etapa 1: Compilar el proyecto y descargar el driver JDBC
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
RUN mvn dependency:copy -Dartifact=org.postgresql:postgresql:42.7.3 -DoutputDirectory=/app/target

# Etapa 2: Configurar WildFly
FROM quay.io/wildfly/wildfly:31.0.1.Final-jdk17

# Copiar el driver JDBC y tu aplicación directamente a deployments/
COPY --from=build /app/target/postgresql-42.7.3.jar /opt/jboss/wildfly/standalone/deployments/
COPY --from=build /app/target/*.war /opt/jboss/wildfly/standalone/deployments/ROOT.war

# Registrar el Datasource apuntando al .jar copiado
COPY configurar-wildfly.cli /tmp/configurar-wildfly.cli
RUN /opt/jboss/wildfly/bin/jboss-cli.sh --file=/tmp/configurar-wildfly.cli && \
    rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history/current/*

EXPOSE 8080

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
