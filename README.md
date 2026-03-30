# MangaVault

Lector de manga personal con backend Java Spring Boot y frontend React PWA.

## Installation

### Requisitos
- Java 21+
- Maven 

```bash
cd backend
./mvnw spring-boot:run
```

---

## API Endpoints

### Mangas
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/mangas` | Listar todos (acepta `?search=` y `?genre=`) |
| GET | `/api/mangas/{id}` | Obtener manga por ID |
| POST | `/api/mangas` | Crear manga (multipart: `manga` JSON + `cover` imagen) |
| PUT | `/api/mangas/{id}` | Actualizar manga |
| DELETE | `/api/mangas/{id}` | Eliminar manga |
| GET | `/api/mangas/{id}/download` | Descargar manga completo como ZIP |

### Capítulos
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/mangas/{id}/chapters` | Listar capítulos de un manga |
| GET | `/api/chapters/{id}` | Obtener capítulo por ID |
| POST | `/api/mangas/{id}/chapters` | Subir capítulo (multipart: páginas como imágenes) |
| DELETE | `/api/chapters/{id}` | Eliminar capítulo |
| GET | `/api/chapters/{id}/download` | Descargar capítulo como ZIP |

### Listas de Lectura
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/lists` | Listar todas las listas |
| POST | `/api/lists` | Crear lista |
| PUT | `/api/lists/{id}` | Actualizar lista |
| DELETE | `/api/lists/{id}` | Eliminar lista |
| POST | `/api/lists/{listId}/mangas/{mangaId}` | Añadir manga a lista |
| DELETE | `/api/lists/{listId}/mangas/{mangaId}` | Quitar manga de lista |

### Imágenes
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/images/{path}` | Servir imagen por ruta relativa |

---

## Estructura del proyecto

```
backend/src/main/java/com/mangareader/
├── MangaReaderApplication.java
├── config/
│   ├── WebConfig.java
│   └── GlobalExceptionHandler.java
├── model/
│   ├── Manga.java
│   ├── Chapter.java
│   └── ReadingList.java
├── repository/
│   ├── MangaRepository.java
│   ├── ChapterRepository.java
│   └── ReadingListRepository.java
├── service/
│   ├── MangaService.java
│   ├── ChapterService.java
│   ├── ReadingListService.java
│   └── FileStorageService.java
└── controller/
    ├── MangaController.java
    ├── ChapterController.java
    ├── ReadingListController.java
    └── ImageController.java
```

