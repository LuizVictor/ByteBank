package br.com.alura.bytebank.domain.client;

import java.util.Objects;

public class Client {
    private String name;
    private final Cpf cpf;
    private Email email;

    public Client(ClientDto data) {
        this.name = data.name();
        this.cpf = new Cpf(data.cpf());
        this.email = new Email(data.email());
    }

    public String getCpf() {
        return cpf.toString();
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return email.toString();
    }

    public void update(ClientUpdateDto data) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return cpf.equals(client.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", cpf='" + cpf.toString() + '\'' +
                ", email='" + email.toString() + '\'' +
                '}';
    }
}
