package br.com.alura.bytebank.domain.account;

import br.com.alura.bytebank.domain.client.Client;
import br.com.alura.bytebank.domain.client.ClientDto;

import java.math.BigDecimal;

public class Account {
    private final Integer number;
    private BigDecimal balance;
    private final Client client;

    public Account(AccountDto data) {
        this.number = data.number();
        this.client = new Client(data.client());
        this.balance = BigDecimal.ZERO;
    }

    public Integer number() {
        return number;
    }

    public BigDecimal balance() {
        return balance;
    }

    public ClientDto client() {
        return new ClientDto(client);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return number.equals(account.number);
    }

    @Override
    public String toString() {
        return "Account {" +
                "number='" + number + '\'' +
                ", balance=" + balance +
                ", client=" + client +
                '}';
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
