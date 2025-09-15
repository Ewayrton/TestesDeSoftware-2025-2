import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Test;

import com.example.DigitalWallet;

public class Pagamento {

    @ParameterizedTest
    @CsvSource({
            "100.0, 30.0, true",   // Saldo suficiente, deve retornar true
            "50.0, 80.0, false",   // Saldo insuficiente, deve retornar false
            "10.0, 10.0, true"     // Saldo exato, deve retornar true
    })
    @DisplayName("Deve processar pagamento com carteira verificada e não bloqueada")
    void pagamentoComCarteiraVerificadaENaoBloqueada(double inicial, double valor, boolean esperado) {
        DigitalWallet wallet = new DigitalWallet("Proprietario Teste", inicial);
        wallet.verify(); // Prepara o estado correto
        wallet.unlock();

        // Pré-condição: o teste só executa se a carteira estiver no estado esperado
        assumeTrue(wallet.isVerified());
        assumeFalse(wallet.isLocked());

        boolean resultado = wallet.pay(valor);
        assertEquals(esperado, resultado);

        // Verifica se o saldo só foi alterado em caso de sucesso
        if (resultado) {
            assertEquals(inicial - valor, wallet.getBalance());
        } else {
            assertEquals(inicial, wallet.getBalance());
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -50.0})
    @DisplayName("Deve lançar IllegalArgumentException para pagamento com valor inválido")
    void deveLancarExcecaoParaPagamentoInvalido(double valor) {
        DigitalWallet wallet = new DigitalWallet("Proprietario Teste", 200.0);
        wallet.verify();

        assertThrows(IllegalArgumentException.class, () -> {
            wallet.pay(valor);
        });
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException ao pagar com carteira não verificada ou bloqueada")
    void deveLancarSeNaoVerificadaOuBloqueada() {
        // Cenário 1: Carteira não verificada
        DigitalWallet walletNaoVerificada = new DigitalWallet("Proprietario Teste", 100.0);
        assertThrows(IllegalStateException.class, () -> {
            walletNaoVerificada.pay(50.0);
        });

        // Cenário 2: Carteira bloqueada
        DigitalWallet walletBloqueada = new DigitalWallet("Proprietario Teste", 100.0);
        walletBloqueada.verify();
        walletBloqueada.lock();
        assertThrows(IllegalStateException.class, () -> {
            walletBloqueada.pay(50.0);
        });
    }
}