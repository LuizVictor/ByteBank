package br.com.alura.bytebank.model;

import br.com.alura.bytebank.domain.account.AccountDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class AccountModel {
    @Id
    private Integer number;
    @ManyToOne
    private ClientModel client;
    private BigDecimal balance;

    public AccountModel(AccountDto data) {
        this.number = data.number();
        this.client = new ClientModel(data.client());
        this.balance = BigDecimal.ZERO;
    }

    public AccountModel() {
    }

    public Integer number() {
        return this.number;
    }

    public ClientModel client() {
        return this.client;
    }

    public BigDecimal balance() {
        return this.balance;
    }

    public void setBalance(BigDecimal amount) {
        this.balance = amount;
    }
}
