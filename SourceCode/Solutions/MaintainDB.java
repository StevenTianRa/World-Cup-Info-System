import java.sql.*;
import java.util.*;

import javax.naming.spi.StateFactory;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// sample input: localhost 5432 database_name username password
public class MaintainDB {

    private Scanner input = new Scanner(System.in);
    private Connection connection = null;

    public MaintainDB(String[] args) {

        // loading the DBMS driver
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Missing DBMS driver.");
            e.printStackTrace();
        }

        try {
            // connecting to the a database
            connection = DriverManager
                    .getConnection("jdbc:db2:CS348");
            System.out.println("Database connection open.\n");

            // setting auto commit to false
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println("DBMS connection failed.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        MaintainDB menu = new MaintainDB(args);
        if (menu.start()) {
            menu.mainMenu(args);
        }
        menu.exit();
    }

    public void exit() {

        try {
            // close database connection
            connection.commit();
            connection.close();
            System.out.println("Database connection closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String SHA256(String message) throws NoSuchAlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(message.getBytes());
            String hash = Base64.getEncoder().encodeToString(bytes);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            throw e;
        }
    }

    public boolean start() throws NoSuchAlgorithmException {
        System.out.println("\n--- Welcome to World Cup Stats ---");
        System.out.println("Please login in or sign up to continue:");
        while (true) {
            System.out.println(
                    "Please select an option: \n" +
                            "  1) Log into an administrator account \n" +
                            "  0) Quit \n");
            int selection = input.nextInt();
            input.nextLine();
            switch (selection) {
                case 1:
                    Console console = System.console();
                    System.out.println("Username:");
                    String username = input.nextLine().trim();
                    System.out.println("Password: (Press 'Enter' to confirm your input)");
                    String password = String.valueOf(console.readPassword());
                    try {
                        this.login(username, password);
                        System.out.println("Successfully signed into your account. \nBringing you to the main menu...");
                        return true;
                    } catch (Exception e) {
                        System.out.println("Login failed.\n");
                        continue;
                    }
                case 0:
                    return false;
                default:
                    System.out.println("Invalid selection. \n");
                    continue;
            }
        }
    }

    private void login(String username, String password) throws Exception {
        String passwordHash = SHA256(password);
        String cmd = "SELECT * FROM administrator WHERE username = ? AND passwordHash = ?";
        PreparedStatement statement = connection.prepareStatement(cmd);
        statement.setString(1, username);
        statement.setString(2, passwordHash);
        try {
            ResultSet RS = statement.executeQuery();
            RS.next();
            RS.getString(1);
            connection.commit();
            statement.close();
        } catch (SQLException e) {
            connection.commit();
            statement.close();
            throw new Exception("Incorrect username or password.\n");
        }
    }

    public void mainMenu(String[] args) throws SQLException {

        mainMenu: while (true) {

            System.out.println("\n-- Actions --");
            System.out.println(
                    "Select an option: \n" +
                            " 1) Add a World Cup \n" +
                            " 2) Update a country's name \n" +
                            " 3) Delete a player\n" +
                            " 0) Exit\n");
            int selection = input.nextInt();
            input.nextLine();

            switch (selection) {
                case 1:
                    System.out.println(
                            "Please provide World Cup info (year, host_country, champion, runner_up, third_place, fourth_place, attendance), separated by a space");
                    String worldCupInfo = input.nextLine().trim();
                    this.addWorldCup(worldCupInfo);
                    break;
                case 2:
                    System.out.println("Please provide the initial of the country you want to update");
                    String initial = input.nextLine().trim();
                    System.out.println("Please provide the name of the country you want to set");
                    String name = input.nextLine().trim();
                    this.updateCountryName(initial, name);
                    break;
                case 3:
                    System.out.println("Please provide the nationality of the player you want to delete:");
                    String country = input.nextLine().trim();
                    System.out.println("Please provide the name of the player you want to delete:");
                    name = input.nextLine().trim();
                    this.deletePlayer(country, name);
                    break;
                case 0:
                    System.out.println("Returning...\n");
                    break mainMenu;
                default:
                    System.out.println("Invalid selection.");
                    break;
            }
        }
    }

    private void addWorldCup(String worldCupInfo) throws SQLException {
        List<String> worldCupInfoFields = Arrays.asList(worldCupInfo.split(" "));
        String cmd = "INSERT INTO worldCup VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(cmd);
        for (int i = 1; i <= worldCupInfoFields.size(); i++) {
            statement.setString(i, worldCupInfoFields.get(i - 1));
        }
        try {
            int numRowsAffected = statement.executeUpdate();
            connection.commit();
            statement.close();
            System.out.println("Success. Number of rows affected: " + Integer.toString(numRowsAffected));
        } catch (SQLException e) {
            connection.commit();
            statement.close();
            System.out.println("Failed to add a new World Cup: invalid input.");
        }
    }

    private void updateCountryName(String initial, String name) throws SQLException {
        String cmd = "UPDATE country SET country_name = ? WHERE country_initial = ?";
        PreparedStatement statement = connection.prepareStatement(cmd);
        statement.setString(1, name);
        statement.setString(2, initial);
        int numRowsAffected = statement.executeUpdate();
        connection.commit();
        statement.close();
        if (numRowsAffected != 0) {
            System.out.println("Success. Number of rows affected: " + Integer.toString(numRowsAffected));
        } else {
            System.out.println("Failed to update country name: no country with initial " + initial + " found.");
        }
    }

    private void deletePlayer(String country, String name) throws SQLException {
        String cmd = "DELETE FROM player WHERE player_nationality = (SELECT country_initial FROM country WHERE LOWER(country_name) = ?) AND LOWER(player_name) = ?";
        PreparedStatement statement = connection.prepareStatement(cmd);
        statement.setString(1, country.toLowerCase());
        statement.setString(2, name.toLowerCase());
        int numRowsAffected = statement.executeUpdate();
        connection.commit();
        statement.close();
        if (numRowsAffected != 0) {
            System.out.println("Success. Number of rows affected: " + Integer.toString(numRowsAffected));
        } else {
            System.out.println(
                    "Failed to delete player: No country with name " + country + " or player with name " + name
                            + " found in this country");
        }
    }

}
