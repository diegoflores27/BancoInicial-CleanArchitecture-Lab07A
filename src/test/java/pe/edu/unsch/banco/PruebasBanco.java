package pe.edu.unsch.banco;

import pe.edu.unsch.banco.aplicacion.CuentaService;
import pe.edu.unsch.banco.infraestructura.ArchivoMovimientoRepository;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

/** Prueba reproducible sin dependencias externas; ejecutar como programa Java. */
public final class PruebasBanco {
    public static void main(String[] args) throws Exception {
        Path historial = Files.createTempFile("lab07a-movimientos-", ".txt");
        try {
            CuentaService servicio = new CuentaService(new ArchivoMovimientoRepository(historial));
            servicio.registrar("Ana", new BigDecimal("1000"));
            comprobar("P01 - Registro", servicio.consultarSaldo(), "1000.00");

            servicio.depositar(new BigDecimal("200"));
            comprobar("P02 - Depósito", servicio.consultarSaldo(), "1200.00");

            servicio.retirar(new BigDecimal("150"));
            comprobar("P03 - Retiro", servicio.consultarSaldo(), "1050.00");

            debeFallar(() -> servicio.retirar(new BigDecimal("2000")));
            comprobar("P04 - Fondos insuficientes", servicio.consultarSaldo(), "1050.00");

            debeFallar(() -> servicio.depositar(new BigDecimal("-10")));
            comprobar("P05 - Monto negativo", servicio.consultarSaldo(), "1050.00");

            debeFallar(() -> servicio.registrar("Luis", new BigDecimal("500")));
            comprobar("P06 - No reemplazar cuenta", servicio.consultarSaldo(), "1050.00");
            if (!"Ana".equals(servicio.consultarTitular())) throw new AssertionError("Titular cambió");

            // Un nuevo servicio equivale a un reinicio: la cuenta no se reconstruye del historial.
            CuentaService nuevaSesion = new CuentaService(new ArchivoMovimientoRepository(historial));
            debeFallar(nuevaSesion::consultarSaldo);
            if (!Files.exists(historial) || Files.readAllLines(historial).size() != 3) {
                throw new AssertionError("Se esperaban tres movimientos almacenados");
            }
            System.out.println("P07 - Reinicio sin recuperación de cuenta: OK");
            System.out.println("7/7 pruebas de comportamiento: OK");
        } finally {
            Files.deleteIfExists(historial);
        }
    }

    private static void comprobar(String nombre, BigDecimal actual, String esperado) {
        if (actual.compareTo(new BigDecimal(esperado)) != 0) {
            throw new AssertionError(nombre + ": esperado " + esperado + ", recibido " + actual);
        }
        System.out.println(nombre + ": OK (saldo S/ " + actual + ")");
    }

    private static void debeFallar(Runnable accion) {
        try {
            accion.run();
            throw new AssertionError("Se esperaba una operación rechazada");
        } catch (IllegalArgumentException | IllegalStateException esperado) {
            // Rechazo válido: no se modifica el saldo.
        }
    }
}
