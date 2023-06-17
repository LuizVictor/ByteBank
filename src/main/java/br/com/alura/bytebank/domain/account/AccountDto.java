package br.com.alura.bytebank.domain.account;


import br.com.alura.bytebank.domain.client.ClientDto;

public record AccountDto(Integer number, ClientDto client) {
    public AccountDto(Account account) {
        this(
                account.number(),
                account.client()
        );
    }
}
