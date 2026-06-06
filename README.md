# 🚀 Innovatech Solutions - Backend For Frontend (BFF) Orquestador

Este servicio actúa como el punto de entrada unificado y la capa de orquestación de la arquitectura de microservicios de **InnovaTech**. 

Su propósito principal es consumir, agregar y simplificar datos provenientes de múltiples microservicios independientes (**Recursos Humanos**, **Proyectos** y **Notificaciones**) y entregar respuestas optimizadas al cliente final (el Frontend) mediante llamadas no bloqueantes.

---

## 🏗️ Arquitectura de Software

El orquestador implementa patrones de integración avanzados y de resiliencia:

*   **Agregación de Datos Asíncrona (Orchestration Pattern):** Consume datos de diversos dominios de forma paralela utilizando **Spring WebFlux** (`WebClient` y `Mono.zip`), optimizando los tiempos de respuesta.
*   **Patrón de Fallback y Resiliencia (Circuit Breaker Lite):** Utiliza políticas de degradación gradual mediante `onErrorResume`. Si un microservicio externo no responde (ej. Recursos Humanos), el BFF entrega la información parcial disponible (ej. detalles del proyecto) sin interrumpir la experiencia de usuario, asignando valores por defecto como `"Sin asignar"`.
*   **Desacoplamiento de Contratos:** Aísla el frontend de los esquemas internos de las bases de datos de los microservicios mediante DTOs (Data Transfer Objects).

---

## 🛠️ Stack Tecnológico

*   **Lenguaje:** Java 21 (Eclipse Temurin JRE optimizado)
*   **Framework:** Spring Boot 3.x (WebFlux reactivo)
*   **Cliente HTTP:** Spring WebFlux (`WebClient`)
*   **Librerías:** Lombok
*   **Orquestación:** Docker Compose

---

## 🚀 Guía de Despliegue y Ejecución

### 📋 Prerrequisitos

*   Docker Desktop / Docker Engine
*   Para ejecutar localmente (fuera de Docker):
    *   Microservicio de **Recursos Humanos** operativo en `http://localhost:8081`
    *   Microservicio de **Proyectos** operativo en `http://localhost:8082`
    *   Microservicio de **Notificaciones** operativo en `http://localhost:8083`

### 🐳 Ejecución con Docker

El archivo `docker-compose.yml` en este directorio incluye contenedores de verificación (`check-rrhh`, `check-proyectos` y `check-notifications`) que esperan a que los respectivos microservicios estén activos en el host antes de levantar el BFF.

1. Asegúrese de que los 3 microservicios backend estén levantados y expuestos en el host en sus respectivos puertos (`8081`, `8082`, `8083`).
2. En la raíz del directorio `fs3_bff`, ejecute:
   ```bash
   docker compose up -d --build
   ```
3. El BFF estará expuesto en el puerto **`8080`**.

### 💻 Ejecución Local (Desarrollo)

Para ejecutar el servicio localmente sin Docker:
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

---

## 🧪 Ejecución de Pruebas Unitarias

El proyecto cuenta con pruebas unitarias que cubren el servicio de orquestación y los controladores, con cobertura Jacoco mantenida entre **60% y 75%**.

Para ejecutar los tests unitarios:

```bash
# En Windows (CMD o PowerShell)
.\mvnw.cmd test

# En Linux o macOS
./mvnw test
```

Para generar el reporte de cobertura de Jacoco:
```bash
# Windows
.\mvnw.cmd clean verify

# Linux / macOS
./mvnw clean verify
```
El reporte se generará en: `target/site/jacoco/index.html`.

---

## 🔌 Documentación del API (Puerto 8080)

El BFF expone los siguientes endpoints unificados:

### 1. Dashboard
| Método | Endpoint | Parámetros de Consulta | Descripción | Código de Éxito |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/dashboard/resumen` | `proyectoId` (Long), `recursoId` (Long) | Retorna un resumen unificado cruzando datos del proyecto y del empleado responsable. | `200 OK` |

**Ejemplo de respuesta (`GET /api/dashboard/resumen?proyectoId=1&recursoId=3`):**
```json
{
  "nombreProyecto": "Migración AWS",
  "estadoProyecto": "ACTIVO",
  "totalHoras": 34,
  "nombreResponsable": "Carlos Gómez",
  "cargoResponsable": "DEVELOPER_LEAD"
}
```

### 2. Empleados
| Método | Endpoint | Payload (Request Body) | Descripción | Código de Éxito |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/empleados` | Ninguno | Obtiene la lista de todos los empleados con su capacidad calculada. | `200 OK` |
| `GET` | `/api/empleados/{id}` | Ninguno | Obtiene los detalles de un empleado por ID. | `200 OK` |
| `GET` | `/api/empleados/{id}/capacity` | Ninguno | Obtiene la capacidad horaria disponible de un empleado. | `200 OK` |
| `POST` | `/api/empleados` | `EmpleadoDTO` (JSON) | Registra un nuevo empleado. | `201 Created` |
| `PUT` | `/api/empleados/{id}` | `EmpleadoDTO` (JSON) | Actualiza la información de un empleado. | `200 OK` |
| `DELETE` | `/api/empleados/{id}` | Ninguno | Elimina un empleado por ID. | `204 No Content` |

### 3. Proyectos
| Método | Endpoint | Payload (Request Body) | Descripción | Código de Éxito |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/proyectos` | Ninguno | Obtiene la lista de todos los proyectos. | `200 OK` |
| `GET` | `/api/proyectos/{id}` | Ninguno | Obtiene el detalle de un proyecto por ID. | `200 OK` |
| `POST` | `/api/proyectos` | `ProyectoDTO` (JSON) | Crea un nuevo proyecto. | `201 Created` |
| `PUT` | `/api/proyectos/{id}` | `ProyectoDTO` (JSON) | Actualiza un proyecto existente. | `200 OK` |
| `DELETE` | `/api/proyectos/{id}` | Ninguno | Elimina un proyecto por ID. | `204 No Content` |

### 4. Asignaciones
| Método | Endpoint | Payload (Request Body) | Descripción | Código de Éxito |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/asignaciones/proyecto/{proyectoId}` | Ninguno | Obtiene las asignaciones de un proyecto específico. | `200 OK` |
| `POST` | `/api/asignaciones` | `AsignacionDTO` (JSON) | Crea una asignación de horas a un empleado en un proyecto. | `201 Created` |
| `PUT` | `/api/asignaciones/{id}` | `AsignacionDTO` (JSON) | Actualiza las horas asignadas de una asignación específica. | `200 OK` |
| `DELETE` | `/api/asignaciones/proyecto/{proyectoId}/empleado/{empleadoId}` | Ninguno | Elimina la asignación de un empleado en un proyecto específico. | `204 No Content` |

### 5. Notificaciones
| Método | Endpoint | Payload (Request Body) | Descripción | Código de Éxito |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/notificaciones` | Ninguno | Obtiene el listado completo de alertas (sobrecarga, baja asignación). | `200 OK` |
| `PUT` | `/api/notificaciones/{id}/leer` | Ninguno | Marca una notificación como leída. | `200 OK` |

---

© 2026 Innovatech Solutions - Ingeniería Civil Informática - Documentación Técnica (EV2)
