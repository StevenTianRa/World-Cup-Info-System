CREATE TABLE worldCup
   ( 
      year             DECIMAL(4, 0) NOT NULL PRIMARY KEY, 
      host_country     VARCHAR(30) NOT NULL,
      champion         VARCHAR(30) NOT NULL, 
      runner_up        VARCHAR(30) NOT NULL, 
      third_place      VARCHAR(30) NOT NULL, 
      fourth_place     VARCHAR(30) NOT NULL,
      attendance       DECIMAL(10,0) NOT NULL
   ); 

CREATE TABLE matchDetails
   (  
      year                DECIMAL(4, 0),
      match_date          VARCHAR(30) NOT NULL,
      stage               VARCHAR(30) NOT NULL,
      stadium             VARCHAR(30) NOT NULL,
      home_name           VARCHAR(30) NOT NULL,
      home_final_score    DECIMAL(2, 0) NOT NULL,
      away_final_score    DECIMAL(2, 0) NOT NULL,
      away_name           VARCHAR(30) NOT NULL,
      win_condition       VARCHAR(100),
      home_half_score     DECIMAL(2, 0) NOT NULL,
      away_half_score     DECIMAL(2, 0) NOT NULL,
      match_id            DECIMAL(12, 0) NOT NULL PRIMARY KEY,
      FOREIGN KEY (year) REFERENCES worldCup 
   );

CREATE TABLE player 
   (
      player_nationality  VARCHAR(20) NOT NULL,
      player_name         VARCHAR(15) NOT NULL,
      PRIMARY KEY (player_nationality, player_name) 
   );

CREATE TABLE enrolled
   (
      match_id            DECIMAL(12, 0) NOT NULL,
      player_nationality  VARCHAR(20) NOT NULL,
      coach_name          VARCHAR(50) NOT NULL,
      player_name         VARCHAR(15) NOT NULL,
      
      PRIMARY KEY (player_name, player_nationality, match_id),
      FOREIGN KEY (player_name) REFERENCES player,
      FOREIGN KEY (player_nationality) REFERENCES player,
      FOREIGN KEY (match_id) REFERENCES matchDetails
   );
