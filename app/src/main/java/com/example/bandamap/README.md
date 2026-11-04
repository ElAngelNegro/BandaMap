# BandaMap

Aplicación Android que reúne en un solo lugar a las bandas locales y nacionales de Chile, sus tocatas y sus redes, y permite armar nuevas fechas mediante **convocatorias**.

Proyecto semestral de **Programación de Aplicaciones Móviles** · INACAP Valparaíso · Hito 1 (noviembre de 2026).

## El problema

Las bandas independientes tienen dificultades para difundir su material de forma constante. La información sobre sus presentaciones y sus redes queda dispersa entre Instagram, Spotify, YouTube y otras plataformas. Por eso, el público se entera tarde de sus fechas o no sabe dónde escucharlas, y quienes organizan tocatas no tienen un canal claro para encontrar bandas.

## Usuarios

| Perfil | Qué necesita |
| --- | --- |
| Bandas y músicos | Difundir su música, sus redes y sus fechas, y conseguir escenarios |
| Organizadores de eventos | Encontrar bandas para armar sus tocatas |
| Público | Saber cuándo y dónde tocan las bandas, y dónde escucharlas |

## Funcionalidades

1. Registrar una banda con sus enlaces (Spotify, Instagram)
2. Listar y buscar bandas por nombre
3. Ver el detalle de una banda
4. Abrir sus redes y plataformas de música
5. Consultar las próximas tocatas
6. **Publicar una convocatoria** (funcionalidad clave)
7. **Postular a una convocatoria** con una banda (funcionalidad clave)
8. Validar los formularios: los campos obligatorios vacíos se marcan en rojo

## Pantallas

| Pantalla | Descripción |
| --- | --- |
| Inicio | Próximas tocatas y accesos principales |
| Bandas | Lista de bandas (`LazyColumn` con `Card`) y buscador |
| Detalle de banda | Ficha, enlaces y tocatas; recibe el ID de la banda |
| Registrar banda | Formulario con validación y lista desplegable de géneros |
| Convocatorias | Lista de convocatorias con cupos y botón Postular |
| Nueva convocatoria | Formulario para publicar una convocatoria |

## Navegación

```
Inicio ─┬─► Bandas ──(envía bandaId)──► Detalle de banda
        ├─► Convocatorias ──(botón +)──► Nueva convocatoria
        └─► Registrar banda
```

- Se usa **Navigation Compose** con `NavHost` y `NavController`.
- El paso de parámetro se hace en la ruta `detalle/{bandaId}`.
- Todas las pantallas secundarias vuelven a la anterior con `popBackStack()`.

## Tecnologías

- Kotlin 2.2
- Jetpack Compose con Material 3
- Navigation Compose 2.9.8
- Android mínimo: API 24 (Android 7.0)

## Estructura del código

```
app/src/main/java/com/example/bandamap/
├── MainActivity.kt          → inicia el tema y la navegación
├── navigation/
│   ├── Rutas.kt             → nombres de las rutas
│   └── AppNavigation.kt     → NavController + NavHost
├── screens/                 → una pantalla por archivo
├── components/              → BarraSuperior, BandaCard, ConvocatoriaCard
└── model/                   → Banda, Tocata, Convocatoria y DatosPrueba
```

## Cómo ejecutar

1. Clonar el repositorio:
   ```
   git clone https://github.com/ElAngelNegro/BandaMap.git
   ```
2. Abrir la carpeta en **Android Studio** y esperar a que termine la sincronización de Gradle.
3. Elegir un emulador o un dispositivo Android y presionar **Run**.

## Alcance del Hito 1

Este hito se centra en las interfaces y la navegación. Los datos son de prueba y viven en memoria (`DatosPrueba`), por lo que se reinician al cerrar la app. Las bandas, tocatas y convocatorias que aparecen son ficticias.

## Próximos pasos

- **Hito 2:** ViewModel, edición y eliminación de datos, y bandas favoritas.
- **Hito 3:** persistencia de datos con Room o Firebase.
- **Hito 4:** GPS para ver tocatas cercanas y cámara para la imagen de la tocata.

## Equipo

- Sebastián Pasmiño
- José Miguel Astudillo Aguirre

Profesor: Mauricio Alberto Daza Rodenas
