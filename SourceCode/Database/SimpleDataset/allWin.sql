SELECT *
FROM (SELECT *
      FROM worldCup 
      WHERE champion = 'Germany') AS t1
  JOIN
    (SELECT *
      FROM matchDetails
      WHERE home_name = 'Germany' OR away_name = 'Germany') AS t2
  ON t1.year = t2.year;