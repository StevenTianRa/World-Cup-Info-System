SELECT count(*) 
FROM ((SELECT * 
        FROM matchDetails AS m1
        WHERE m1.home_name = 'Brazil' AND m1.home_final_score > m1.away_final_score)
    UNION
      (SELECT *
        FROM matchDetails AS m2
        WHERE m2.away_name = 'Brazil' AND m2.home_final_score < m2.away_final_score));
