import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.DigitalWallet;

class SaldoInicial {
    @Test
    @DisplayName("Deve configurar o saldo inicial corretamente ao criar a carteira")
    void deveConfigurarSaldoInicialCorreto() {
        DigitalWallet wallet = new DigitalWallet("Proprietario Teste", 150.75);
        assertEquals(150.75, wallet.getBalance());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException para saldo inicial negativo")
    void deveLancarExcecaoParaSaldoInicialNegativo() {
        // Tenta criar uma carteira com saldo inicial negativo e verifica a exceção
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new DigitalWallet("Proprietario Teste", -100.0);
        });

        // Opcional: verifica se a mensagem de erro está correta
        assertEquals("Negative initial balance", exception.getMessage());
    }
}