package pe.edu.unsch.banco.aplicacion;

import pe.edu.unsch.banco.dominio.CuentaBancaria;
import java.math.BigDecimal;
import java.util.Objects;

/** Casos de uso. Conserva una sola cuenta activa en memoria durante la sesión. */
public final class CuentaService {
    private final MovimientoRepository repositorio;
    private CuentaBancaria cuenta;

    public CuentaService(MovimientoRepository repositorio) {
        this.repositorio = Objects.requireNonNull(repositorio, "Repositorio obligatorio");
    }

    public void registrar(String titular, BigDecimal saldoInicial) {
        if (cuenta != null) {
            throw new IllegalStateException("Ya existe una cuenta activa; no será reemplazada.");
        }
        CuentaBancaria nueva = new CuentaBancaria(titular, saldoInicial);
        repositorio.guardar("Cuenta creada | Titular: " + nueva.getTitular()
                + " | Saldo inicial: S/ " + nueva.getSaldo().toPlainString());
        cuenta = nueva;
    }

    public void depositar(BigDecimal monto) {
        CuentaBancaria activa = obtenerCuenta();
        activa.depositar(monto);
        repositorio.guardar("Titular: " + activa.getTitular() + " | Deposito: S/ "
                + monto.setScale(2).toPlainString() + " | Saldo actual: S/ "
                + activa.getSaldo().toPlainString());
    }

    public void retirar(BigDecimal monto) {
        CuentaBancaria activa = obtenerCuenta();
        activa.retirar(monto);
        repositorio.guardar("Titular: " + activa.getTitular() + " | Retiro: S/ "
                + monto.setScale(2).toPlainString() + " | Saldo actual: S/ "
                + activa.getSaldo().toPlainString());
    }

    public BigDecimal consultarSaldo() {
        return obtenerCuenta().getSaldo();
    }

    public String consultarTitular() {
        return obtenerCuenta().getTitular();
    }

    private CuentaBancaria obtenerCuenta() {
        if (cuenta == null) {
            throw new IllegalStateException("No hay una cuenta registrada en esta sesión.");
        }
        return cuenta;
    }
}
