package br.com.alura.bytebank.domain.client;

import br.com.alura.bytebank.domain.client.exceptions.ClientDomainException;

import java.util.Objects;
import java.util.regex.Pattern;

public class Client {
    private String name;
    private String cpf;
    private String email;

    public Client(ClientDto data) {
        this.name = data.name();
        this.setCpf(data.cpf());
        this.setEmail(data.email());
    }

    public Client() {

    }

    public String cpf() {
        return cpf;
    }

    public String name() {
        return this.name;
    }

    public String email() {
        return email;
    }

    private void setCpf(String cpf) {
        String regex = "\\d{3}.\\d{3}.\\d{3}-\\d{2}$";
        boolean isValid = Pattern.compile(regex).matcher(cpf).find();

        if (!isValid) {
            throw new ClientDomainException("Invalid CPF");
        }

        this.cpf = cpf;
    }

    private void setEmail(String email) {
        String regex = "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$";
        boolean isValid = Pattern.compile(regex).matcher(email).find();

        if (!isValid) {
            throw new ClientDomainException("Invalid email");
        }

        this.email = email;
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
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public void update(ClientUpdateDto updateDto) {
        if (!updateDto.name().isEmpty()) {
            this.name = updateDto.name();
        }

        if (!updateDto.email().isEmpty()) {
            this.email = updateDto.email();
        }
    }
}
