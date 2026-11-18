# Pruebas de comportamiento — Actividad C

## Matriz de pruebas

En el entorno de preparación se compilaron las clases con Java 17 y se ejecutó `PruebasBanco` con resultado **7/7 OK** (véase `RESULTADOS_VERIFICACION.txt`). La matriz siguiente conserva una columna para que registres la **salida observada en tu propio IntelliJ** y adjuntes capturas personales. Los estados figuran pendientes hasta esa reproducción local.

| Caso | Entrada / acción | Resultado esperado | Salida observada en mi IntelliJ | Estado |
|---|---|---|---|---|
| P01 | Registrar `Ana`, saldo S/ 1000 | Cuenta activa, saldo `1000.00` | Por registrar | Pendiente |
| P02 | Depositar S/ 200 | Saldo `1200.00`; nuevo movimiento | Por registrar | Pendiente |
| P03 | Retirar S/ 150 | Saldo `1050.00`; nuevo movimiento | Por registrar | Pendiente |
| P04 | Retirar S/ 2000 | Rechazo por fondos insuficientes; saldo `1050.00` | Por registrar | Pendiente |
| P05 | Depositar S/ -10 | Rechazo por monto negativo; saldo intacto | Por registrar | Pendiente |
| P06 | Registrar `Luis` sin reiniciar | Rechaza el registro; titular sigue siendo `Ana` | Por registrar | Pendiente |
| P07 | Cerrar y abrir otra sesión; consultar saldo | Aviso de no recuperación y cuenta no disponible | Por registrar | Pendiente |

## Secuencia manual para P01–P06
1. Opción 1 → `Ana`, `1000`.
2. Opción 2 → `200`.
3. Opción 3 → `150`.
4. Opción 3 → `2000` (debe rechazarse).
5. Opción 2 → `-10` (debe rechazarse).
6. Opción 1 → `Luis`, `500` (debe rechazarse, Ana permanece).
7. Opción 4 → confirmar `Ana` y `1050.00`.
8. Opción 5 → salir.

## Secuencia manual para P07
1. Ejecutar de nuevo `Main` desde IntelliJ **sin borrar `movimientos.txt`**.
2. Observar el aviso de que el historial no restaura cuentas.
3. Opción 4 → aparece `No hay una cuenta registrada en esta sesión.`
4. Opción 5 → salir.

## Verificación de historial
Después de P01–P03, abrir `movimientos.txt`. Debe registrar **tres líneas**: alta, depósito, retiro, con titular, montos, saldo y marca temporal. Los casos rechazados P04, P05, P06 **no deben generar registros nuevos**. No confundir el historial con persistencia de cuentas.

## Pruebas sin JUnit
Para ejecutar la clase de pruebas proporcionada: `PruebasBanco.main()`. Si Maven muestra `No tests to run`, esto **no es un fallo de compilación**; significa que no hay tests JUnit/Surefire disponibles. La consola de `PruebasBanco` es la evidencia independiente de los siete casos.
