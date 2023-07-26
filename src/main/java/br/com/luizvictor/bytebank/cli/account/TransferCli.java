package br.com.luizvictor.bytebank.cli.account;

import br.com.luizvictor.bytebank.app.account.Transfer;
import br.com.luizvictor.bytebank.domain.account.AccountRepository;
import br.com.luizvictor.bytebank.domain.account.AccountService;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.Scanner;

public class TransferCli {
    private static final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public TransferCli(AccountRepository repository, AccountService service, EntityManager entityManager) {
        System.out.println("Enter account number that will send");
        Integer from = scanner.nextInt();

        System.out.println("Enter account number that will receive");
        Integer to = scanner.nextInt();

        System.out.println("Enter amount");
        BigDecimal amount = scanner.nextBigDecimal();

        Transfer transfer = new Transfer(repository, service);
        transfer.execute(from, to, amount);
        entityManager.flush();

        System.out.println("Amount transferred successfully");
        System.out.println("Press ENTER to return to the menu");
    }
}
