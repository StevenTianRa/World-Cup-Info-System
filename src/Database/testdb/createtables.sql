CREATE TABLE worldCup
   ( 
      year             DECIMAL(4, 0) NOT NULL PRIMARY KEY, 
      host_country     VARCHAR(30) NOT NULL,
      champion         VARCHAR(30) NOT NULL, 
      runner_up        VARCHAR(30) NOT NULL, 
      third_place      VARCHAR(30) NOT NULL, 
      fourth_place     VARCHAR(30) 
   ); 

CREATE TABLE matchDetails
   (  
      match_id            DECIMAL(10, 0) NOT NULL PRIMARY KEY,
      stage               VARCHAR(30) NOT NULL,
      home_name           VARCHAR(30) NOT NULL,
      away_name           VARCHAR(30) NOT NULL,
      home_half_score     DECIMAL(2, 0) NOT NULL,
      away_half_score     DECIMAL(2, 0) NOT NULL,
      stadium             VARCHAR(30) NOT NULL,
      match_date          VARCHAR(30) NOT NULL,
      home_final_score    DECIMAL(2, 0) NOT NULL,
      away_final_score    DECIMAL(2, 0) NOT NULL,
      win_condition       VARCHAR(100),
      year                DECIMAL(4, 0),
      FOREIGN KEY (year) REFERENCES worldCup 
   );

CREATE TABLE goalDetails
   (
      goal_time           VARCHAR(30) NOT NULL,
      player_name         VARCHAR(20) NOT NULL,
      goal_type           VARCHAR(10) NOT NULL,
      match_id            DECIMAL(10, 0) NOT NULL,
      PRIMARY KEY(match_id, goal_time),
      FOREIGN KEY(match_id) REFERENCES matchDetails
   );

CREATE TABLE team
   (
      team_name           VARCHAR(30) NOT NULL PRIMARY KEY,
      coach_name          VARCHAR(30)
   );

CREATE TABLE player
   (
      player_name         VARCHAR(30) NOT NULL PRIMARY KEY
   );

CREATE TABLE enrolledIn 
   (
      year                DECIMAL(4, 0) NOT NULL,
      team_name           VARCHAR(30) NOT NULL,
      player_name         VARCHAR(30) NOT NULL,
      PRIMARY KEY(year, team_name, player_name),
      FOREIGN KEY(team_name) REFERENCES team,
      FOREIGN KEY(player_name) REFERENCES player,
   );

