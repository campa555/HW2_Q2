import java.sql.Struct;
import java.util.Random;

public class User {
    private boolean logged;
    private String username;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String email;
    private double balance;
    private String cardNumber;

    private User(String username, String password, String fullName, String phoneNumber, String email, double balance, String cardNumber) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.balance = balance;
        this.cardNumber = cardNumber;
        this.logged = false;
    }

    public String getUsername() {
        return username;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public static void create(String command) {
        // ------------- first check if username was already used or not --------------
        // extract username
        String startingWord;
        if (command.contains("register ")) {
            startingWord = "register ";
        } else {
            startingWord = "Register ";
        }
        int beginIdx = command.indexOf(startingWord) + startingWord.length();
        int endIdx = beginIdx + command.substring(beginIdx).indexOf(" ");

        // add username to info
        String username = command.substring(beginIdx, endIdx);
        if (!UserInterface.checkUsername(username)) {
            System.out.println("Error: username already exists.");
            return;
        }

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



        // ------------- add full name -----------
        beginIdx = endIdx + 1;
        endIdx = command.indexOf("09") - 1;
        if (endIdx < 0) {
            System.out.println("wrong phone number (should start with 09)");
            return;
        }
        String FullName = command.substring(beginIdx, endIdx);




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

        // assign a cardNumber that don't exist
        Random rgen = new Random();

        while (true){
            String cardNumber = "6037";
            for(int i=0; i<12; i++){
                cardNumber = cardNumber + Integer.toString(rgen.nextInt(10));
            }

            if (UserInterface.checkCardNumber(cardNumber)) {
                User newUser = new User(username, password, FullName, phoneNumber, email, 0.0 , cardNumber);
                UserInterface.addUser(newUser);
                System.out.println("Assigned card number: " + cardNumber);
                break;
            }

        }
    }


}
