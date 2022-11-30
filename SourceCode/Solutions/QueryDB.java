import java.sql.*;
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        if (menu.start()) {
            menu.mainMenu();
        }
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

    public boolean start() {
        System.out.println("\n--- Welcome to World Cup Stats ---");
        System.out.println("Please login or sign up to continue:");
        System.out.println(
                "Please select an option: \n" +
                        "  1) Log into an existing account \n" +
                        "  2) Create a new account \n" +
                        "  0) Quit \n");
        int selection = input.nextInt();
        input.nextLine();
        switch (selection) {
            case 1:
                System.out.println("Username:");
                String username = input.nextLine().trim();
                System.out.println("Password:");
                String password = input.nextLine().trim();
                try {
                    this.login(username, password);
                } catch (Exception e) {
                    System.out.println("Login failed.\n");
                    return false;
                }
                break;
            case 2:
                System.out.println("Username:");
                username = input.nextLine().trim();
                System.out.println("Password:");
                String password1 = input.nextLine().trim();
                System.out.println("Confirm password:");
                String password2 = input.nextLine().trim();
                if (!password1.equals(password2)) {
                    System.out.println("Passwords do not match.\n");
                    return false;
                }
                try {
                    this.signup(username, password1);
                } catch (Exception e) {
                    System.out.println("Registration failed.\n");
                    return false;
                }
                break;
            case 0:
                return false;
            default:
                return false;
        }
        return true;
    }

    private void login(String username, String password) throws Exception {
        String passwordHash = SHA256(password);
        String cmd = "SELECT * FROM user WHERE username = ? AND passwordHash = ?";
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

    private void signup(String username, String password) throws NoSuchAlgorithmException, SQLException {
        String passwordHash;
        try {
            passwordHash = SHA256(password);
        } catch (NoSuchAlgorithmException e) {
            throw e;
        }
        String cmd = "INSERT INTO user VALUES(?, ?)";
        PreparedStatement statement = connection.prepareStatement(cmd);
        statement.setString(1, username);
        statement.setString(2, passwordHash);
        try {
            statement.executeUpdate();
            connection.commit();
            statement.close();
        } catch (SQLException e) {
            connection.commit();
            statement.close();
            throw e;
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

    public void mainMenu() throws SQLException {

        mainMenu: while (true) {

            System.out.println("\n--- Menu ---");
            System.out.println(
                    "Please select an option: \n" +
                            "  1) Look up past World Cup info \n" +
                            "  2) Look up past World Cup results \n" +
                            "  3) Look up the number of obtained champions \n" +
                            "  4) Show champion rank \n" +
                            "  5) Show players that have played at least n matches \n" +
                            "  6) Show the number of matches of a country won in history \n" +
                            "  0) Quit \n");
            int selection = input.nextInt();
            input.nextLine();

            String year;
            switch (selection) {
                case 1:
                    System.out.println("Please provide the year of the World Cup: ");
                    year = input.nextLine().trim();
                    System.out.println(
                            "Please select the information you want to look up: \n" +
                                    "  1) Host country \n" +
                                    "  2) Attendance");
                    int option = input.nextInt();
                    input.nextLine();
                    try {
                        this.getWorldCupInfo(year, option);
                    } catch (SQLException e) {
                        System.out.println(
                                "Invalid input. Please provide a year number.");
                    }
                    break;
                case 2:
                    System.out.println("Please provide the year of the World Cup: ");
                    year = input.nextLine().trim();
                    System.out.println(
                            "Please select the result you want to look up: \n" +
                                    "  1) Champion \n" +
                                    "  2) Runner-up \n" +
                                    "  3) Third place \n" +
                                    "  4) Fourth place \n" +
                                    "  5) All results");
                    option = input.nextInt();
                    input.nextLine();
                    try {
                        this.getWorldCupResult(year, option);
                    } catch (SQLException e) {
                        System.out.println(
                                "Invalid input. Please provide a year number.");
                    }
                    break;
                case 3:
                    System.out.println("Please enter the name of a country:");
                    String countryName = input.nextLine().trim();
                    this.countChampion(countryName);
                    break;
                case 4:
                    this.championRank();
                    break;
                case 5:
                    System.out.println("Please provide n:");
                    String num = input.nextLine().trim();
                    try {
                        this.filterPlayerByNumMatches(num);
                    } catch (SQLException e) {
                        System.out.println("Invalid input. Please provide a number. ");
                    }
                    break;
                case 6:
                    System.out.println("Please enter the name of a country:");
                    countryName = input.nextLine().trim();
                    this.countWin(countryName);
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

    // find corresponding country initial of country name
    private String findInitial(String countryName) throws SQLException {
        String cmd = "SELECT country_initial FROM country WHERE LOWER(country_name) = ?";
        PreparedStatement findInitialStatement = connection.prepareStatement(cmd);
        findInitialStatement.setString(1, countryName.toLowerCase());
        ResultSet findInitialRS = findInitialStatement.executeQuery();
        if (findInitialRS.next()) {
            String country_initial = findInitialRS.getString(1);
            connection.commit();
            findInitialStatement.close();
            return country_initial;
        } else {
            connection.commit();
            findInitialStatement.close();
            return "";
        }
    }

    // find corresponding country name of country initial
    private String findName(String countryInitial) throws SQLException {
        String cmd = "SELECT country_name FROM country WHERE country_initial = ?";
        PreparedStatement findNameStatement = connection.prepareStatement(cmd);
        findNameStatement.setString(1, countryInitial);
        ResultSet findNameRS = findNameStatement.executeQuery();
        if (findNameRS.next()) {
            String country_name = findNameRS.getString(1);
            connection.commit();
            findNameStatement.close();
            return country_name;
        } else {
            connection.commit();
            findNameStatement.close();
            return "";
        }
    }

    private void countWin(String countryName) throws SQLException {
        String countryInitial = findInitial(countryName);
        if (countryInitial == "") {
            System.out.println("Country does not exist or never participated World Cup");
            return;
        }
        String cmd = "SELECT count(*) FROM" +
                "((SELECT * FROM matchDetails AS m1 WHERE m1.home_initial = ? AND m1.home_final_score > m1.away_final_score)" +
                "UNION (SELECT * FROM matchDetails AS m2 WHERE m2.away_initial = ? AND m2.home_final_score < m2.away_final_score))";
        PreparedStatement countWinStatement = connection.prepareStatement(cmd);
        countWinStatement.setString(1, countryInitial);
        countWinStatement.setString(2, countryInitial);
        ResultSet countWinRS = countWinStatement.executeQuery();
        countWinRS.next();
        int times = Integer.parseInt(countWinRS.getString(1));
        String actualCountryName = findName(countryInitial);
        System.out.println(actualCountryName + " has won " + times + " matches in history.");
        connection.commit();
        countWinStatement.close();
    }

    private void championRank() throws SQLException {
        String cmd = "SELECT champion, count(*) FROM worldCup GROUP BY champion ORDER BY 2 DESC";
        PreparedStatement championRankStatement = connection.prepareStatement(cmd);
        ResultSet championRankRS = championRankStatement.executeQuery();
        System.out.println("**Start of Answer**");
        while (championRankRS.next()) {
            String countryInitial = championRankRS.getString(1);
            String countryName = findName(countryInitial);
            System.out.println(countryName + " : " + championRankRS.getString(2));
        }
        System.out.println("**End of Answer**");
        connection.commit();
        championRankStatement.close();
    }

    private void countChampion(String countryName) throws SQLException {
        
        String countryInitial = findInitial(countryName);
        if (countryInitial == "") {
            System.out.println("Country does not exist or never participated World Cup");
            return;
        }
        String cmd = "SELECT count(*) FROM worldCup WHERE champion = ?";
        PreparedStatement countChampionStatement = connection.prepareStatement(cmd);
        countChampionStatement.setString(1, countryInitial);
        ResultSet countChampionRS = countChampionStatement.executeQuery();
        countChampionRS.next();
        int championTime = Integer.parseInt(countChampionRS.getString(1));
        String actualCountryName = findName(countryInitial);
        if (championTime != 1) {
            System.out.println(actualCountryName + " has won champion " + championTime + " times. \n");
        } else {
            System.out.println(actualCountryName + " has won champion " + championTime + " time. \n");
        }
        connection.commit();
        countChampionStatement.close();
    }

    private void getWorldCupInfo(String year, int option) throws SQLException {
        String getHostAndAttendance = "SELECT host_country,attendance FROM worldCup WHERE year = ?";
        PreparedStatement getHostAndAttendanceStatement = connection.prepareStatement(getHostAndAttendance);
        getHostAndAttendanceStatement.setString(1, year);
        try {
            ResultSet hostAndAttendanceRS = getHostAndAttendanceStatement.executeQuery();
            hostAndAttendanceRS.next();
            String countryInitial = hostAndAttendanceRS.getString(1);
            String countryName = findName(countryInitial);
            switch (option) {
                case 1:
                    System.out.println("The host country of " + year +
                            " World Cup was " + countryName);
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
        } catch (NumberFormatException e) {
            connection.commit();
            getHostAndAttendanceStatement.close();
            throw e;
        } catch (SQLException e) {
            connection.commit();
            getHostAndAttendanceStatement.close();
            throw e;
        }

    }

    private void getWorldCupResult(String year, int option) throws SQLException {
        String getResults = "SELECT champion, runner_up, third_place, fourth_place FROM worldCup WHERE year = ?";
        PreparedStatement getResultsStatement = connection.prepareStatement(getResults);
        getResultsStatement.setString(1, year);
        try {
            ResultSet resultsRS = getResultsStatement.executeQuery();
            resultsRS.next();
            String championInitial = resultsRS.getString(1);
            String championName = findName(championInitial);
            String runnerUpInitial = resultsRS.getString(2);
            String runnerUpName = findName(runnerUpInitial);
            String thirdInitial = resultsRS.getString(3);
            String thirdName = findName(thirdInitial);
            String fourthInitial = resultsRS.getString(4);
            String fourthName = findName(fourthInitial);
            switch (option) {
                case 1:
                    System.out.println("The " + year + " World Cup's champion was " + championName);
                    break;
                case 2:
                    System.out.println("The " + year + " World Cup's runner-up was " + runnerUpName);
                    break;
                case 3:
                    System.out.println("The " + year + " World Cup's third place was " + thirdName);
                    break;
                case 4:
                    System.out.println("The " + year + " World Cup's third place was " + fourthName);
                    break;
                case 5:
                    System.out.println("The " + year + " World Cup's results were: \n" +
                            "  Winner: " + championName + "\n" +
                            "  Runner-up: " + runnerUpName + "\n" +
                            "  Third place: " + thirdName + "\n" +
                            "  Fourth place: " + fourthName);
                default:
                    break;
            }
            connection.commit();
            getResultsStatement.close();
        } catch (NumberFormatException e) {
            connection.commit();
            getResultsStatement.close();
            throw e;
        } catch (SQLException e) {
            connection.commit();
            getResultsStatement.close();
            throw e;
        }
    }

    private void filterPlayerByNumMatches(String num) throws SQLException {
        String getResults = "SELECT p.player_name, p.player_nationality FROM player AS p, enrolled AS e WHERE p.player_nationality = e.player_nationality AND p.player_name = e.player_name GROUP by p.player_name, p.player_nationality HAVING COUNT(*) >= ?";
        PreparedStatement getResultsStatement = connection.prepareStatement(getResults);
        getResultsStatement.setString(1, num);
        try {
            ResultSet resultsRS = getResultsStatement.executeQuery();
            int count = 0;
            System.out.println("**Start of Answer**");
            while (resultsRS.next()) {
                if (count == 20) {
                    System.out.println("Showing maximum of 20 results");
                    break;
                }
                String countryInitial = resultsRS.getString(2);
                String countryName = findName(countryInitial);
                System.out.println(resultsRS.getString(1) + " " + countryName);
                count++;
            }
            System.out.println("**End of Answer**");
            connection.commit();
            getResultsStatement.close();
        } catch (SQLException e) {
            connection.commit();
            getResultsStatement.close();
            throw e;
        }

    }

}
