# Errores encontrados en la versión del compañero

Mi suite (MP-1 a MP-4) atrapó sola **2** de los 5 errores: (`calculaComision` en ComisionTest y `sinSaldoSuficiente` en TransferenciaTest).

| Queja | Prueba que la reproduce | Qué estaba mal en el código (la línea y por qué) | ¿Mi suite ya lo cubría? |
|---|---|---|---|
| 1 | tercerRetiroGratis | En el método `comisionDelRetiro`, la condición usaba `< RETIROS_GRATIS` en lugar de `<=`, provocando que el retiro 3 cobrara comisión. | sí |
| 2 | retiroExactoDelLimite | En el método `retirar`, se usaba `>= LIMITE_POR_RETIRO` en lugar de `>`, lo que rechazaba retiros exactamente iguales al límite de $5,000.00. | no |
| 3 | transferenciaRechazadaNoCambiaElSaldo | En el método `transferir`, se estaba restando el monto del saldo *antes* de comprobar si el saldo era suficiente (y lanzar la excepción). | sí |
| 4 | laComisionApareceEnElHistorial | En el método `retirar`, faltaba el código para registrar y agregar el movimiento de la comisión a la lista de `movimientos` si esta se cobraba. | no |
| 5 | elHistorialNoSePuedeModificar | El método `getMovimientos` devolvía la referencia directa a la lista (`return movimientos;`), permitiendo borrar su contenido. Faltaba envolverla en `Collections.unmodifiableList()`. | no |