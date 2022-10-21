-- Tests for feature 1,2
SELECT host_country, attendance, champion, runner_up, third_place, fourth_place FROM worldCup WHERE year = 2014;

-- Tests for feature 3
SELECT COUNT(*) FROM worldCup WHERE champion = 'Germany';
SELECT COUNT(*) FROM worldCup WHERE champion = 'Brazil';

-- Tests for feature 4
SELECT champion, count(*) AS times FROM worldCup GROUP BY champion ORDER BY times DESC;

-- Tests for feature 5
SELECT p.player_name, p.player_nationality FROM player AS p, enrolled AS e 
    WHERE p.player_nationality = e.player_nationality AND p.player_name = e.player_name 
GROUP by p.player_name, p.player_nationality 
HAVING COUNT(*) >= 4;

SELECT p.player_name, p.player_nationality FROM player AS p, enrolled AS e 
    WHERE p.player_nationality = e.player_nationality AND p.player_name = e.player_name 
GROUP by p.player_name, p.player_nationality 
HAVING COUNT(*) >= 7;

-- Tests for feature 6
SELECT count(*) FROM 
    ((SELECT * FROM matchDetails AS m1 WHERE m1.home_name = 'Germany'
         AND m1.home_final_score > m1.away_final_score) 
    UNION (SELECT * FROM matchDetails AS m2 WHERE m2.away_name = 'Germany' 
        AND m2.home_final_score < m2.away_final_score));

SELECT count(*) FROM 
    ((SELECT * FROM matchDetails AS m1 WHERE m1.home_name = 'Brazil'
         AND m1.home_final_score > m1.away_final_score) 
    UNION (SELECT * FROM matchDetails AS m2 WHERE m2.away_name = 'Brazil' 
        AND m2.home_final_score < m2.away_final_score));


SELECT count(*) FROM 
    ((SELECT * FROM matchDetails AS m1 WHERE m1.home_name = 'Argentina'
         AND m1.home_final_score > m1.away_final_score) 
    UNION (SELECT * FROM matchDetails AS m2 WHERE m2.away_name = 'Argentina' 
        AND m2.home_final_score < m2.away_final_score));

