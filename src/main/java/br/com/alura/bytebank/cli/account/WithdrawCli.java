package br.com.alura.bytebank.cli.account;

import br.com.alura.bytebank.app.account.Withdraw;
import br.com.alura.bytebank.domain.account.AccountRepository;
import br.com.alura.bytebank.domain.account.AccountService;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.Scanner;

public class WithdrawCli {
    private static final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public WithdrawCli(AccountRepository repository, AccountService service, EntityManager entityManager) {
        System.out.println("Enter number:");
        Integer number = scanner.nextInt();

        System.out.println("Enter amount");
        BigDecimal amount = scanner.nextBigDecimal();

        Withdraw withdraw = new Withdraw(repository, service);
        withdraw.execute(number, amount);
        entityManager.flush();
    }
}
