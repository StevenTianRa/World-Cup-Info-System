-- Tests for feature 1, 2
SELECT host_country, attendance, champion, runner_up, third_place, fourth_place  FROM worldCup WHERE year = 1938;
SELECT host_country, attendance, champion, runner_up, third_place, fourth_place  FROM worldCup WHERE year = 1942;
SELECT host_country, attendance, champion, runner_up, third_place, fourth_place  FROM worldCup WHERE year = 1994;
SELECT host_country, attendance, champion, runner_up, third_place, fourth_place  FROM worldCup WHERE year = 1998;
SELECT host_country, attendance, champion, runner_up, third_place, fourth_place  FROM worldCup WHERE year = 2002;
SELECT host_country, attendance, champion, runner_up, third_place, fourth_place  FROM worldCup WHERE year = 2006;
SELECT host_country, attendance, champion, runner_up, third_place, fourth_place  FROM worldCup WHERE year = 2010;
SELECT host_country, attendance, champion, runner_up, third_place, fourth_place  FROM worldCup WHERE year = 2014;

-- Tests for feature 3
SELECT COUNT(*) FROM worldCup WHERE champion = 'GER';
SELECT COUNT(*) FROM worldCup WHERE champion = 'BRA';
SELECT COUNT(*) FROM worldCup WHERE champion = 'ITA';
SELECT COUNT(*) FROM worldCup WHERE champion = 'GER';
SELECT COUNT(*) FROM worldCup WHERE champion = 'ESP';
SELECT COUNT(*) FROM worldCup WHERE champion = 'ENG';
SELECT COUNT(*) FROM worldCup WHERE champion = 'CHN';
SELECT COUNT(*) FROM worldCup WHERE champion = 'ARG';
SELECT COUNT(*) FROM worldCup WHERE champion = 'NED';

-- Tests for feature 4
SELECT champion, count(*) AS times FROM worldCup GROUP BY champion ORDER BY times DESC;
SELECT champion, count(*) AS times FROM worldCup GROUP BY champion;

-- Tests for feature 5
SELECT p.player_name, p.player_nationality FROM player AS p, enrolled AS e 
    WHERE p.player_nationality = e.player_nationality AND p.player_name = e.player_name 
GROUP by p.player_name, p.player_nationality 
HAVING COUNT(*) >= 5;

SELECT p.player_name, p.player_nationality FROM player AS p, enrolled AS e 
    WHERE p.player_nationality = e.player_nationality AND p.player_name = e.player_name 
GROUP by p.player_name, p.player_nationality 
HAVING COUNT(*) >= 15;

SELECT p.player_name, p.player_nationality FROM player AS p, enrolled AS e 
    WHERE p.player_nationality = e.player_nationality AND p.player_name = e.player_name 
GROUP by p.player_name, p.player_nationality 
HAVING COUNT(*) >= 20;

SELECT p.player_name, p.player_nationality FROM player AS p, enrolled AS e 
    WHERE p.player_nationality = e.player_nationality AND p.player_name = e.player_name 
GROUP by p.player_name, p.player_nationality 
HAVING COUNT(*) >= 27;

SELECT p.player_name, p.player_nationality FROM player AS p, enrolled AS e 
    WHERE p.player_nationality = e.player_nationality AND p.player_name = e.player_name 
GROUP by p.player_name, p.player_nationality 
HAVING COUNT(*) >= 29;

-- Tests for feature 6
SELECT count(*) FROM 
    ((SELECT * FROM matchDetails AS m1 WHERE m1.home_initial = 'GER'
         AND m1.home_final_score > m1.away_final_score) 
    UNION (SELECT * FROM matchDetails AS m2 WHERE m2.away_initial = 'GER' 
        AND m2.home_final_score < m2.away_final_score));

SELECT count(*) FROM 
    ((SELECT * FROM matchDetails AS m1 WHERE m1.home_initial = 'BRA'
         AND m1.home_final_score > m1.away_final_score) 
    UNION (SELECT * FROM matchDetails AS m2 WHERE m2.away_initial = 'BRA' 
        AND m2.home_final_score < m2.away_final_score));


SELECT count(*) FROM 
    ((SELECT * FROM matchDetails AS m1 WHERE m1.home_initial = 'ARG'
         AND m1.home_final_score > m1.away_final_score) 
    UNION (SELECT * FROM matchDetails AS m2 WHERE m2.away_initial = 'ARG' 
        AND m2.home_final_score < m2.away_final_score));

SELECT count(*) FROM 
    ((SELECT * FROM matchDetails AS m1 WHERE m1.home_initial = 'CHN'
         AND m1.home_final_score > m1.away_final_score) 
    UNION (SELECT * FROM matchDetails AS m2 WHERE m2.away_initial = 'CHN' 
        AND m2.home_final_score < m2.away_final_score));

-- Table for feature 7
INSERT INTO worldCup VALUEs(2011, 'CHN', 'CHN', 'CHN', 'CHN', 'CHN', 1);
INSERT INTO worldCup VALUEs(2022, 'CHN', 'CHN', 'CHN', 'CHN', 'CHN', 130000);

SELECT * FROM worldCup WHERE year = 2011;
SELECT * FROM worldCup WHERE year = 2022;

-- Table for feature 8
UPDATE country SET country_name = 'Chinaaaa' WHERE country_initial = 'CHN';
UPDATE country SET country_name = 'RuiAnTian' WHERE country_initial = 'ARG';

SELECT country_name from country WHERE country_initial = 'CHN';
SELECT country_name from country WHERE country_initial = 'ARG';
