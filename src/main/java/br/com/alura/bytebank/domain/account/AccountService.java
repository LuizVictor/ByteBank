package br.com.alura.bytebank.domain.account;

import java.math.BigDecimal;

public interface AccountService {
    void deposit(Account account, BigDecimal amount);
    void withdraw(Account account, BigDecimal amount);
}
