# Sustentación individual — Laboratorio 07-A

## 1. ¿Qué problema se resolvió al separar `CuentaBancaria` de `Main`?
Se aisló el comportamiento financiero (validar y actualizar saldos) de la interacción por consola. La entidad puede probarse sin `Scanner`, impresión ni archivo; un cambio de interfaz afecta principalmente a `Main`, no al dominio.

## 2. ¿Por qué `CuentaService` recibe `MovimientoRepository` en su constructor?
Para aplicar inversión de dependencias: la aplicación depende de un **contrato**, no de `FileWriter`. Es posible sustituir el adaptador de almacenamiento sin cambiar el caso de uso; también permite pruebas con repositorios simulados.

## 3. ¿Qué archivos cambiarían si sustituimos `FileWriter` por JDBC/H2?
Se añadiría o reemplazaría la implementación de infraestructura (por ejemplo, `H2MovimientoRepository`) y se modificaría el punto de ensamblaje `Main` para inyectarla. `CuentaBancaria`, `CuentaService` y la interfaz `MovimientoRepository` deberían permanecer iguales si el contrato sirve para el nuevo almacenamiento.

## 4. ¿En qué capa se valida que un retiro no exceda el saldo?
En la entidad de **dominio** `CuentaBancaria`, dentro de `retirar`. El servicio coordina la operación, pero no duplica la regla del saldo.

## 5. ¿Qué diferencia hay entre registrar movimientos y persistir cuentas?
El historial contiene eventos de depósito y retiro, pero la cuenta no se reconstruye automáticamente cuando el programa se inicia de nuevo. Persistir cuentas implica almacenar y recuperar su identidad y estado; esta práctica no implementa esa capacidad.

## Dificultades técnicas identificadas y solución
- **Responsabilidades acopladas:** se redistribuyeron entre paquetes de dominio, aplicación, infraestructura y presentación.
- **Registro duplicado:** se añadió un control en `CuentaService` para no reemplazar la cuenta activa.
- **Errores en entradas y montos:** se reemplazó `double` por `BigDecimal`, se validaron importes y se controlaron entradas no numéricas.
- **Confusión entre historial y persistencia:** se muestra un aviso al iniciar y se documenta la limitación.
- **Rastreo de pruebas:** se separó el procedimiento de P01–P07 y se preparó un ejecutable de comprobación sin dependencias externas.

## Conclusión general
La refactorización del programa bancario permitió separar las reglas financieras de la interfaz por consola y de los detalles de almacenamiento. La clase `CuentaBancaria` mantiene la validez de los importes y saldos; `CuentaService` coordina los casos de uso mediante la interfaz `MovimientoRepository`, mientras que el adaptador de archivo conserva el historial. El sistema mantiene las cinco operaciones, rechaza registros duplicados y operaciones inválidas, y explicita que una nueva ejecución no restaura la cuenta. Esta organización facilita la comprensión, el mantenimiento y el reemplazo futuro de la persistencia sin modificar la lógica central.
