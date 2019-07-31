import pandas as pd 

links_df = pd.read_csv("ml-20m/links.csv")

movies_df = pd.read_csv("ml-20m/movies.csv")

df = movies_df.merge(links_df, left_on="movieId", right_on="movieId", how="inner")

df = df[["imdbId", "genres"]]

genre_list = ["Action", "Adventure", "Animation", "Children", "Comedy", "Crime", "Documentary", "Drama", "Fantasy", "Film-Noir", "Horror", "Musical", "Mystery", "Romance", "Sci-Fi", "Thriller", "War", "Western", "IMAX"]

genre_map = {}

for (i, genre) in enumerate(genre_list):
    genre_map[genre] = i

a_set = set()
for item in df["genres"]:
    for i in item.split('|'):
        if i != "(no genres listed)":
            index = genre_map[i]
            a_set.add(index)

print(a_set)

rows = []
for (_, movieId, genres) in df.itertuples():
    row = [movieId]
    if genres == "(no genres listed)":
        row += [0] * len(genre_list)
    else:
        items = [0] * len(genre_list)
        for genre in genres.split('|'):
            items[genre_map[genre]] = 1
        row += items
    rows.append(row)

res = pd.DataFrame(rows, columns=["imdbId"] + genre_list)
res.to_csv("genres.csv", index=False)

