# 🚢 Batalla Naval (Battleship) - Console Game

> **Proyecto Final:** Programación Estructurada.
> **Fecha:** Diciembre 2025.

##  Descripción

**Batalla Naval** es una implementación en Java del clásico juego de estrategia por turnos. El proyecto fue desarrollado como evaluación final para la materia de Programación Estructurada, con el objetivo de demostrar el dominio sobre **arreglos bidimensionales (matrices)**, **lógica modular** y **persistencia de datos**.

El juego enfrenta al usuario contra una CPU en un tablero de 8x8, donde el objetivo es localizar y destruir la flota enemiga mediante coordenadas alfanuméricas.

## ⚙️ Características y Mecánicas

El sistema opera bajo las siguientes reglas y funcionalidades:

* **El Tablero:** Una matriz de $8 \times 8$ donde las filas son letras (A-H) y las columnas números (1-8).
* **Simbología en Consola:**
    * `~` : Agua (Desconocido).
    * `O` : Disparo fallido (Agua).
    * `X` : Barco Impactado (Acierto).
* **CPU:** La computadora dispara a coordenadas aleatorias, validando internamente no repetir disparos en posiciones previas.
* **Persistencia de Datos:**
    * **Configuración Dinámica:** El juego lee el archivo `barcos_config.txt` al inicio para definir cuántos barcos habrá en la partida sin necesidad de recompilar el código.
    * **Registro de Resultados:** Al finalizar, se escriben los resultados (Nombre, Disparos, Victoria/Derrota) en `battleship_scores.txt` usando `FileWriter` en modo *append*.

##  Reto Técnico: Conversión ASCII

Uno de los desafíos más interesantes de este proyecto fue traducir la entrada humana (ej. "C5") a índices numéricos que las matrices del programa pudieran entender.

Implementamos una solución basada en la manipulación de caracteres ASCII:

```java
// Ejemplo de la lógica implementada
char letra = 'C';
int fila = letra - 'A'; // Resultado: 2 (Índice para la matriz)
int col = '5' - '1';    // Resultado: 4


