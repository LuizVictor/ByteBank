package br.com.luizvictor.bytebank.domain.client;

public record ClientDto(String name, String cpf, String email) {
    public ClientDto(Client client) {
        this(
                client.name(),
                client.cpf(),
                client.email()
        );
    }
}
