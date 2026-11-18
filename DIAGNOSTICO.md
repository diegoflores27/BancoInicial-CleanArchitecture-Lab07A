| Responsabilidad mezclada en `SistemaBancario` | Evidencia en el código inicial | Cambio que obligaría a modificar la misma clase | Riesgo |
|---|---|---|---|
| Entrada de datos por consola | `Scanner`, `nextLine`, `nextDouble` | Cambiar de consola a interfaz web/GUI | Acoplamiento a la interfaz |
| Gestión de cuenta activa | Campos `titular` y `saldo` | Admitir múltiples cuentas o consultas por identificador | Registrar otra cuenta sobrescribe la anterior |
| Reglas de depósito | `monto > 0` y `saldo += monto` | Establecer límites, comisiones o validaciones | Reglas mezcladas con E/S |
| Reglas de retiro | `monto > 0 && monto <= saldo` | Limitar importes diarios o nuevas reglas | Difícil probar sin consola |
| Persistencia de movimientos | `FileWriter("movimientos.txt", true)` | Sustituir TXT por JDBC/H2 u otro almacenamiento | Cambios de infraestructura impactan negocio |
| Presentación de resultados | `System.out.println` en operaciones | Cambiar formato de avisos o API de salida | Lógica y textos acoplados |
| Navegación y coordinación | `main`, `switch(opcion)` | Incorporar una opción de menú | Clase con demasiadas responsabilidades |


## Defectos concretos detectados

1. **Segundo registro:** `registrarCuenta()` sustituye `titular` y `saldo` sin comprobar que ya existe una cuenta activa.

2. **Operaciones sin cuenta:** `depositar()` y `retirar()` pueden invocarse antes de registrar; no se distingue una cuenta válida de un estado sin titular.

3. **Monto como double:** Puede acumular errores de redondeo en dinero.

4. **Persistencia incompleta:** `movimientos.txt` almacena eventos, pero no permite reconstruir automáticamente una cuenta al abrir una nueva sesión.

5. **Cambios costosos:** Agregar una base de datos o una GUI obligaría a modificar la misma clase de negocio.

## Decisión de refactorización

Separar `CuentaBancaria` (dominio), `CuentaService` y `MovimientoRepository` (aplicación/puerto), `ArchivoMovimientoRepository` (infraestructura) y `Main` (consola y ensamblaje).

La regla de dependencias permite que una alternativa JDBC/H2 implemente el mismo puerto sin modificar el dominio ni los casos de uso.

Se conserva deliberadamente una sola cuenta en memoria y se rechaza el segundo registro. No se implementa recuperación al reiniciar porque el archivo `movimientos.txt` es únicamente un historial de operaciones.
