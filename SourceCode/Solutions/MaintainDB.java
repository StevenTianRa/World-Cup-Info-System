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
                            " 1) Add a world cup \n" +
                            " 2) Update a player's name \n" +
                            " 3) Delete a player\n" +
                            " 0) Exit\n");
            int selection = input.nextInt();
            input.nextLine();

            switch (selection) {
                case 1:
                    System.out.println(
                            "Please provide world cup info (year, host_country, champion, runner_up, third_place, fourth_place, attendance) as a comma-separted string:");
                    String worldCupInfo = input.nextLine();
                    this.addWorldCup(worldCupInfo);
                    break;
                case 2:
                    System.out.println("Please provide the nationality of the player you want to update");
                    String country = input.nextLine();
                    System.out.println("Please provide the old name of the player you want to update");
                    String oldName = input.nextLine();
                    System.out.println("Please provide the new name of the player you want to update");
                    String newName = input.nextLine();
                    this.updatePlayerName(country, oldName, newName);
                    break;
                case 3:
                    System.out.println("Please provide the nationality of the player you want to delete:");
                    country = input.nextLine();
                    System.out.println("Please provide the name of the player you want to delete:");
                    String name = input.nextLine();
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
        String cmd = "SELECT * FROM worldCup WHERE year = ?";
        PreparedStatement statement = connection.prepareStatement(cmd);
        statement.setString(1, worldCupInfoFields.get(0));
        ResultSet RS = statement.executeQuery();
        if (RS.next()) {
            System.out.println("The " + worldCupInfoFields.get(0) + " World Cup is already in the dataset");
            return;
        }
        cmd = "INSERT INTO worldCup VALUES (?, ?, ?, ?, ?, ?, ?)";
        statement = connection.prepareStatement(cmd);
        for (int i = 0; i < 7; ++i) {
            String field = worldCupInfoFields.get(i);
            if (i != 0 && i != 6) {
                field = "'" + field + "'";
            }
            statement.setString(i + 1, field);
        }
        statement.executeUpdate();
        connection.commit();
        statement.close();
        System.out.println("Added world cup with info " + worldCupInfo);
    }

    // TODO: Convert the player_country from name to initials
    private void updatePlayerName(String country, String oldName, String newName) throws SQLException {
        System.out.println("Updating the name of player " + oldName + " from " + country + " to " + newName);
        String cmd = "UPDATE player SET player_name = ? WHERE player_nationality = ? AND player_name = ?";
        PreparedStatement statement = connection.prepareStatement(cmd);
        statement.setString(1, "'" + newName + "'");
        statement.setString(2, "'" + country + "'");
        statement.setString(3, "'" + oldName + "'");
        statement.executeUpdate();
        connection.commit();
        statement.close();
        System.out.println("Updated the name of player " + oldName + " from " + country + " to " + newName);
    }

    // TODO: Convert the player_country from name to initials
    private void deletePlayer(String country, String name) throws SQLException {
        System.out.println("Deleting player " + name + " from " + country);
        String cmd = "DELETE FROM player WHERE player_nationality = ? AND player_name = ?";
        PreparedStatement statement = connection.prepareStatement(cmd);
        statement.setString(1, "'" + country + "'");
        statement.setString(2, "'" + name + "'");
        statement.executeUpdate();
        connection.commit();
        statement.close();
        System.out.println("Deleted player " + name + " from " + country);
    }

}
