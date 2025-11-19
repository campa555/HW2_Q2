import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        String command;
        int condition;
        while (true) {
            command = input.nextLine();
            condition = process_command(command);
            if (condition == -1) {
                System.out.print("Goodbye!");
                UserInterface.exit();
                break;
            }
        }
    }

    public static int process_command(String command){
        int expression = (command.contains("register") || command.contains("Register") ? 1 : 0)
                | (command.contains("login") || command.contains("Login")? 2 : 0)
                | (command.contains("show balance") || command.contains("Show balance") || command.contains("Show Balance") ? 4 : 0)
                | (command.contains("deposit") || command.contains("Deposit") ? 8 : 0)
                | (command.contains("withdraw") || command.contains("Withdraw") ? 16 : 0)
                | (command.contains("transfer") || command.contains("Transfer") ? 32 : 0)
                | (command.contains("logout") || command.contains("Logout") ? 64 : 0)
                | (command.contains("exit") || command.contains("Exit")? 128 : 0);

        switch (expression) {
            case 1:
                UserInterface.register(command);
                return 0;
            case 2:
                UserInterface.login(command);
                return 0;
            case 4:
                UserInterface.showBalance();
                return 0;
            case 8:
                UserInterface.deposit(command);
                return 0;
            case 16:
                UserInterface.withdraw(command);
                return 0;
            case 32:
                UserInterface.transfer(command);
                return 0;
            case 64:
                UserInterface.logOut();
                return 0;
            case 128:
                return -1;
        }

        System.out.println("no case triggered");
        return 0;
    }

}
