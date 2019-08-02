ALTER TABLE Movies ADD COLUMN IF NOT EXISTS Bayesian_Average_Rating FLOAT NOT NULL;
ALTER TABLE Movies MODIFY Bayesian_Average_Rating FLOAT NOT NULL;
UPDATE Movies, (SELECT IMDB_ID, ((C * M + Avg_Rating * Num_Votes) / (C + Num_Votes)) AS Rating FROM Movies LEFT JOIN (SELECT COUNT(*) AS C, AVG(Avg_Rating) AS M FROM Movies) as T1 ON TRUE) as T2 SET Movies.Bayesian_Average_Rating = T2.Rating WHERE Movies.IMDB_ID = T2.IMDB_ID;
