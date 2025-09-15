# Atividade: Carteira Digital (JUnit 5)

## Objetivo

Testar funcionalidades de uma Carteira Digital (e-wallet) usando JUnit 5, cobrindo operações de depósito, pagamento e estorno, além de regras de verificação de conta (KYC) e bloqueio. O foco é exercitar:

- ```assumeTrue```/```assumeFalse``` (pré-condições contextuais),
- ```assertThrows``` (erros de regra/entrada),
- testes parametrizados (```@ParameterizedTest``` com ```@ValueSource```, ```@CsvSource``` e ```@MethodSource```).

## Classe sob teste: DigitalWallet

A classe representa uma carteira digital simples.

### API esperada
```java
public class DigitalWallet {
    public DigitalWallet(String owner, double initialBalance) { ... }

    public String getOwner();                 // nome do titular
    public double getBalance();               // saldo atual
    public boolean isVerified();              // KYC concluído?
    public boolean isLocked();                // carteira bloqueada?
    public void verify();                     // marca como verificada
    public void lock();                       // bloqueia carteira
    public void unlock();                     // desbloqueia carteira

    public void deposit(double amount);       // adiciona ao saldo (valor > 0)
    public boolean pay(double amount);        // debita se verificada, não bloqueada, valor > 0 e saldo suficiente
    public void refund(double amount);        // estorna (valor > 0), exige verificada e não bloqueada
}

```
## Regras

1. Saldo inicial deve ser ≥ 0. Saldo negativo no construtor deve lançar ```IllegalArgumentException```.

2. Depósito: apenas ```amount > 0```. Caso contrário, lançar ```IllegalArgumentException```.

3. Pagamento (pay):

    - Requer carteira verificada e não bloqueada.
    - ```amount > 0```.
    - Retorna true se houver saldo suficiente e debita; false se saldo insuficiente.

4. Estorno (refund):

    - Requer carteira verificada e não bloqueada.
    - ```amount > 0```. Caso contrário, lançar ```IllegalArgumentException```.

### Requisitos de Teste (o que implementar)
1) Saldo Inicial (básico)

    - Verifique que o saldo inicial é configurado corretamente.
    - Caso de erro: tentar criar carteira com saldo inicial negativo deve lançar ```IllegalArgumentException``` (```assertThrows```).

2) Depósito

    - Atualiza corretamente o saldo para valores válidos (use ```@ParameterizedTest``` com ```@ValueSource(doubles = {...})```).
    - Depósito com ```0``` ou valor negativo deve lançar ```IllegalArgumentException``` (```assertThrows```).

3) Pagamento (pay)

    - Pré-condição com ```assumeTrue```: assuma que a carteira está verificada e não bloqueada para os testes “felizes”.
    - Caso feliz: pagamentos válidos debitam saldo e retornam true (use ```@CsvSource``` com pares ```saldoInicial,valorPagamento,resultadoEsperado```).
    - Saldo insuficiente retorna ```false``` e não altera saldo.
    - Casos de erro com ```assertThrows```: pagamento com valor ≤ 0 deve lançar exceção.

4) Estorno (refund)

    - Use ```assumeTrue``` para exigir carteira verificada e não bloqueada.
    - Estorno válido (```amount > 0```) aumenta o saldo.
    - ```assertThrows``` ao estornar 0 ou negativo.

5) Estados da Carteira (verificada/bloqueada)

    - ```assumeFalse``` / ```assumeTrue``` para pular cenários inválidos:
        - Se a carteira não estiver verificada, ```pay``` e ```refund``` devem lançar ```IllegalStateException```.
        - Se a carteira estiver bloqueada, ```pay``` e ```refund``` devem lançar ```IllegalStateException```.

### Dicas de Parametrização

- ```@ValueSource(doubles = {10.0, 0.01, 999.99})``` para depósitos válidos.
- ```@CsvSource({ "100.0, 30.0, true", "50.0, 80.0, false", "10.0, 10.0, true" })``` para pagamentos.
- ```@MethodSource``` para gerar combinações mais ricas (ex.: múltiplos estornos em sequência).

## Critérios de Avaliação

- Uso correto de ```assumeTrue```/```assumeFalse``` para pré-condições (verificada/não bloqueada).
- Uso de ```assertThrows``` para entradas inválidas e estados inválidos.
- Cobertura com Parameterized Tests (```@ValueSource```, ```@CsvSource``` e pelo menos um ```@MethodSource```).
- Clareza na organização e nomes descritivos.