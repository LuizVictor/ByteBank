package br.com.alura.bytebank.domain.client;

public record ClientDto(String name, String cpf, String email) {
    public ClientDto(Client client) {
        this(
                client.getName(),
                client.getCpf(),
                client.getEmail()
        );
    }
}
