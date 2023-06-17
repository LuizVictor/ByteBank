package br.com.alura.bytebank.domain.account.exceptions;

public class AccountDomainException extends RuntimeException {
    public AccountDomainException(String message) {
        super(message);
    }
}
