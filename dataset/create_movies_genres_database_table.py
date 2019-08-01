import pandas as pd 

links_df = pd.read_csv("ml-20m/links.csv")

movies_df = pd.read_csv("ml-20m/movies.csv")

df = movies_df.merge(links_df, left_on="movieId", right_on="movieId", how="inner")

df = df[["imdbId", "genres"]]

genre_list = ["Action", "Adventure", "Animation", "Children", "Comedy", "Crime", "Documentary", "Drama", "Fantasy", "Film-Noir", "Horror", "Musical", "Mystery", "Romance", "Sci-Fi", "Thriller", "War", "Western", "IMAX", "(no genres listed)"]

genre_map = {}

for (i, genre) in enumerate(genre_list):
    genre_map[genre] = i

a_set = set()
for item in df["genres"]:
    for i in item.split('|'):
        index = genre_map[i]
        a_set.add(index)

print(a_set)

rows = []
for (_, movieId, genres) in df.itertuples():
    for genre in genres.split('|'):
        if genre != "(no genres listed)":
            rows.append((movieId, genre))

res = pd.DataFrame(rows, columns=["IMDB_ID", "Genre"])
res.to_csv("Genres.csv", index=False)
