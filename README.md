# World-Cup-Info

Firstly, set up environment by running:
source ~cs348/public/db2profile and source ~db2inst2/sqllib/db2profile

We can use droptables.sql, createtables.sql and populatetables.sql to create and load sample database. These files are located in 
/SourceCode/Database/SampleDataset.

Similarly, droptables.sql, createtables.sql and populatetables.sql which are located in /SourceCode/Database/ProductionDataset can be used to create and load production database.

Application driver QueryDB.java is located at /SourceCode/Solutions. Input commands "java QueryDB.java -d ." to compile.
Then input "java QueryDB" to run application.

Application maintainer MaintainDB.java is located at /SourceCode/Solutions. Input commands "java MaintainDB.java -d ." to compile.
Then input "java MaintainDB" to run application.

For your convenience, you can run scripts /SourceCode/test-sample.sh, /SourceCode/QueryDB_test.sh or 
/SourceCode/MaintainDB_test.sht o create and load sample/production database, and run respective database-driven application.

For required test-sample.sql, test-sample.out,  test-production.sql, test-production.out, we provide a copy 
for each in the git repo.

Current features:
1. Allows users to look up some information about a past World Cup given the year, including the country in which it is hosted, its attendance, also allows users to look up top 4 countries of a past World Cup given the year.
2. Allows users to look up the number of champions obtained by the selected country.
3. Allows users to see the ranking of countries based on the number of champions obtained.
4. Allows users to look up the list of players who have played for more than ? matches in World Cups.
5. Allows users to look up the total number of winning matches (except for penalties) of each country in World Cups.
6. Allows database administrators who wish to add data for a new World Cup.
7. Allows database administrators who wish to update the name of country when a country’s name is incorrect or contains unreadable characters due to the database’s incompatibility with foreign language characters.
8. Allows database administrators to delete a player from the database.
