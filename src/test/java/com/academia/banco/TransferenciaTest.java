package com.academia.banco;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Transferencias entre cuentas")
class TransferenciaTest {

    private CuentaBancaria origen;
    private CuentaBancaria destino;

    @BeforeEach
    void prepararCuentas() {
        origen = new CuentaBancaria("Ana");
        origen.depositar(new BigDecimal("1000.00"));
        
        destino = new CuentaBancaria("Beto");
    }

    @Nested
    @DisplayName("Cuando la transferencia procede")
    class CuandoLaTransferenciaProcede {

        @Test
        @DisplayName("el dinero sale de una cuenta y llega a la otra")
        void dineroSaleYLlega() {
            origen.transferir(new BigDecimal("400.00"), destino);
            
            assertAll(
                () -> assertEquals(new BigDecimal("600.00"), origen.getSaldo()),
                () -> assertEquals(new BigDecimal("400.00"), destino.getSaldo())
            );
        }

        @Test
        @DisplayName("no cobra comisión ni cuenta como retiro")
        void noCobraComisionNiRetiro() {
            origen.transferir(new BigDecimal("1000.00"), destino);
            
            assertAll(
                () -> assertEquals(new BigDecimal("0.00"), origen.getSaldo()),
                () -> assertEquals(0, origen.getRetiros())
            );
        }

        @Test
        @DisplayName("queda en el historial de las dos cuentas")
        void quedaEnHistorial() {
            origen.transferir(new BigDecimal("400.00"), destino);
            
            assertAll(
                () -> assertEquals(
                    new Movimiento(TipoMovimiento.TRANSFERENCIA_ENVIADA, new BigDecimal("400.00")),
                    origen.getMovimientos().get(1)
                ),
                () -> assertEquals(
                    List.of(new Movimiento(TipoMovimiento.TRANSFERENCIA_RECIBIDA, new BigDecimal("400.00"))),
                    destino.getMovimientos()
                )
            );
        }
    }

    @Nested
    @DisplayName("Cuando la transferencia se rechaza")
    class CuandoLaTransferenciaSeRechaza {

        @Test
        @DisplayName("sin saldo suficiente, ninguna de las dos cuentas cambia")
        void sinSaldoSuficiente() {
            assertThrows(SaldoInsuficienteException.class, 
                () -> origen.transferir(new BigDecimal("1000.01"), destino));
                
            assertAll(
                () -> assertEquals(new BigDecimal("1000.00"), origen.getSaldo()),
                () -> assertEquals(new BigDecimal("0.00"), destino.getSaldo()),
                () -> assertEquals(1, origen.getMovimientos().size()),
                () -> assertEquals(0, destino.getMovimientos().size())
            );
        }

        @Test
        @DisplayName("no se puede transferir a la misma cuenta")
        void noTransferirAMismaCuenta() {
            assertThrows(IllegalArgumentException.class, 
                () -> origen.transferir(new BigDecimal("100.00"), origen));
        }

        @Test
        @DisplayName("la cuenta destino es obligatoria")
        void cuentaDestinoObligatoria() {
            assertThrows(IllegalArgumentException.class, 
                () -> origen.transferir(new BigDecimal("100.00"), null));
        }

        @Test
        @DisplayName("el monto debe ser mayor que cero")
        void montoMayorQueCero() {
            assertThrows(IllegalArgumentException.class, 
                () -> origen.transferir(new BigDecimal("0.00"), destino));
        }
    }
}