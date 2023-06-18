package br.com.alura.bytebank.domain.client;

public record ClientUpdateDto(String name, String email) {
    public ClientUpdateDto(Client client) {
        this(client.name(), client.email());
    }
}
