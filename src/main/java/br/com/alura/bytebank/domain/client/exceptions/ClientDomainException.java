package br.com.alura.bytebank.domain.client.exceptions;

public class ClientDomainException extends RuntimeException {
    public ClientDomainException(String message) {
        super(message);
    }
}
