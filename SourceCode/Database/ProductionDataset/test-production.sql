-- Tests for feature 1
SELECT host_country, attendance, champion, runner_up, third_place, fourth_place  FROM worldCup WHERE year = 1938;
SELECT host_country, attendance, champion, runner_up, third_place, fourth_place  FROM worldCup WHERE year = 1942;
SELECT host_country, attendance, champion, runner_up, third_place, fourth_place  FROM worldCup WHERE year = 1994;
SELECT host_country, attendance, champion, runner_up, third_place, fourth_place  FROM worldCup WHERE year = 1998;
SELECT host_country, attendance, champion, runner_up, third_place, fourth_place  FROM worldCup WHERE year = 2002;
SELECT host_country, attendance, champion, runner_up, third_place, fourth_place  FROM worldCup WHERE year = 2006;
SELECT host_country, attendance, champion, runner_up, third_place, fourth_place  FROM worldCup WHERE year = 2010;
SELECT host_country, attendance, champion, runner_up, third_place, fourth_place  FROM worldCup WHERE year = 2014;

-- Tests for feature 2
SELECT COUNT(*) FROM worldCup WHERE champion = 'Germany';
SELECT COUNT(*) FROM worldCup WHERE champion = 'Brazil';
SELECT COUNT(*) FROM worldCup WHERE champion = 'Italy';
SELECT COUNT(*) FROM worldCup WHERE champion = 'Germany FR';
SELECT COUNT(*) FROM worldCup WHERE champion = 'Spain';
SELECT COUNT(*) FROM worldCup WHERE champion = 'England';
SELECT COUNT(*) FROM worldCup WHERE champion = 'China';
SELECT COUNT(*) FROM worldCup WHERE champion = 'Argentina';
SELECT COUNT(*) FROM worldCup WHERE champion = 'Netherlands';

-- Tests for feature 3
SELECT champion, count(*) AS times FROM worldCup GROUP BY champion ORDER BY times DESC;

-- Tests for feature 4
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