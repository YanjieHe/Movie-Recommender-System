import pandas as pd

def get_title_id(column):
    result = []
    for item in column:
        result.append(int(item[2:]))
    return result


def ratings_stat(ratings_df):
    movies = set(ratings_df["imdbId"].tolist())
    result = []
    index = 0
    for movie in movies:
        index += 1
        if index % 100 == 0:
            print(index / len(movies))
        sub_df = ratings_df[ratings_df["imdbId"] == movie]
        num_votes = len(sub_df["rating"])
        rating_mean = sub_df["rating"].mean()
        rating_std = sub_df["rating"].std()
        result.append((movie, num_votes, rating_mean, rating_std))
    return pd.DataFrame(result, columns=["imdbId", "num_votes", "rating_mean", "rating_std"])

pd.options.display.max_columns = 9999

ratings_df = pd.read_csv("ml-20m/ratings.csv")
links_df = pd.read_csv("ml-20m/links.csv")
links_df = links_df[["movieId", "imdbId"]]
ratings_df = ratings_df.merge(links_df, left_on="movieId", right_on="movieId", how="inner")
ratings_stat_df = ratings_stat(ratings_df)
ratings_stat_df.to_csv("ratings_stat.csv", index=False)


ratings_stat_df = pd.read_csv("ratings_stat.csv")
titles_df = pd.read_csv("title.basics.tsv", sep='\t')
titles_df["imdbId"] = get_title_id(titles_df["tconst"])
titles_df = titles_df.dropna()

df = titles_df.merge(ratings_stat_df,
    left_on="imdbId",
    right_on="imdbId",
    how="inner")

df.to_csv("movies_data.csv", index=False)