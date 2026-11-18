package pe.edu.unsch.banco;

import pe.edu.unsch.banco.aplicacion.CuentaService;
import pe.edu.unsch.banco.aplicacion.MovimientoRepository;
import pe.edu.unsch.banco.infraestructura.ArchivoMovimientoRepository;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.Scanner;

/** Capa de presentación: aquí se muestran los mensajes, sin reglas bancarias. */
public final class Main {
    private Main() { }

    public static void main(String[] args) {
        MovimientoRepository repositorio =
                new ArchivoMovimientoRepository(Paths.get("movimientos.txt"));
        CuentaService servicio = new CuentaService(repositorio);

        System.out.println("AVISO: movimientos.txt guarda el historial de operaciones.");
        System.out.println("La cuenta no se restaura automáticamente al reiniciar.");

        try (Scanner entrada = new Scanner(System.in)) {
            boolean activo = true;
            while (activo) {
                mostrarMenu();
                String opcion = entrada.nextLine().trim();
                try {
                    switch (opcion) {
                        case "1" -> {
                            System.out.print("Ingrese titular: ");
                            String titular = entrada.nextLine();
                            BigDecimal saldoInicial = leerMonto(entrada, "Ingrese saldo inicial: ");
                            servicio.registrar(titular, saldoInicial);
                            System.out.println("Cuenta registrada, saldo " + formato(servicio.consultarSaldo()));
                        }
                        case "2" -> {
                            BigDecimal monto = leerMonto(entrada, "Monto a depositar: ");
                            servicio.depositar(monto);
                            System.out.println("Depósito exitoso, saldo " + formato(servicio.consultarSaldo())
                                    + ". Movimiento registrado");
                        }
                        case "3" -> {
                            BigDecimal monto = leerMonto(entrada, "Monto a retirar: ");
                            servicio.retirar(monto);
                            System.out.println("Retiro exitoso, saldo " + formato(servicio.consultarSaldo())
                                    + ". Movimiento registrado");
                        }
                        case "4" -> {
                            System.out.println("Titular: " + servicio.consultarTitular());
                            System.out.println("Saldo actual: S/ " + formato(servicio.consultarSaldo()));
                        }
                        case "5" -> {
                            System.out.println("Saliendo del sistema...");
                            activo = false;
                        }
                        default -> System.out.println("Opción inválida.");
                    }
                } catch (IllegalArgumentException | IllegalStateException ex) {
                    mostrarRechazo(servicio, opcion, ex);
                } catch (java.io.UncheckedIOException ex) {
                    System.out.println("Error al guardar el movimiento: " + ex.getMessage());
                }
                System.out.println();
            }
        }
    }

    private static void mostrarRechazo(CuentaService servicio, String opcion, RuntimeException ex) {
        if ("1".equals(opcion) && ex instanceof IllegalStateException
                && ex.getMessage().startsWith("Ya existe una cuenta activa")) {
            System.out.println("Ya existe una cuenta activa. No se reemplazó.");
            return;
        }
        if (("2".equals(opcion) || "3".equals(opcion)) && ex instanceof IllegalArgumentException) {
            try {
                System.out.println("Operación rechazada. Saldo " + formato(servicio.consultarSaldo()));
            } catch (IllegalStateException sinCuenta) {
                System.out.println("Operación rechazada: " + sinCuenta.getMessage());
                return;
            }
            System.out.println("Motivo: " + ex.getMessage());
            return;
        }
        System.out.println("Operación rechazada: " + ex.getMessage());
    }

    private static String formato(BigDecimal monto) {
        return monto.stripTrailingZeros().toPlainString();
    }

    private static BigDecimal leerMonto(Scanner entrada, String pregunta) {
        System.out.print(pregunta);
        String texto = entrada.nextLine().trim();
        try {
            return new BigDecimal(texto);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Debe ingresar un número válido (ejemplo: 150.50).");
        }
    }

    private static void mostrarMenu() {
        System.out.println("=== SISTEMA BANCARIO ===");
        System.out.println("1. Registrar cuenta");
        System.out.println("2. Depositar dinero");
        System.out.println("3. Retirar dinero");
        System.out.println("4. Consultar saldo");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
    }
}

