package br.com.alura.bytebank.domain.account;

import java.math.BigDecimal;

public interface AccountService {
    void deposit(Integer accountNumber, BigDecimal amount);
    void withdraw(Integer accountNumber, BigDecimal amount);
}
