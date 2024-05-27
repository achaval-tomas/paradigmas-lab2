---
title: Laboratorio de Programación Orientada a Objetos
author: Tomás Achaval, Tomás Maraschio, Tomás Peyronel
---

El enunciado del laboratorio se encuentra en [este link](https://docs.google.com/document/d/1wLhuEOjhdLwgZ4rlW0AftgKD4QIPPx37Dzs--P1gIU4/edit#heading=h.xe9t6iq9fo58).

# 1. Tareas

## Verificación de que pueden hacer las cosas.
- [x] Java 17 instalado. Deben poder compilar con `make` y correr con `make run` para obtener el mensaje de ayuda del programa.

## 1.1. Interfaz de usuario
- [x] Estructurar opciones
- [x] Construir el objeto de clase `Config`

## 1.2. FeedParser
- [x] `class Article`
    - [x] Atributos
    - [x] Constructor
    - [x] Método `print`
    - [x] _Accessors_
- [x] `parseXML`

## 1.3. Entidades nombradas
- [x] Pensar estructura y validarla con el docente
- [x] Implementarla
- [x] Extracción
    - [x] Implementación de heurísticas
- [x] Clasificación
    - [x] Por tópicos
    - [x] Por categorías
- Estadísticas
    - [x] Por tópicos
    - [x] Por categorías
    - [x] Impresión de estadísticas

## 1.4 Limpieza de código
- [x] Pasar un formateador de código
- [x] Revisar TODOs

# 2. Experiencia
Este lab nos gustó bastante en general. Java está bueno y tiene muchas features útiles para proyectos de este tipo. El uso de herencia, interfaces y records redujo mucho la cantidad de código que se tendría que haber escrito si no existieran estas herramientas.

# 3. Preguntas
1. Explicar brevemente la estructura de datos elegida para las entidades nombradas:<br>
Utilizamos una clase base NamedEntity de la cual heredan todas las entidades nombradas, cada una agregando sus propios atributos.
2. Explicar brevemente cómo se implementaron las heurísticas de extracción:<br>
Las heuristicas se implementaron mediante una interfaz con un método para extraer candidatos, y otros para identificarlas (nombre corto, nombre largo, y descripción).
Al implementar una nueva heurística hay que asegurarse de que no se repita el nombre corto o el nombre largo de otra heurística.
<br>
Las heurísticas se crean al principio del programa, y se guardan todas en un arreglo. 
Este arreglo se utiliza para obtener la elección del usuario, y también para imprimir la lista de heurísticas si se especifica --help.

# 4. Extras
No completamos puntos extra para este laboratorio.