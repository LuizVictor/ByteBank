
package br.com.luizvictor.bytebank.tests.unit.domain.account;

import br.com.luizvictor.bytebank.domain.account.AccountDto;
import br.com.luizvictor.bytebank.domain.client.ClientDto;
import br.com.luizvictor.bytebank.domain.account.Account;
import br.com.luizvictor.bytebank.domain.client.exceptions.ClientDomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CreateAccountTest {
    @Test
    void mustCreateAValidAccount() {
        ClientDto client = new ClientDto("John", "123.123.123-12", "john@email.com");
        AccountDto accountDto = new AccountDto(1234, client);
        Account account = new Account(accountDto);

        assertEquals(1234, account.number());
        assertEquals("John", account.client().name());
        assertEquals("john@email.com", account.client().email());
        assertEquals(BigDecimal.ZERO, account.balance());
    }

    @Test
    void mustNotCreateAnAccountWithInvalidClientCpf() {
        Exception exception = assertThrows(ClientDomainException.class, () -> {
            ClientDto client = new ClientDto("John", "123.123.123", "john@email.com");
            AccountDto accountDto = new AccountDto(1234, client);
            new Account(accountDto);
        });

        String expectedMessage = "Invalid CPF";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void mustNotCreateAnAccountWithInvalidClientEmail() {
        Exception exception = assertThrows(ClientDomainException.class, () -> {
            ClientDto client = new ClientDto("John", "123.123.123-12", "john@email");
            AccountDto accountDto = new AccountDto(1234, client);
            new Account(accountDto);
        });

        String expectedMessage = "Invalid email";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
