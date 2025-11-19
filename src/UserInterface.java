import java.util.ArrayList;
import java.util.Stack;

public class UserInterface {
    public static ArrayList<User> users = new ArrayList<User>();
    private static User loggedInUser;
    private static boolean loggedIn = false;

    private UserInterface() {}

    // check if username is already used or not
    // true : never used before , false : already used
    public static boolean checkUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    // check if cardNumber is already in use or not
    // true : never used before , false : already used
    public static boolean checkCardNumber(String cardNumber) {
        for (User user : users) {
            if (user.getCardNumber().equals(cardNumber)) {
                return false;
            }
        }
        return true;
    }

    public static void addUser(User user) {
        users.add(user);
    }

    public static void register(String command) {
        User.create(command);
    }

    public static void login(String command) {
        // extract username
        String startingWord;
        if (command.contains("login ")) {
            startingWord = "login ";
        } else {
            startingWord = "Login ";
        }
        int beginIdx = command.indexOf(startingWord) + startingWord.length();
        int endIdx = beginIdx + command.substring(beginIdx).indexOf(" ");
        String userName = command.substring(beginIdx, endIdx);

        // extract password
        beginIdx = endIdx + 1;
        endIdx = command.substring(beginIdx).indexOf(" ");
        endIdx = (endIdx == -1? command.length() : endIdx + beginIdx);
        String password = command.substring(beginIdx, endIdx);

        for (int i=0; i<users.size(); i++) {
            if (users.get(i).getUsername().equals(userName) && users.get(i).getPassword().equals(password)){
                System.out.println("Login successful.");
                loggedIn = true;
                loggedInUser = users.get(i);
                return;
            }
        }

        System.out.println("Incorrect username or password");
        loggedIn = false;
        return;
    }

    public static void showBalance() {
        if (loggedIn){
            for (User user: users){
                if (user == loggedInUser) {
                    System.out.print("Current balance: ");
                    System.out.printf("%.1f\n", user.getBalance());
                }
            }
        } else {
            System.out.println("Error: You should login first.");
            return;
        }
    }

    public static void deposit(String command){
        String startingWord;
        if (command.contains("deposit ")) {
            startingWord = "deposit ";
        } else {
            startingWord = "Deposit ";
        }
        int beginIdx = command.indexOf(startingWord) + startingWord.length();
        int endIdx = command.length();
        double depositValue = Double.parseDouble(command.substring(beginIdx, endIdx));

        if (loggedIn){
            for (User user: users){
                if (user == loggedInUser) {
                    user.setBalance(user.getBalance() + depositValue);
                    System.out.print("Deposit successful. ");
                    UserInterface.showBalance();
                }
            }
        } else {
            System.out.println("Error: You should login first.");
            return;
        }
    }

    public static void withdraw(String command) {
        String startingWord;
        if (command.contains("withdraw ")) {
            startingWord = "withdraw ";
        } else {
            startingWord = "Withdraw ";
        }
        int beginIdx = command.indexOf(startingWord) + startingWord.length();
        int endIdx = command.length();
        double withdrawValue = Double.parseDouble(command.substring(beginIdx, endIdx));

        if (loggedIn){
            for (User user: users){
                if (user == loggedInUser) {
                    if (user.getBalance() > withdrawValue) {
                        user.setBalance(user.getBalance() - withdrawValue);
                        System.out.print("Withdrawal successful. ");
                        UserInterface.showBalance();
                    } else {
                        System.out.println("Error: insufficient balance.");
                    }
                }
            }
        } else {
            System.out.println("Error: You should login first.");
            return;
        }
    }

    public  static void transfer(String command) {

        String startingWord;
        if (command.contains("transfer ")) {
            startingWord = "transfer ";
        } else {
            startingWord = "Transfer ";
        }
        int beginIdx = command.indexOf(startingWord) + startingWord.length();

        int endIdx = beginIdx + command.substring(beginIdx).indexOf(" ");
        String cardNumber = command.substring(beginIdx, endIdx);

        beginIdx = endIdx + 1;
        endIdx = command.substring(beginIdx).indexOf(" ");
        endIdx = (endIdx == -1? command.length() : endIdx + beginIdx);
        Double amount = Double.parseDouble(command.substring(beginIdx, endIdx));

        double withdrawValue = Double.parseDouble(command.substring(beginIdx, endIdx));

        if (loggedIn){
            for (User user: users){
                if (user == loggedInUser) {
                    for (User transferUser: users){
                        if (transferUser.getCardNumber().equals(cardNumber)){
                            // check if user have enough money to transfer
                            if (amount <= user.getBalance()){
                                // withdraw from user
                                user.setBalance(user.getBalance() - withdrawValue);
                                // deposit to transfer user
                                transferUser.setBalance(transferUser.getBalance() + withdrawValue);
                                // print status and return
                                System.out.println("Transferred successfully.");
                                return;
                            }
                            else {
                                System.out.println("Error: insufficient balance.");
                                return;
                            }
                        }
                    }
                }
            }
            System.out.println("Error: invalid card number");
            return;
        } else {
            System.out.println("Error: You should login first.");
            return;
        }
    }

    public static void logOut() {
        if (loggedIn) {
            loggedIn = false;
            loggedInUser = null;
            System.out.println("Logout successful.");
        } else {
            System.out.println("Error: you did not log in");
        }
    }

    public static void exit(){
        loggedIn = false;
        loggedInUser = null;
    }
}
