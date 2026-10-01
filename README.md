# Cuenta bancaria — pruebas unitarias con JUnit 5

**Autor:** Alan Miguel Crispin Rivera

## Cómo correr las pruebas

    ./probar.sh final

## Qué prueba cada clase

| Clase de prueba | Qué prueba | Cuántas pruebas |
|---|---|---|
| DepositoTest | Verifica que los depósitos incrementen el saldo correctamente y rechacen montos inválidos. | 2 |
| RetiroTest | Verifica las reglas de retiros (disminución de saldo, fondos insuficientes, límites diarios). | 4 |
| TransferenciaTest | Verifica las reglas para enviar dinero entre cuentas (casos exitosos y casos de rechazo). | 7 |
| InteresTest | Verifica el cálculo correcto del interés mensual según la tabla de tramos, incluyendo límites y valores frontera. | 2 (8 casos) |
| ComisionTest | Verifica que el cobro de comisión por retiro se aplique a partir del 4.º retiro y valide números inválidos. | 2 (6 casos) |
| QuejasTest | Reproduce y verifica las soluciones a los 5 errores (bugs) específicos reportados por los clientes. | 5 |
| CierreDeCuentaTest | Verifica mediante TDD las reglas de cierre de cuenta y el bloqueo de operaciones (depósitos, retiros, transferencias). | 7 |

## Boleto de salida

1. **¿Por qué una prueba que nunca viste en rojo no es confiable? Da un ejemplo de hoy.**
   Porque no tenemos garantía de que la prueba realmente esté evaluando lo que crees que evalúa; podría estar pasando por accidente (un falso positivo) o no estar conectada a la lógica real. Un ejemplo de hoy fue con el TDD del cierre de cuenta (Ciclos 3 a 7): necesitábamos ver las pruebas fallar primero (en rojo) para confirmar que el sistema *no tenía* la validación de cuenta cerrada. Si hubiéramos escrito el `if (cerrada)` antes de correr la prueba, nunca sabríamos si la prueba realmente era capaz de detectar la ausencia de esa regla.

2. **¿Qué error se escondía en un valor frontera, y qué prueba lo atrapó?**
   Hubo dos errores clásicos de valores frontera:
   * **El límite de retiro:** El código usaba `>= 5000.00` en lugar de `>`, lo que provocaba que retirar exactamente $5,000.00 fuera rechazado. Fue atrapado por la prueba `retiroExactoDelLimite` (Queja 2).
   * **Los retiros gratis:** El código evaluaba `< 3` en lugar de `<= 3`, cobrando comisión de forma incorrecta desde el tercer retiro. Lo atrapó la prueba `tercerRetiroGratis` (Queja 1).

3. **¿Qué cambió en tu forma de programar al escribir la prueba ANTES del código (MP-6)?**
   Cambia el enfoque: pasas de pensar en *"cómo voy a programar esto"* a pensar en *"qué comportamiento debe tener el sistema"*. Escribir la prueba primero te obliga a definir claramente el resultado esperado y los casos de error (como lanzar `CuentaCerradaException`). Además, te da una red de seguridad que permite limpiar el código sin miedo (como cuando extrajimos el método privado `validarAbierta()`), sabiendo de inmediato si rompiste algo.