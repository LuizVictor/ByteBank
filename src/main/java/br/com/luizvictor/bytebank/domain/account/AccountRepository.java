package br.com.luizvictor.bytebank.domain.account;

import java.util.List;

public interface AccountRepository {
    void register(Account account);

    List<Account> list();

    Account searchByNumber(Integer accountNumber);

    List<Account> searchByClientCpf(String cpf);

    void close(Integer accountNumber);
}
