package com.academia.banco;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Comisión por retiro")
class ComisionTest {

    @ParameterizedTest
    @CsvSource({
            "1, 0.00",
            "3, 0.00",
            "4, 10.00",
            "100, 10.00"
    })
    void calculaComision(int numero, BigDecimal esperada) {
        assertEquals(esperada, CuentaBancaria.comisionDelRetiro(numero));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void numeroRetiroInvalido(int numero) {
        assertThrows(IllegalArgumentException.class, () -> CuentaBancaria.comisionDelRetiro(numero));
    }
}