import java.sql.*;
import java.util.*;

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
        menu.mainMenu(args);
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
        System.out.println(worldCupInfoFields.get(0));
        System.out.println(worldCupInfoFields.get(6));
        String cmd = "INSERT INTO worldCup VALUES (";
        for (int i = 0; i < 7; ++i) {
            String field = worldCupInfoFields.get(i);
            if (i == 0) {
                cmd += field + ",";
            } else if (i == 6) {
                cmd += field + ")";
            } else {
                cmd += "'" + field + "'" + ",";
            }
        }
        System.out.println(cmd);
        PreparedStatement statement = connection.prepareStatement(cmd);
        int numRowsAffected = statement.executeUpdate();
        connection.commit();
        statement.close();
        System.out.println("Success. Number of rows affected: " + Integer.toString(numRowsAffected));
    }

    private void updateCountryName(String initial, String name) throws SQLException {
        String cmd = "UPDATE country SET country_name = '" + name + "' WHERE country_initial = '" + initial + "'";
        PreparedStatement statement = connection.prepareStatement(cmd);
        int numRowsAffected = statement.executeUpdate();
        connection.commit();
        statement.close();
        System.out.println("Success. Number of rows affected: " + Integer.toString(numRowsAffected));
    }

    private void deletePlayer(String country, String name) throws SQLException {
        System.out.println("Deleting player " + name + " from " + country);
        String cmd = "DELETE FROM player WHERE player_nationality = (SELECT country_initial FROM country WHERE country_name = '"
                + country + "') AND player_name = '" + name + "'";
        PreparedStatement statement = connection.prepareStatement(cmd);
        int numRowsAffected = statement.executeUpdate();
        connection.commit();
        statement.close();
        System.out.println("Success. Number of rows affected: " + Integer.toString(numRowsAffected));
    }

}
