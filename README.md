# Generador de instancias y visualizador gráfico de soluciones para problemas de scheduling

[![Build Status](https://travis-ci.com/M1RZ4/TFG.svg?branch=master)](https://travis-ci.com/M1RZ4/TFG)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/f2f0d0b009384c8aba7deacb39b7b541)](https://app.codacy.com/project/M1RZ4/TFG/dashboard)
[![codecov](https://codecov.io/gh/M1RZ4/TFG/branch/master/graph/badge.svg)](https://codecov.io/gh/M1RZ4/TFG)

-   **Autor**: Mirza Ojeda Veira
-   **Tutores**: Francisco Javier Gil Gala y Ramiro José Varela Arias

## Descripción de la propuesta

<p>En este trabajo se propone el desarrollo de una herramienta gráfica que permita generar instancias de problemas de scheduling, atendiendo a unos parámetros indicados por el usuario, resolverlas utilizando un algoritmo, y por último visualizar de forma gráfica tanto las instancias como sus soluciones. La implementación se hará mediante una aplicación de escritorio.

Como ejemplo de prueba, se utilizará el problema de secuenciamiento de una máquina con capacidad variable. Actualmente se dispone de un prototipo software para resolver este problema que fue presentado en:

[Francisco Gil, Carlos Mencía, María R. Sierra, Ramiro Varela. Genetic programming to evolve priority rules for on-line scheduling on single machine with variable capacity. MAEB 2018](https://unioviedo-my.sharepoint.com/:b:/g/personal/uo251443_uniovi_es/EdVNq2IlPDRGugjQPPGuPYABQoNfPyECAPdcDpgg1HPnWw?e=MaBwoW)

El alumno hará uso de este código de forma completamente transparente y lo integrará en la herramienta desarrollada en el trabajo.

Es recomendable conocer el lenguaje de programación Java, así como tener conocimientos de librerías gráficas.

Se propone el uso de las librerías [Swing]( https://docs.oracle.com/javase/7/docs/api/javax/swing/package-summary.html) y [JFreeChart](http://www.jfree.org/jfreechart/)

En cualquier caso, el alumno es libre de seleccionar las librerías que considere oportunas.</p>

## Instrucciones de uso

<p>Si se desea compilar y generar el JAR ejecutable para este proyecto deben seguirse los siguientes pasos:</p>

**_NOTA:_** Ya hay disponible una *release* [aquí](https://github.com/M1RZ4/TFG/releases) y, por tanto, no es necesario compilar el código fuente manualmente.

-   [Descargar](https://maven.apache.org/download.cgi) e [instalar](https://maven.apache.org/install.html) Apache Maven
-   Descargar o clonar este repositorio
-   Modificar el fichero pom.xml y eliminar las líneas `<scope>` y `<systemPath>` en:

```xml
<dependency>
  <groupId>com.gestor</groupId>
  <artifactId>GestorProblema1maquina</artifactId>
  <version>1.0</version>
  <scope>system</scope >
	<systemPath>${project.basedir}/lib/GestorProblema1maquina.jar</systemPath>
</dependency>
```

-   Situarse en el directorio raíz del repositorio y ejecutar `mvn package`

<p>Si se han seguido los pasos correctamente en la raíz del directorio target estará situado el fichero ejecutable TFG-1.0-RELEASE.jar generado.</p>

## Documentación

<p>Puede consultarse toda la documentación asociada al trabajo [aquí](https://drive.google.com/file/d/1G2W4DuKawl-8iy637abudfRErPA5HdwA/view?usp=sharing)</p>
