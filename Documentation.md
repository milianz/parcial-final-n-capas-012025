# 🐳 Guía de Despliegue con Docker

## 📋 Prerrequisitos

### Software Requerido
```bash
# Verificar instalaciones
docker --version          # Docker 20.10+
docker-compose --version  # Docker Compose 2.0+
```

### Puertos Requeridos
- **8080**: Aplicación Spring Boot
- **8081**: Adminer (Gestión de BD)
- **5432**: PostgreSQL

---

## 🚀 Despliegue

### Opción 1: Script Automatizado (Recomendado)
```bash
# 1. Navegar al directorio del proyecto
cd parcial-final-n-capas-012025

# 2. Hacer el script ejecutable
chmod +x docker-deploy.sh

# 3. Ejecutar despliegue
./docker-deploy.sh
```

### Opción 2: Comandos Manuales
```bash
# 1. Construir la aplicación
./mvnw clean package -DskipTests

# 2. Levantar contenedores
docker-compose up --build -d

# 3. Verificar estado
docker-compose ps
```

---

## 🔧 Comandos de Gestión

### Monitoreo
```bash
# Ver estado de servicios
docker-compose ps

# Ver logs en tiempo real
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f app
docker-compose logs -f postgres
```

### Control de Servicios
```bash
# Reiniciar servicios
docker-compose restart

# Detener servicios
docker-compose down

# Detener y eliminar datos
docker-compose down -v --remove-orphans

# Reconstruir contenedores
docker-compose up --build -d
```

---

## 🌐 URLs de Acceso

| Servicio | URL | Descripción |
|----------|-----|-------------|
| **Aplicación** | http://localhost:8080 | API principal |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | Documentación API |
| **Adminer** | http://localhost:8081 | Gestión de BD |
| **Health Check** | http://localhost:8080/auth/test | Verificación |

### Credenciales de BD (Adminer)
- **Servidor**: postgres
- **Usuario**: postgres
- **Contraseña**: admin
- **Base de datos**: supportdb

---

## 🧪 Verificación del Despliegue

### 1. Verificar que los servicios están corriendo
```bash
docker-compose ps
```

**Salida esperada:**
```
     Name                   Command               State           Ports
------------------------------------------------------------------------
support-app      java -XX:+UseContainerSup ...   Up      0.0.0.0:8080->8080/tcp
support-db       docker-entrypoint.sh postgres   Up      0.0.0.0:5432->5432/tcp
support-adminer  entrypoint.sh docker-php- ...   Up      0.0.0.0:8081->8080/tcp
```

### 2. Probar conectividad
```bash
# Verificar que la aplicación responde
curl http://localhost:8080/auth/test

# Respuesta esperada: {"message": "¡La autenticación está funcionando!"}
```

### 3. Verificar logs
```bash
# Ver logs de la aplicación
docker-compose logs app | tail -20

# Buscar líneas de éxito
docker-compose logs app | grep "Started ParcialFinalNCapasApplication"
```

---

## 🐛 Resolución de Problemas

### Problema: Puerto ya en uso
```bash
# Verificar qué proceso usa el puerto
lsof -i :8080

# Detener servicios Docker existentes
docker-compose down
```

### Problema: Contenedor no inicia
```bash
# Ver logs detallados
docker-compose logs app

# Reconstruir sin caché
docker-compose build --no-cache
```

### Problema: Base de datos no conecta
```bash
# Verificar logs de PostgreSQL
docker-compose logs postgres

# Reiniciar solo la BD
docker-compose restart postgres
```

### Problema: Aplicación no responde
```bash
# Verificar estado de contenedores
docker-compose ps

# Ver logs en tiempo real
docker-compose logs -f app

# Reiniciar aplicación
docker-compose restart app
```

---

## 🔄 Comandos de Mantenimiento

### Limpieza del Sistema
```bash
# Limpiar contenedores parados
docker container prune -f

# Limpiar imágenes sin usar
docker image prune -f

# Limpiar todo el sistema Docker
docker system prune -f
```

### Backup de Datos
```bash
# Crear backup de la BD
docker exec support-db pg_dump -U postgres supportdb > backup.sql

# Restaurar backup
cat backup.sql | docker exec -i support-db psql -U postgres -d supportdb
```

### Actualizar la Aplicación
```bash
# 1. Detener servicios
docker-compose down

# 2. Reconstruir aplicación
./mvnw clean package -DskipTests

# 3. Reconstruir y levantar
docker-compose up --build -d
```

---

## 📊 Monitoreo

### Ver Recursos Utilizados
```bash
# Ver uso de recursos por contenedor
docker stats

# Ver información específica
docker exec support-app ps aux
docker exec support-db ps aux
```

### Verificar Variables de Entorno
```bash
# Ver variables de la aplicación
docker exec support-app env | grep SPRING

# Ver variables de la BD
docker exec support-db env | grep POSTGRES
```

---

## 🛑 Detener el Sistema

### Detener Servicios (Mantener Datos)
```bash
docker-compose down
```

### Detener y Eliminar Todo
```bash
# Eliminar contenedores, redes y volúmenes
docker-compose down -v --remove-orphans

# Limpiar imágenes relacionadas
docker rmi $(docker images | grep parcial-final-n-capas | awk '{print $3}')
```

**🎉 ¡Sistema Docker desplegado exitosamente!**