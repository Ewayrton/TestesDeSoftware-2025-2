import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.DigitalWallet;

class Deposito {

    @ParameterizedTest
    @ValueSource(doubles = {10.0, 0.01, 999.99, 50.50})
    @DisplayName("Deve atualizar o saldo corretamente para depósitos válidos")
    void deveDepositarValoresValidos(double amount) {
        DigitalWallet wallet = new DigitalWallet("Proprietario Teste", 100.0);
        wallet.deposit(amount);
        assertEquals(100.0 + amount, wallet.getBalance());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -10.0, -0.01})
    @DisplayName("Deve lançar IllegalArgumentException para depósitos com valor zero ou negativo")
    void deveLancarExcecaoParaDepositoInvalido(double amount) {
        DigitalWallet wallet = new DigitalWallet("Proprietario Teste", 100.0);

        // Verifica se a exceção é lançada ao tentar depositar um valor inválido
        assertThrows(IllegalArgumentException.class, () -> {
            wallet.deposit(amount);
        });
    }
}