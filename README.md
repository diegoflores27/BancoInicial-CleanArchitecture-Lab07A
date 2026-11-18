# BancoInicial — Laboratorio 07-A individual

**Curso:** Construcción y Evolución de Software (IS-388)  
**Tema:** Arquitectura Limpia (Clean Architecture)  
**Modalidad:** Desarrollo personal / individual.  
**Base:** `codigo_inicial/SistemaBancario.java`, copia del `script01.txt` entregado.

## Objetivo
Refactorizar la implementación bancaria monolítica, preservando las operaciones de registrar, depositar, retirar, consultar saldo y salir. El sistema solo admite **una cuenta activa en memoria por ejecución**.

## Estructura
```
src/main/java/pe/edu/unsch/banco/
├── dominio/CuentaBancaria.java
├── aplicacion/MovimientoRepository.java
├── aplicacion/CuentaService.java
├── infraestructura/ArchivoMovimientoRepository.java
└── Main.java
src/test/java/pe/edu/unsch/banco/PruebasBanco.java
```

## Ejecución en IntelliJ IDEA
1. Abrir la carpeta que contiene `pom.xml` como proyecto Maven.
2. Seleccionar JDK 17 o superior.
3. Ejecutar `pe.edu.unsch.banco.Main` (botón ▶ en IntelliJ).
4. Para probar los siete escenarios sin JUnit, ejecutar `pe.edu.unsch.banco.PruebasBanco` desde el IDE.

### Terminal (con Maven instalado)
```
mvn clean test
java -cp target/classes pe.edu.unsch.banco.Main
java -cp "target/classes;target/test-classes" pe.edu.unsch.banco.PruebasBanco
```
En Linux/macOS se utiliza `:` en vez de `;` para separar los elementos de la ruta de clases.

> **Precisión sobre Maven:** `mvn test` compila el proyecto, pero `PruebasBanco` es un ejecutable de pruebas independiente sin JUnit. Para ejecutar los siete casos se debe abrir esa clase en IntelliJ o ejecutar el último comando. No afirmar que `mvn test` ejecutó siete pruebas JUnit.

## Reglas del sistema
- Titular obligatorio, saldo inicial no negativo.
- Importes con **BigDecimal** (máximo dos decimales) para evitar errores de representación binaria de `double` en montos monetarios.
- Depósitos/retiros mayores que cero; retiro limitado al saldo disponible.
- Intentar crear una segunda cuenta en la misma sesión **no reemplaza** la anterior.
- `movimientos.txt` se genera en la carpeta de trabajo y **solo registra el historial**. Al reiniciar el programa la cuenta **no se restaura**.

## Dirección de dependencias
- `dominio` no importa aplicación ni infraestructura.
- `aplicacion` depende del dominio y del **contrato** `MovimientoRepository`, no de archivos concretos.
- `infraestructura` implementa ese contrato para un archivo de texto.
- `Main` actúa como adaptador de entrada y punto de ensamblaje; crea `ArchivoMovimientoRepository` y lo inyecta en `CuentaService`.

## Entregables
- `DIAGNOSTICO.md`: responsabilidades mezcladas y justificación de la separación.
- `PRUEBAS.md`: casos P01–P07 y forma de comprobar resultados.
- `SUSTENTACION.md`: respuestas, dificultades y conclusiones.
- `EVIDENCIAS.md`: guía de capturas con comentarios.

