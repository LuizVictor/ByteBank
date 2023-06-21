package br.com.alura.bytebank.cli.account;

import br.com.alura.bytebank.app.account.ListAccount;
import br.com.alura.bytebank.domain.account.AccountDetailDto;
import br.com.alura.bytebank.domain.account.AccountRepository;

import java.util.List;
import java.util.Scanner;

public final class ListAccountsCli {
    private static final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public static List<AccountDetailDto> all(AccountRepository repository) {
        ListAccount listAccount = new ListAccount(repository);
        return listAccount.list();
    }

    public static AccountDetailDto byNumber(AccountRepository repository) {
        System.out.println("Enter number:");
        Integer number = scanner.nextInt();

        ListAccount listAccount = new ListAccount(repository);
        return listAccount.searchByNumber(number);
    }

    public static List<AccountDetailDto> byCpf(AccountRepository repository) {
        System.out.println("Enter cpf:");
        String cpf = scanner.next();

        ListAccount listAccount = new ListAccount(repository);
        return listAccount.searchByCpf(cpf);
    }
}
