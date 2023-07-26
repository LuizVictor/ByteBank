package br.com.luizvictor.bytebank.domain.client;

import br.com.luizvictor.bytebank.domain.client.exceptions.ClientDomainException;

import java.util.regex.Pattern;

public class Cpf {
    private String cpf;

    public Cpf(String cpf) {
        setCpf(cpf);
    }

    public void setCpf(String cpf) {
        String regex = "\\d{3}.\\d{3}.\\d{3}-\\d{2}$";
        boolean isValid = Pattern.compile(regex).matcher(cpf).find();

        if (!isValid) {
            throw new ClientDomainException("Invalid CPF");
        }

        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return this.cpf;
    }
}
