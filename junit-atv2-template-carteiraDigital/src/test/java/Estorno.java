import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Test;

import com.example.DigitalWallet;

class Estorno {
    // Método provedor de argumentos para o teste de estorno
    static Stream<Arguments> valoresEstorno() {
        return Stream.of(
                Arguments.of(100.0, 10.0, 110.0), // Saldo inicial, valor estorno, saldo esperado
                Arguments.of(0.0,   5.0,   5.0),
                Arguments.of(50.0,  0.01, 50.01)
        );
    }

    @ParameterizedTest
    @MethodSource("valoresEstorno")
    @DisplayName("Deve adicionar valor ao saldo ao estornar com carteira válida")
    void refundComCarteiraValida(double inicial, double valor, double saldoEsperado) {
        DigitalWallet wallet = new DigitalWallet("Proprietario Teste", inicial);
        wallet.verify();
        wallet.unlock();

        // Pré-condição: Testa apenas se a carteira está ativa e verificada
        assumeTrue(wallet.isVerified() && !wallet.isLocked());

        wallet.refund(valor);
        assertEquals(saldoEsperado, wallet.getBalance());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -25.0})
    @DisplayName("Deve lançar IllegalArgumentException para estorno com valor inválido")
    void deveLancarExcecaoParaRefundInvalido(double valor) {
        DigitalWallet wallet = new DigitalWallet("Proprietario Teste", 100.0);
        wallet.verify();

        assertThrows(IllegalArgumentException.class, () -> {
            wallet.refund(valor);
        });
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException ao estornar com carteira não verificada ou bloqueada")
    void deveLancarSeNaoVerificadaOuBloqueada() {
        // Cenário 1: Carteira não verificada
        DigitalWallet walletNaoVerificada = new DigitalWallet("Proprietario Teste", 100.0);
        assertThrows(IllegalStateException.class, () -> {
            walletNaoVerificada.refund(50.0);
        });

        // Cenário 2: Carteira bloqueada
        DigitalWallet walletBloqueada = new DigitalWallet("Proprietario Teste", 100.0);
        walletBloqueada.verify();
        walletBloqueada.lock();
        assertThrows(IllegalStateException.class, () -> {
            walletBloqueada.refund(50.0);
        });
    }
}