package br.com.alura.bytebank.cli.account;

import br.com.alura.bytebank.app.account.CloseAccount;
import br.com.alura.bytebank.domain.account.AccountRepository;
import jakarta.persistence.EntityManager;

import java.util.Scanner;

public class CloseAccountCli {
    private static final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public CloseAccountCli(AccountRepository repository, EntityManager entityManager) {
        System.out.println("Enter account number:");
        Integer number = scanner.nextInt();

        CloseAccount close = new CloseAccount(repository);
        close.execute(number);
        entityManager.flush();

        System.out.println("Account closed");
        System.out.println("Press ENTER to return to the menu");
    }
}
