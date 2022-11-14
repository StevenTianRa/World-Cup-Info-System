CREATE TABLE country
   (
      country_name        VARCHAR(30) NOT NULL,
      country_initial     VARCHAR(6) NOT NULL PRIMARY KEY
   );

CREATE TABLE worldCup
   ( 
      year             INTEGER NOT NULL PRIMARY KEY, 
      host_country     VARCHAR(30) NOT NULL,
      champion         VARCHAR(30) NOT NULL, 
      runner_up        VARCHAR(30) NOT NULL, 
      third_place      VARCHAR(30) NOT NULL, 
      fourth_place     VARCHAR(30) NOT NULL,
      attendance       INTEGER NOT NULL,
      FOREIGN KEY (host_country) REFERENCES country(country_initial),
      FOREIGN KEY (champion) REFERENCES country(country_initial),
      FOREIGN KEY (runner_up) REFERENCES country(country_initial),
      FOREIGN KEY (third_place) REFERENCES country(country_initial),
      FOREIGN KEY (fourth_place) REFERENCES country(country_initial)
   ); 

CREATE TABLE matchDetails
   (  
      year                INTEGER NOT NULL,
      match_date          VARCHAR(30) NOT NULL,
      stage               VARCHAR(30) NOT NULL,
      stadium             VARCHAR(100) NOT NULL,
      home_final_score    INTEGER NOT NULL,
      away_final_score    INTEGER NOT NULL,
      win_condition       VARCHAR(100),
      home_half_score     INTEGER NOT NULL,
      away_half_score     INTEGER NOT NULL,
      match_id            DECIMAL(12, 0) NOT NULL PRIMARY KEY,
      home_initial        VARCHAR(6) NOT NULL,
      away_initial        VARCHAR(6) NOT NULL,
      FOREIGN KEY (home_initial) REFERENCES country(country_initial),
      FOREIGN KEY (away_initial) REFERENCES country(country_initial),
      FOREIGN KEY (year) REFERENCES worldCup 
   );

CREATE TABLE player 
   (
      player_nationality  VARCHAR(20) NOT NULL,
      player_name         VARCHAR(40) NOT NULL,
      PRIMARY KEY (player_nationality, player_name),
      FOREIGN KEY (player_nationality) REFERENCES country(country_initial)
   );

CREATE TABLE enrolled
   (
      match_id            DECIMAL(12, 0) NOT NULL,
      player_nationality  VARCHAR(20) NOT NULL,
      coach_name          VARCHAR(50) NOT NULL,
      player_name         VARCHAR(40) NOT NULL,
      PRIMARY KEY (player_name, player_nationality, match_id),
      FOREIGN KEY (player_nationality, player_name) REFERENCES player(player_nationality, player_name),
      FOREIGN KEY (match_id) REFERENCES matchDetails
      
   );
   
