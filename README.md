Firstly, set up environment by running:
source ~cs348/public/db2profile
source ~db2inst2/sqllib/db2profile

We can use droptables.sql, createtables.sql and populatetables.sql to create and load sample database. These files are located in 
/SourceCode/Database/SampleDataset.

Similarly, droptables.sql, createtables.sql and populatetables.sql which are located in /SourceCode/Database/ProductionDataset can be used to create and load production database.

Application driver QueryDB.java is located at /SourceCode/Solutions. Input commands "java QueryDB.java -d ." to compile.
Then input "java QueryDB" to run application.

For your convenience, you can run scripts test-sample.sh or test-production.sh to create and load sample/production database, and run respective database-driven application.

Current features:
1. Allows users to look up some information about a past World Cup given the year, including the country in which it is hosted, its attendance
2. Allows users to look up top 4 countries of a past World Cup given the year.
3. Allows users to look up the number of champions obtained by the selected country.
4. Allows users to see the ranking of countries based on the number of champions obtained.
5. Allows users to look up the list of players who have played for more than ? matches in World Cups.
6. Allows users to look up the total number of winning matches (except for penalties) of each country in World Cups.