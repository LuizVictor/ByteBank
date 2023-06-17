package br.com.alura.bytebank.domain.account;

import java.math.BigDecimal;

public interface TransferService {
    void execute(Account from, Account to, BigDecimal amount);
}
