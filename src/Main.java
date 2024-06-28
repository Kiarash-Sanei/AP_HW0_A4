import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Information information = new Information();
        State myState = State.registering;
        while (myState != State.end) {
            String line = input.nextLine();
            line = line.trim();
            if (myState == State.registering)
                myState = registering(line);
            else if (myState == State.enteringData)
                myState = enteringData(line, information);
            else
                myState = enteringBiography(line, information);
        }
        costumePrinter(information);
    }

    public static State registering(String line) {
        if (!line.startsWith("register")) {
            System.out.println("invalid command");
            return State.registering;
        }
        Pattern flagPattern = Pattern.compile("\\s*(-[^-\\s]+)\\s+([^ \\s]+)");
        Matcher flag = flagPattern.matcher(line);
        int flagCounter = 0;
        String temporaryUsername = null;
        String temporaryPassword = null;
        while (flag.find()) {
            if (Objects.equals(flag.group(1), "-u")) {
                if (temporaryUsername == null)
                    temporaryUsername = flag.group(2);
                else {
                    flagCounter += 3;
                    break;
                }
            } else if (Objects.equals(flag.group(1), "-p")) {
                if (temporaryPassword == null)
                    temporaryPassword = flag.group(2);
                else {
                    flagCounter += 3;
                    break;
                }
            } else {
                flagCounter += 3;
                break;
            }
            flagCounter++;
        }
        if (flagCounter != 2) {
            System.out.println("wrong flag format");
            return State.registering;
        }
        Username username = new Username();
        Password password = new Password();
        if (username.setUsername(temporaryUsername) &&
                password.setPassword(temporaryPassword, temporaryUsername)) {
            System.out.println("register successful");
            return State.enteringData;
        }
        return State.registering;
    }

    public static State enteringData(String line, Information information) {
        if (!line.startsWith("data")) {
            System.out.println("invalid command");
            return State.enteringData;
        }
        Pattern flagPattern = Pattern.compile("\\s*(-[^-\\s]+)\\s+([^ \\s]+)");
        Matcher flag = flagPattern.matcher(line);
        int flagCounter = 0;
        String temporaryFirstName = null;
        String temporaryLastName = null;
        String temporaryEmail = null;
        String temporaryPhoneNumber = null;
        while (flag.find()) {
            if (Objects.equals(flag.group(1), "-fn")) {
                if (temporaryFirstName == null)
                    temporaryFirstName = flag.group(2);
                else {
                    flagCounter += 5;
                    break;
                }
            } else if (Objects.equals(flag.group(1), "-ln")) {
                if (temporaryLastName == null)
                    temporaryLastName = flag.group(2);
                else {
                    flagCounter += 5;
                    break;
                }
            } else if (Objects.equals(flag.group(1), "-e")) {
                if (temporaryEmail == null)
                    temporaryEmail = flag.group(2);
                else {
                    flagCounter += 5;
                    break;
                }
            } else if (Objects.equals(flag.group(1), "-ph")) {
                if (temporaryPhoneNumber == null)
                    temporaryPhoneNumber = flag.group(2);
                else {
                    flagCounter += 5;
                    break;
                }
            } else {
                flagCounter += 5;
                break;
            }
            flagCounter++;
        }
        if (flagCounter != 4) {
            System.out.println("wrong flag format");
            return State.enteringData;
        }
        FirstName firstName = new FirstName();
        LastName lastName = new LastName();
        Email email = new Email();
        PhoneNumber phoneNumber = new PhoneNumber();
        if (firstName.setFirstName(temporaryFirstName) &&
                lastName.setLastName(temporaryLastName) &&
                email.setEmail(temporaryEmail) &&
                phoneNumber.setPhoneNumber(temporaryPhoneNumber)) {
            System.out.println("data saved successfully");
            information.firstName = firstName.getFirstName();
            information.lastName = lastName.getLastName();
            information.email = email.getEmail();
            information.phoneNumber = phoneNumber.getPhoneNumber();
            return State.enteringBiography;
        }
        return State.enteringData;
    }

    public static State enteringBiography(String line, Information information) {
        if (!line.startsWith("bio")) {
            System.out.println("invalid command");
            return State.enteringBiography;
        }
        line = line.substring(3);
        line = line.trim();
        String biography;
        if (line.startsWith("\"") &&
                line.endsWith("\"")) {
            biography = line.substring(1, line.length() - 1);
            information.biography = biography;
            return State.end;
        }
        System.out.println("invalid command");
        return State.enteringBiography;
    }

    public static void costumePrinter(Information information) {
        System.out.println("----------");
        System.out.println(information.firstName + " " + information.lastName);
        System.out.println("Email: " + information.email);
        System.out.println("Phone Number: " + information.phoneNumber);
        System.out.println();
        System.out.println("Biography:");
        String biography = information.biography;
        biography = biography.replaceAll(" +", " ");    
        biography = biography.replaceAll("\\\\n", "\n");
        biography = biography.replaceAll("\\n{2,}" , "\n\n");
        biography = biography.trim();
        if (biography != null &&
                !biography.isBlank()) {
            StringBuilder line = new StringBuilder();
            String[] words = biography.split(" +");
            for (String word : words) {
                if (word.contains("\n")) {
                    String firstWords = word.substring(0, word.indexOf("\n"));
                    String newLines = word.substring(word.indexOf("\n"), word.lastIndexOf("\n") + 1);
                    String lastWords = word.substring(word.lastIndexOf("\n") + 1);
                    if (line.length() + firstWords.length() > 40) {
                        System.out.print(line);
                        line.setLength(0);
                        line.append("\n");
                        line.append(firstWords);
                        line.append(newLines);
                    } else {
                        line.append(firstWords);
                        line.append(newLines);
                    }
                    System.out.print(line);
                    line.setLength(0);
                    line.append(lastWords);
                    line.append(" ");
                } else if (line.length() + word.length() > 40) {
                    System.out.print(line);
                    line.setLength(0);
                    line.append("\n");
                    line.append(word);
                    line.append(" ");
                } else if (line.length() + word.length() == 40) {
                    line.append(word);
                    line.append("\n");
                    System.out.print(line);
                    line.setLength(0);
                    line.append(" ");
                } else {
                    line.append(word);
                    line.append(" ");
                }
            }
        }
        System.out.println();
        System.out.print("----------");
    }

    enum State {
        registering,
        enteringData,
        enteringBiography,
        end
    }

    static class Username {
        private String username;
        private static final Pattern usernamePattern = Pattern.compile("[a-zA-Z]([._]?[a-zA-Z0-9]+)*");

        public boolean setUsername(String username) {
            if (usernamePattern.matcher(username).matches()) {
                this.username = username;
                return true;
            }
            System.out.println("invalid username format");
            return false;
        }

        public String getUsername() {
            return username;
        }
    }

    static class Password {
        private String password;
        private static final Pattern passwordPattern = Pattern.compile("(\\S{6,20})");
        private static final Pattern lowerCaseEnglishLetterPattern = Pattern.compile("[a-z]");
        private static final Pattern upperCaseEnglishLetterPattern = Pattern.compile("[A-Z]");
        private static final Pattern digitPattern = Pattern.compile("\\d");
        private static final Pattern specialCharacterPattern = Pattern.compile("[!@#$%^&*()\\-+=]");
        private static final Pattern moreThan3TimesRepetitionPattern = Pattern.compile("(.)\\1{3,}");

        public boolean setPassword(String password, String username) {
            if (passwordPattern.matcher(password).matches() &&
                    lowerCaseEnglishLetterPattern.matcher(password).find() &&
                    upperCaseEnglishLetterPattern.matcher(password).find() &&
                    digitPattern.matcher(password).find() &&
                    specialCharacterPattern.matcher(password).find() &&
                    !moreThan3TimesRepetitionPattern.matcher(password).find()) {
                String[] notAllowedParts = username.split("[._\\d]");
                for (String s : notAllowedParts) {
                    if (s.length() > 3 &&
                            password.contains(s)) {
                        System.out.println("weak password");
                        return false;
                    }
                }
                this.password = password;
                return true;
            }
            System.out.println("weak password");
            return false;
        }

        public String getPassword() {
            return password;
        }
    }

    static class FirstName {
        private String firstName;
        private static final Pattern firstNamePattern = Pattern.compile("[a-zA-z]+");

        public boolean setFirstName(String firstName) {
            if (firstNamePattern.matcher(firstName).matches() &&
                    !firstName.contains("_") &&
                    !firstName.contains("^")) {
                StringBuilder result = new StringBuilder();
                result.append(firstName.toLowerCase(Locale.ENGLISH));
                result.setCharAt(0, (char) (result.charAt(0) - 32));
                this.firstName = result.toString();
                return true;
            }
            System.out.println("wrong name format");
            return false;
        }

        public String getFirstName() {
            return firstName;
        }
    }

    static class LastName {
        private String lastName;
        private static final Pattern lastNamePattern = Pattern.compile("[a-zA-z]+(-?[a-zA-z]+)?");

        public boolean setLastName(String lastName) {
            if (lastNamePattern.matcher(lastName).matches() &&
                    !lastName.contains("_") &&
                    !lastName.contains("^")) {
                StringBuilder result = new StringBuilder();
                result.append(lastName.toLowerCase(Locale.ENGLISH));
                result.setCharAt(0, (char) (result.charAt(0) - 32));
                if (lastName.contains("-"))
                    result.setCharAt(result.indexOf("-") + 1, (char) (result.charAt(result.indexOf("-") + 1) - 32));
                this.lastName = result.toString();
                return true;
            }
            System.out.println("wrong name format");
            return false;
        }

        public String getLastName() {
            return lastName;
        }
    }

    static class Email {
        private String email;
        private static final Pattern emailPattern = Pattern.compile("[a-zA-Z]([._]?[a-zA-Z0-9]+)*@[a-zA-Z](\\.?[a-zA-Z]+)*\\.[cC][oO][mM]");

        public boolean setEmail(String email) {
            if (emailPattern.matcher(email).matches()) {
                String emailDot = email.substring(email.indexOf('@'));
                int dotCounter = 0;
                for (int i = 0; i < emailDot.length(); i++) {
                    if (emailDot.charAt(i) == '.')
                        dotCounter++;
                }
                if (dotCounter < 3) {
                    this.email = email.toLowerCase(Locale.ENGLISH);
                    return true;
                }
            }
            System.out.println("invalid email format");
            return false;
        }

        public String getEmail() {
            return email;
        }
    }

    static class PhoneNumber {
        private String phoneNumber;
        private static final Pattern phoneNumberPattern = Pattern.compile("((09)|(\\+989))\\d{9}");

        public boolean setPhoneNumber(String phoneNumber) {
            if (phoneNumberPattern.matcher(phoneNumber).matches()) {
                StringBuilder result = new StringBuilder();
                result.append("+98-9");
                if (phoneNumber.startsWith("+"))
                    result.append(phoneNumber.substring(4));
                else
                    result.append(phoneNumber.substring(2));
                result.insert(7, '-');
                result.insert(11, '-');
                this.phoneNumber = result.toString();
                return true;
            }
            System.out.println("invalid phone number");
            return false;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }
    }

    static class Information {
        String firstName;
        String lastName;
        String email;
        String phoneNumber;
        String biography;
    }

}