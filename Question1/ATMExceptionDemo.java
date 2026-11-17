import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ATMExceptionDemo {
    private static final int BALANCE = 10000;

    static void withdraw(String accountText, String amountText) throws IOException {
        int account = Integer.parseInt(accountText);
        int amount = Integer.parseInt(amountText);
        if (account <= 0) throw new IllegalArgumentException("Account number must be positive.");
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal must be positive.");
        if (amount > BALANCE) throw new ArithmeticException("Insufficient balance.");
        // The card-reader file is deliberately checked to demonstrate a checked exception.
        Path reader = Path.of("card-reader.txt");
        if (!Files.exists(reader)) throw new IOException("Card-reader file is missing.");
        System.out.println("Withdrawal successful: Rs." + amount);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Account number: ");
            String account = sc.nextLine();
            System.out.print("Withdrawal amount: ");
            String amount = sc.nextLine();
            withdraw(account, amount);
        } catch (NumberFormatException | InputMismatchException e) {
            System.out.println("Input error: enter numeric values.");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println("Transaction error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Device error: " + e.getMessage());
        } finally {
            System.out.println("ATM session closed.");
            sc.close();
        }
    }
}