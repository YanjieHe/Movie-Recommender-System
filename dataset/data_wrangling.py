import pandas as pd

pd.options.display.max_columns = 9999

ratings_df = pd.read_csv("ml-20m/ratings.csv")
links_df = pd.read_csv("ml-20m/links.csv")
links_df = links_df[["movieId", "imdbId"]]
df = ratings_df.merge(links_df, left_on="movieId", right_on="movieId", how="left")
