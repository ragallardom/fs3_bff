# 🚀 Innovatech Solutions - BFF (Backend For Frontend) Orquestador

Este servicio actúa como el punto de entrada unificado y capa de orquestación para el ecosistema de Innovatech Solutions.  
Su propósito principal es agregar, transformar y simplificar datos provenientes de múltiples microservicios (**RRHH** y **Proyectos**), entregando una respuesta optimizada al cliente final mediante llamadas no bloqueantes.

---

## 🏗️ Arquitectura de Software

El orquestador implementa patrones avanzados de integración para garantizar la alta disponibilidad y eficiencia:

### Agregación de Datos Asíncrona (Orchestration Pattern)

A diferencia de un gateway tradicional, este BFF consume datos de diversos dominios de forma paralela utilizando **Project Reactor** y `Mono.zip`.  
Esto reduce el tiempo total de respuesta al del microservicio más lento, cumpliendo con estándares de alto rendimiento.

### Patrón de Fallback y Resiliencia (Circuit Breaker Lite)

El servicio implementa una política de degradación gradual mediante `onErrorResume`.  
Si un microservicio backend (como RRHH) se encuentra fuera de servicio, el BFF puede entregar información parcial disponible (ej: datos del proyecto) sin interrumpir la experiencia del usuario, marcando los datos faltantes como **"Sin asignar"**.

### Desacoplamiento de Contratos

Mediante el uso de **DTOs (Data Transfer Objects)**, el BFF actúa como una barrera de protección que aísla al frontend de cambios en los esquemas internos de los microservicios.

---

## 🛠️ Stack Tecnológico

- **Backend:** Java 21 (Eclipse Temurin JRE optimizado para Alpine Linux)
- **Framework:** Spring Boot 3.x con enfoque reactivo
- **Cliente HTTP:** Spring WebFlux (`WebClient`) para consumo no bloqueante
- **Librerías:** Lombok para reducción de código repetitivo y construcción de objetos inmutables
- **Orquestación:** Docker Compose para interconectividad de red entre servicios

---

## 🚀 Guía de Despliegue y Ejecución

### 📋 Prerrequisitos

- Docker Desktop / Docker Engine
- Microservicio de **RRHH** operativo en el puerto **8081**
- Microservicio de **Proyectos** operativo en el puerto **8082**

---

### ⚡ Arranque del Entorno

El despliegue del BFF incluye verificación de dependencias para asegurar que los microservicios estén disponibles antes de iniciar:

```bash
docker compose up -d --build
```

## 🧪 Documentación del API (Puerto 8080)

### Dashboard - Resumen Unificado de Proyecto

Consulta simultáneamente la carga horaria calculada (MS Proyectos) y la identidad del responsable (MS RRHH).

- **Endpoint:** `GET /api/dashboard/resumen`

### Parámetros

- `proyectoId`: Identificador único del proyecto
- `recursoId`: Identificador del empleado responsable

### Ejemplo de Solicitud

```http
GET http://localhost:8080/api/dashboard/resumen?proyectoId=1&recursoId=120
``` 

---

### Ejemplo de Respuesta

```json
{
  "nombreProyecto": "Migración AWS",
  "estadoProyecto": "ACTIVO",
  "totalHoras": 34,
  "nombreResponsable": "Rodrigo",
  "cargoResponsable": "DEVELOPER_LEAD"
}
```
## 🛡️ Estándares de Seguridad y Gobernanza

- **Aislamiento de Infraestructura:** Uso de `host.docker.internal` para comunicación entre servicios, permitiendo flexibilidad en despliegue
- **Defensa en Profundidad:** Ejecución bajo usuario `spring` (`no-root`), reduciendo superficie de ataque
- **Validación de Integridad:** Coherencia de datos garantizada mediante lógica de negocio antes de la serialización JSON

---

© 2026 Innovatech Solutions - Ingeniería Civil Informática - Documentación Técnica (EV2)
