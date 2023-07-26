package br.com.luizvictor.bytebank.domain.client;

import br.com.luizvictor.bytebank.domain.client.exceptions.ClientDomainException;

import java.util.regex.Pattern;

public class Email {

    private String email;

    public Email(String email) {
        this.setEmail(email);
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
    public String toString() {
        return this.email;
    }
}
