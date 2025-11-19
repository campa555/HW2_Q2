import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    // we define these variables as private static in
    // this class in order to access to them and be able
    // to change them in our methods
    private static boolean loggedIn = false;
    private static String loggedUserName = new String();

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        // all user info (including username, password, email, etc.) are stored in this data structure
        ArrayList<String[]> usersInfo = new ArrayList<String[]>();

        String command;
        int condition;
        while (true) {
            command = input.nextLine();
            condition = process_command(command, usersInfo);
            if (condition == -1) {
                System.out.print("Goodbye!");
                loggedIn = false;
                break;
            }
        }
    }

    public static int process_command(String command, ArrayList<String[]> usersInfo){
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
                register(command, usersInfo);
                return 0;
            case 2:
                login(command, usersInfo);
                return 0;
            case 4:
                showBalance(usersInfo);
                return 0;
            case 8:
                deposit(command, usersInfo);
                return 0;
            case 16:
                withdraw(command, usersInfo);
                return 0;
            case 32:
                transfer(command, usersInfo);
                return 0;
            case 64:
                logOut();
                return 0;
            case 128:
                return -1;
        }

        System.out.println("no case triggered");
        return 0;
    }

    private static void register(String command, ArrayList<String[]> usersInfo) {

        String[] info = new String[7];

        // -------------- first check if userName is unique or not --------------
        String startingWord;
        if (command.contains("register ")) {
            startingWord = "register ";
        } else {
            startingWord = "Register ";
        }
        int beginIdx = command.indexOf(startingWord) + startingWord.length();
        int endIdx = beginIdx + command.substring(beginIdx).indexOf(" ");

        String selectedUserName = command.substring(beginIdx, endIdx);
        // loop through ArrayList to see if there's any match for selected username
        for (String[] user : usersInfo) {
            if (user[0].equals(selectedUserName)) {
                System.out.println("Error: username already exists.");
                return;
            }
        }

        // add username to info
        info[0] = selectedUserName;

        // -------------- next, check password and see if it is in right format -------------
        beginIdx = endIdx + 1;
        endIdx = beginIdx + command.substring(beginIdx).indexOf(" ");

        String password = command.substring(beginIdx, endIdx);

        // first, check if our password has more than 8 characters or not
        if (password.length() < 8) {
            System.out.println("password length should more than 7 characters");
            return;
        }
        // then check weather our password has any special character in it
        boolean ContainsSpecialChar = password.contains("!") || password.contains("?")
                || password.contains("@") || password.contains("&") || password.contains("$");

        if (!ContainsSpecialChar) {
            System.out.println("password does not contain special characters");
            return;
        }

        // check if password contains at least on uppercase and one lowercase letter
        boolean DoesHaveUpperCase = false;
        boolean DoesHaveLowerCase = false;
        for (int i=0; i<password.length(); i++) {
            if (Character.isUpperCase(password.charAt(i))) {
                DoesHaveUpperCase = true;
            }
            if (Character.isLowerCase(password.charAt(i))) {
                DoesHaveLowerCase = true;
            }
        }
        if (!(DoesHaveLowerCase && DoesHaveUpperCase)) {
            System.out.println(password);
            System.out.println("password does not contain lowercase or uppercase letters");
            return;
        }

        info[1] = password;

        // ------------- add full name -----------
        beginIdx = endIdx + 1;
        endIdx = command.indexOf("09") - 1;
        if (endIdx < 0) {
            System.out.println("wrong phone number (should start with 09)");
            return;
        }
        String FullName = command.substring(beginIdx, endIdx);


        info[2] = FullName;

        // ------------- check phone number ----------------
        beginIdx = endIdx + 1;
        endIdx = endIdx + command.substring(beginIdx).indexOf(" ") + 1;
        String phoneNumber = command.substring(beginIdx, endIdx);

        // check if phone number have the right lengths
        if (phoneNumber.length() != 11) {
            System.out.println("phone Number should be 11 digits");
            return;
        }

        // check weather all characters are numbers
        for (int i=0; i<phoneNumber.length(); i++){
            if (!Character.isDigit(phoneNumber.charAt(i))) {
                System.out.println("phone number should only contain numbers");
                return;
            }
        }

        info[4] = phoneNumber;

        // ---------- check email address ------------------

        beginIdx = endIdx + 1;
        endIdx = command.substring(beginIdx).indexOf(" ");
        endIdx = (endIdx == -1 ? command.length() : endIdx + beginIdx);

        String email = command.substring(beginIdx, endIdx);

        if (email.contains("@")){
            if (email.substring(email.indexOf("@")+1).contains("@")) {
                System.out.println("incorrect email format (does have more than one @ in it) ");
                return;
            }
        }
        else {
            System.out.println("incorrect email format (does not have @ in it) ");
            return;
        }

        if (!email.contains("@aut.com")){
            System.out.println("incorrect email format (doesn't contain @aut.com)");
            return;
        }


        System.out.println("Registered successfully.");
        info[5] = email;

        // set balance to zero
        info[6] = "0";

        // assign a cardNumber that don't exist
        Random rgen = new Random();

        while (true){
            String cadNumber = "6037";
            for(int i=0; i<12; i++){
                cadNumber = cadNumber + Integer.toString(rgen.nextInt(10));
            }

            boolean DoesExist = false;

            for (int i=0; i<usersInfo.size(); i++) {
                if (usersInfo.get(i)[3].equals(cadNumber)) DoesExist = true;
            }

            if (!DoesExist) {
                info[3] = cadNumber;
                usersInfo.add(info);
                System.out.println("Assigned card number: " + info[3]);
                break;
            }

        }

    }

    private static void login(String command, ArrayList<String[]> usersInfo) {
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

        for (int i=0; i<usersInfo.size(); i++) {
            if (usersInfo.get(i)[0].equals(userName) && usersInfo.get(i)[1].equals(password)){
                System.out.println("Login successful.");
                loggedIn = true;
                loggedUserName = userName;
                return;
            }
        }

        System.out.println("Incorrect username or password");
        loggedIn = false;
        return;

    }

    private static void showBalance(ArrayList<String[]> usersInfo) {
        if (loggedIn){
            for (String[] user: usersInfo){
                if (user[0].equals(loggedUserName)) {
                    System.out.print("Current balance: ");
                    System.out.printf("%.1f\n", Double.parseDouble(user[6]));
                }
            }
        } else {
            System.out.println("Error: You should login first.");
            return;
        }
    }

    private static void deposit(String command, ArrayList<String[]> usersInfo){

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
            for (String[] user: usersInfo){
                if (user[0].equals(loggedUserName)) {
                    double newBalance = Double.parseDouble(user[6]) + depositValue;
                    user[6] = Double.toString(newBalance);
                    System.out.print("Deposit successful. ");
                    showBalance(usersInfo);
                }
            }
        } else {
            System.out.println("Error: You should login first.");
            return;
        }
    }

    private static void withdraw(String command, ArrayList<String[]> usersInfo){
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
            for (String[] user: usersInfo){
                if (user[0].equals(loggedUserName)) {
                    if (Double.parseDouble(user[6]) > withdrawValue) {
                        double newBalance = Double.parseDouble(user[6]) - withdrawValue;
                        user[6] = Double.toString(newBalance);
                        System.out.print("Withdrawal successful. ");
                        showBalance(usersInfo);
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

    private static void transfer(String command, ArrayList<String[]> usersInfo) {

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
            for (String[] user: usersInfo){
                if (user[0].equals(loggedUserName)) {
                    for (String[] transferUser: usersInfo){
                        if (transferUser[3].equals(cardNumber)){
                            // check if user have enough money to transfer
                            if (amount <= Double.parseDouble(user[6])){
                                // withdraw from user
                                double newBalance = Double.parseDouble(user[6]) - withdrawValue;
                                user[6] = Double.toString(newBalance);
                                // deposit to transfer user
                                newBalance = Double.parseDouble(transferUser[6]) + withdrawValue;
                                transferUser[6] = Double.toString(newBalance);
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

    private static void logOut() {
        if (loggedIn) {
            loggedIn = false;
            loggedUserName = "";
            System.out.println("Logout successful.");
        } else {
            System.out.println("Error: you did not log in");
        }
    }

}
