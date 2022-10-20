SELECT *
FROM (SELECT *
      FROM worldCup 
      WHERE champion = 'Spain') AS t1
  JOIN
    (SELECT *
      FROM matchDetails
      WHERE home_name = 'Spain' OR away_name = 'Spain') AS t2
  ON t1.year = t2.year;