package br.com.luizvictor.bytebank.domain.account;

import br.com.luizvictor.bytebank.domain.client.Client;
import br.com.luizvictor.bytebank.domain.client.ClientDto;

import java.math.BigDecimal;
import java.util.Random;

public class Account {
    private Integer number;
    private BigDecimal balance;
    private Client client;

    public Account(ClientDto client) {
        this.number = generateNumber();
        this.client = new Client(client);
        this.balance = BigDecimal.ZERO;
    }

    public Account() {

    }

    private Integer generateNumber() {
        Random random = new Random();
        return random.nextInt(9000) + 1000;
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
