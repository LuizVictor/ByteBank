package br.com.luizvictor.bytebank.cli.account;

import br.com.luizvictor.bytebank.app.account.Deposit;
import br.com.luizvictor.bytebank.domain.account.AccountRepository;
import br.com.luizvictor.bytebank.domain.account.AccountService;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.Scanner;

public class DepositCli {
    private static final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public DepositCli(AccountRepository repository, AccountService service, EntityManager entityManager) {
        System.out.println("Enter number:");
        Integer number = scanner.nextInt();

        System.out.println("Enter amount");
        BigDecimal amount = scanner.nextBigDecimal();

        Deposit deposit = new Deposit(repository, service);
        deposit.execute(number, amount);
        entityManager.flush();

        System.out.println("Amount deposited successfully");
        System.out.println("Press ENTER to return to the menu");
    }
}
