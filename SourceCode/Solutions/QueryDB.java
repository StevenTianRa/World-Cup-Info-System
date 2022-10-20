import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.*;
import java.math.BigDecimal;

// sample input: localhost 50000 database_name username password
public class QueryDB {

    private Scanner input = new Scanner(System.in);
    private Connection connection = null;

    public QueryDB(String[] args) {

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
        QueryDB menu = new QueryDB(args);
        menu.mainMenu();
        menu.exit();
    }

    public void exit() {

        try {
            // close database connection
            connection.close();
            System.out.println("Database connection closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mainMenu() throws SQLException {

        mainMenu: while (true) {

            System.out.println("\n--- Menu ---");
            System.out.println(
                    "Please select an option: \n" +
                            "  1) Look up past World Cup info \n" +
                            "  2) Look up past World Cup results \n" +
                            "  3) Look up number of obtained champions \n" +
                            "  4) Show champion rank \n" +
                            "  5) Show players that have played at least n matches \n" +
                            "  0) Quit \n");
            int selection = input.nextInt();
            input.nextLine();

            switch (selection) {
                case 1:
                    System.out.println("Please provide the year of the World Cup: ");
                    String year = input.nextLine().trim();
                    int yearNum = Integer.parseInt(year);
                    if (yearNum < 1930 || yearNum > 2014 || yearNum % 4 != 2) {
                        System.out.println("There was no World Cup in " + year + " .\n");
                        break;
                    }
                    System.out.println(
                            "Please select the information you want to look up: \n" +
                                    "  1) Host country \n" +
                                    "  2) Attendance");
                    int option = input.nextInt();
                    input.nextLine();
                    this.getWorldCupInfo(year, option);
                    break;
                case 2:
                    System.out.println("Please provide the year of the World Cup: ");
                    year = input.nextLine().trim();
                    yearNum = Integer.parseInt(year);
                    if (yearNum < 1930 || yearNum > 2014 || yearNum % 4 != 2) {
                        System.out.println("There was no World Cup in " + year + ". \n");
                        break;
                    }
                    System.out.println(
                            "Please select the result you want to look up: \n" +
                                    "  1) Champion \n" +
                                    "  2) Runner-up \n" +
                                    "  3) Third place \n" +
                                    "  4) Fourth place \n" +
                                    "  5) All results");
                    option = input.nextInt();
                    input.nextLine();
                    this.getWorldCupResult(year, option);
                    break;
                case 3:
                    System.out.println("Please enter the name of the country: ");
                    String countryName = input.nextLine().trim();
                    this.countChampion(countryName);
                    break;
                case 4:
                    this.championRank();
                    break;
                case 5:
                    System.out.println("Please provide n");
                    String num = input.nextLine().trim();
                    this.filterPlayerByNumMatches(num);
                    break;
                case 0:
                    System.out.println("Returning...\n");
                    break mainMenu;
                default:
                    System.out.println("Invalid selection. \n");
                    break;
            }
        }
    }

    private void championRank() throws SQLException {
        String cmd = "SELECT champion, count(*) FROM worldCup GROUP BY champion ORDER BY 2 DESC";
        PreparedStatement championRankStatement = connection.prepareStatement(cmd);
        ResultSet championRankRS = championRankStatement.executeQuery();
        System.out.println("**Start of Answer**");
        while (championRankRS.next()) {
            System.out.println(championRankRS.getString(1) + " : " + championRankRS.getString(2));
        }
        System.out.println("**End of Answer**");
        connection.commit();
        championRankStatement.close();
    }

    private void countChampion(String countryName) throws SQLException {
        String cmd = "SELECT count(*) FROM worldCup WHERE champion = '" + countryName + "'";
        PreparedStatement countChampionStatement = connection.prepareStatement(cmd);
        ResultSet countChampionRS = countChampionStatement.executeQuery();
        countChampionRS.next();
        int championTime = Integer.parseInt(countChampionRS.getString(1));
        if (championTime != 1) {
            System.out.println(countryName + " won champion " + championTime + " times. \n");
        } else {
            System.out.println(countryName + " won champion " + championTime + " time. \n");
        }
        connection.commit();
        countChampionStatement.close();
    }

    private void getWorldCupInfo(String year, int option) throws SQLException {
        String getHostAndAttendance = "SELECT host_country,attendance FROM worldCup WHERE year = ?";
        PreparedStatement getHostAndAttendanceStatement = connection.prepareStatement(getHostAndAttendance);
        getHostAndAttendanceStatement.setString(1, year);
        ResultSet hostAndAttendanceRS = getHostAndAttendanceStatement.executeQuery();
        hostAndAttendanceRS.next();
        switch (option) {
            case 1:
                System.out.println("The host country of " + year +
                        " World Cup was " + hostAndAttendanceRS.getString(1));
                break;
            case 2:
                System.out.println("The " + year + " World Cup had an attendance of " +
                        hostAndAttendanceRS.getString(2));
                break;
            default:
                break;
        }
        connection.commit();
        getHostAndAttendanceStatement.close();
    }

    private void getWorldCupResult(String year, int option) throws SQLException {
        String getResults = "SELECT champion, runner_up, third_place, fourth_place FROM worldCup WHERE year = ?";
        PreparedStatement getResultsStatement = connection.prepareStatement(getResults);
        getResultsStatement.setString(1, year);
        ResultSet resultsRS = getResultsStatement.executeQuery();
        resultsRS.next();
        switch (option) {
            case 1:
                System.out.println("The " + year + " World Cup's champion was " + resultsRS.getString(1));
                break;
            case 2:
                System.out.println("The " + year + " World Cup's runner-up was " + resultsRS.getString(2));
                break;
            case 3:
                System.out.println("The " + year + " World Cup's third place was " + resultsRS.getString(3));
                break;
            case 4:
                System.out.println("The " + year + " World Cup's third place was " + resultsRS.getString(4));
            case 5:
                System.out.println("The " + year + " World Cup's results were: \n" +
                        "  Winner: " + resultsRS.getString(1) + "\n" +
                        "  Runner-up: " + resultsRS.getString(2) + "\n" +
                        "  Third place: " + resultsRS.getString(3) + "\n" +
                        "  Fourth place: " + resultsRS.getString(4));
            default:
                break;
        }
        connection.commit();
        getResultsStatement.close();
    }

    private void filterPlayerByNumMatches(String num) throws SQLException {
        String getResults = "SELECT p.player_name, p.player_nationality FROM player AS p, enrolled AS e WHERE p.player_nationality = e.player_nationality AND p.player_name = e.player_name GROUP by p.player_name, p.player_nationality HAVING COUNT(*) >= ?";
        PreparedStatement getResultsStatement = connection.prepareStatement(getResults);
        getResultsStatement.setString(1, num);
        ResultSet resultsRS = getResultsStatement.executeQuery();
        int count = 0;
        System.out.println("**Start of Answer**");
        while (resultsRS.next()) {
            if (count == 20) {
                System.out.println("Showing maximum of 20 results");
                break;
            }
            System.out.println(resultsRS.getString(1) + " " + resultsRS.getString(2));
            count++;
        }
        System.out.println("**End of Answer**");
        connection.commit();
        getResultsStatement.close();

    }

}
