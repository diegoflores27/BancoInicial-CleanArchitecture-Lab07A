package pe.edu.unsch.banco.dominio;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Entidad del dominio: no conoce consola, servicios ni almacenamiento. */
public final class CuentaBancaria {
    private final String titular;
    private BigDecimal saldo;

    public CuentaBancaria(String titular, BigDecimal saldoInicial) {
        if (titular == null || titular.isBlank()) {
            throw new IllegalArgumentException("El titular no puede estar vacío.");
        }
        this.titular = titular.trim();
        this.saldo = normalizar(saldoInicial);
        if (this.saldo.signum() < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo.");
        }
    }

    public String getTitular() {
        return titular;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void depositar(BigDecimal monto) {
        BigDecimal cantidad = validarPositivo(monto);
        saldo = saldo.add(cantidad);
    }

    public void retirar(BigDecimal monto) {
        BigDecimal cantidad = validarPositivo(monto);
        if (cantidad.compareTo(saldo) > 0) {
            throw new IllegalArgumentException("Fondos insuficientes.");
        }
        saldo = saldo.subtract(cantidad);
    }

    private static BigDecimal validarPositivo(BigDecimal monto) {
        BigDecimal cantidad = normalizar(monto);
        if (cantidad.signum() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        }
        return cantidad;
    }

    private static BigDecimal normalizar(BigDecimal monto) {
        if (monto == null) {
            throw new IllegalArgumentException("El monto no puede ser nulo.");
        }
        try {
            return monto.setScale(2, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException ex) {
            throw new IllegalArgumentException("El monto admite como máximo dos decimales.");
        }
    }
}
