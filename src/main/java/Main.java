import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        var scanner = new Scanner(System.in);
        var rekeningRepo = new AccountRepository();
        var transferFunds = new TransferFunds();

        while (true) {
            System.out.println("1. Nieuwe rekening");  ///      BE72091012240116
            System.out.println("2. Saldo consulteren");  ////         BE71096123456769
            System.out.println("3. Overschrijven");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            var choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1 -> {
                        System.out.print("Voer een nieuw rekeningnummer in: ");
                        var rekeningNummer = scanner.nextLine();
                        rekeningRepo.createNewAccount(rekeningNummer);
                    }
                    case 2 -> {
                        System.out.print("Voer het rekeningnummer in om het saldo te controleren: ");
                        var rekeningNummer = scanner.nextLine();
                        var rekening = rekeningRepo.getAccountBalance(rekeningNummer);
                        rekening.ifPresentOrElse(
                                r -> System.out.println("Balance: " + r.getSaldo()),
                                () -> System.out.println("Account not found."));
                    }
                    case 3 -> {
                        System.out.print("Enter from-account number: ");
                        var fromAccount = scanner.nextLine();
                        System.out.print("Enter to-account number: ");
                        var toAccount = scanner.nextLine();
                        System.out.print("Enter amount to transfer: ");
                        var amount = scanner.nextDouble();
                        scanner.nextLine();
                        transferFunds.transferFunds(fromAccount, toAccount, amount);
                    }
                    case 4 -> System.exit(0);
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
