#!/bin/bash

# Script para construir y desplegar la aplicación con Docker
# Compatible con arquitecturas AMD64 y ARM64

set -e

echo "🚀 Iniciando construcción y despliegue del Sistema de Soporte Técnico"

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para imprimir mensajes coloridos
print_message() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Verificar que Docker está instalado
if ! command -v docker &> /dev/null; then
    print_error "Docker no está instalado. Por favor instalar Docker primero."
    exit 1
fi

# Verificar que Docker Compose está instalado
if ! command -v docker-compose &> /dev/null; then
    print_error "Docker Compose no está instalado. Por favor instalar Docker Compose primero."
    exit 1
fi

# Limpiar contenedores y volúmenes anteriores (opcional)
read -p "¿Deseas limpiar contenedores anteriores? (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    print_warning "Limpiando contenedores anteriores..."
    docker-compose down -v --remove-orphans || true
    docker system prune -f || true
fi

# Construir la aplicación con Maven
print_message "📦 Construyendo aplicación con Maven..."
if ./mvnw clean package -DskipTests; then
    print_message "✅ Aplicación construida exitosamente"
else
    print_error "❌ Error al construir la aplicación"
    exit 1
fi

# Verificar que el JAR fue creado
if [ ! -f target/parcial-final-n-capas-*.jar ]; then
    print_error "❌ No se encontró el archivo JAR en target/"
    exit 1
fi

# Construir y levantar los contenedores
print_message "🐳 Construyendo y levantando contenedores Docker..."
if docker-compose up --build -d; then
    print_message "✅ Contenedores levantados exitosamente"
else
    print_error "❌ Error al levantar los contenedores"
    exit 1
fi

# Esperar a que los servicios estén listos
print_message "⏳ Esperando a que los servicios estén listos..."
sleep 30

# Verificar estado de los servicios
print_message "🔍 Verificando estado de los servicios..."
docker-compose ps

# Probar la conectividad
print_message "🧪 Probando conectividad de la aplicación..."
for i in {1..10}; do
    if curl -f http://localhost:8080/auth/test > /dev/null 2>&1; then
        print_message "✅ Aplicación respondiendo correctamente en http://localhost:8080"
        break
    fi
    if [ $i -eq 10 ]; then
        print_error "❌ La aplicación no responde después de 10 intentos"
        docker-compose logs app
        exit 1
    fi
    print_warning "Intento $i/10 - Esperando respuesta de la aplicación..."
    sleep 5
done

# Mostrar información de los servicios
echo
print_message "🎉 ¡Despliegue completado exitosamente!"
echo
echo -e "${BLUE}📋 Información de servicios:${NC}"
echo "🌐 Aplicación: http://localhost:8080"
echo "📊 Swagger UI: http://localhost:8080/swagger-ui.html"
echo "🗄️  Adminer (DB): http://localhost:8081"
echo "   - Servidor: postgres"
echo "   - Usuario: postgres"
echo "   - Contraseña: admin"
echo "   - Base de datos: supportdb"
echo
echo -e "${BLUE}👥 Usuarios de prueba creados automáticamente:${NC}"
echo "🔧 TECH: tech@test.com / password123"
echo "👤 USER: user@test.com / password123"
echo "👑 ADMIN: admin@test.com / admin123"
echo
echo -e "${BLUE}📝 Comandos útiles:${NC}"
echo "🔍 Ver logs: docker-compose logs -f"
echo "⏹️  Detener: docker-compose down"
echo "🔄 Reiniciar: docker-compose restart"
echo "🧹 Limpiar todo: docker-compose down -v --remove-orphans"

print_message "🚀 Sistema listo para usar!"
