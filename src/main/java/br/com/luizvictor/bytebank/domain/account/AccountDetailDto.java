package br.com.luizvictor.bytebank.domain.account;

import br.com.luizvictor.bytebank.domain.client.ClientDto;

import java.math.BigDecimal;

public record AccountDetailDto(Integer number, ClientDto client, BigDecimal balance) {
    public AccountDetailDto(Account account) {
        this(
                account.number(),
                account.client(),
                account.balance()
        );
    }
}
