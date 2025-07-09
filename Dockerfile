# Usar imagen base de OpenJDK 21 compatible con múltiples arquitecturas
FROM openjdk:21-jdk-slim

# Información del mantenedor
LABEL maintainer="Josue Milian Zelada Cubillas <00053722>"
LABEL description="Sistema de Soporte Técnico con Spring Boot y JWT"
LABEL version="1.0.0"

# Crear usuario no-root para mayor seguridad
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Crear directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR de la aplicación
COPY target/parcial-final-n-capas-*.jar app.jar

# Cambiar propietario del directorio de trabajo
RUN chown -R appuser:appuser /app

# Cambiar al usuario no-root
USER appuser

# Exponer el puerto 8080
EXPOSE 8080

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=docker
ENV SERVER_PORT=8080

# Comando de entrada con configuraciones optimizadas para contenedores
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+ExitOnOutOfMemoryError", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]

# Healthcheck para verificar que la aplicación esté funcionando
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/auth/test || exit 1
