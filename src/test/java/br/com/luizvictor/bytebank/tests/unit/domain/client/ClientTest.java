package br.com.luizvictor.bytebank.tests.unit.domain.client;

import br.com.luizvictor.bytebank.domain.client.Client;
import br.com.luizvictor.bytebank.domain.client.ClientDto;
import br.com.luizvictor.bytebank.domain.client.exceptions.ClientDomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    @Test
    void mustCreateAnValidClient() {
        ClientDto data = new ClientDto("John", "123.123.123-12", "email@email.com");
        Client client = new Client(data);
        assertEquals("John", client.name());
    }

    @Test
    void mustNotCreateClientWithInvalidEmail() {
        Exception exception = assertThrows(ClientDomainException.class, () -> {
            ClientDto data = new ClientDto("John", "123.123.123-12", "email@email");
            new Client(data);
        });

        String actual = exception.getMessage();
        String expected = "Invalid email";

        assertEquals(expected, actual);
    }

    @Test
    void mustNotCreateClientWithInvalidCpf() {
        Exception exception = assertThrows(ClientDomainException.class, () -> {
            ClientDto data = new ClientDto("John", "123.123.123", "email@email.com");
            new Client(data);
        });

        String actual = exception.getMessage();
        String expected = "Invalid CPF";

        assertEquals(expected, actual);
    }
}