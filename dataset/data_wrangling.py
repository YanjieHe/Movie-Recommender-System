import pandas as pd
import database
import ast


pd.options.display.max_columns = 9999


def get_title_id(column):
    result = []
    for item in column:
        result.append(int(item[2:]))
    return result


def process_year(year_column):
    result = []
    for item in year_column:
        if item == "\\N":
            result.append(-1)
        else:
            result.append(int(item))
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


def make_imdb_movies_dataset():
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

# ***************** Movies *****************
def write_into_movies_table():
    df = pd.read_csv("movies_data.csv")
    df = df[["imdbId", "num_votes", "rating_mean", "rating_std",
        "runtimeMinutes", "startYear", "primaryTitle", "originalTitle"]]

    df = df.rename(columns={
        "imdbId" : "IMDB_ID",
        "primaryTitle" : "Primary_Title",
        "originalTitle" : "Original_Title",
        "rating_mean" : "Avg_Rating",
        "startYear" : "Start_Year",
        "num_votes" : "Num_Votes",
        "runtimeMinutes" : "Runtime_Minutes"
    })

    df["Trailer_Link"] = [None] * len(df)
    df["Poster_Link"] = [None] * len(df)
    df["Overview"] = [None] * len(df)

    df = df[[
        "IMDB_ID", "Primary_Title",
        "Original_Title", "Avg_Rating",
        "Num_Votes", "Start_Year",
        "Runtime_Minutes", "Trailer_Link",
        "Poster_Link", "Overview"
    ]]

    df = df[df["Runtime_Minutes"] != "\\N"]
    database.insert_data("Movies", df)
    database.run_sql("UPDATE Movies SET Trailer_Link = NULL, Poster_Link = NULL, Overview = NULL;")


# ***************** People *****************
def write_into_people_table():
    df = pd.read_csv("name.basics.tsv", sep="\t")
    print("successfully load dataset")
    df["Name_ID"] = get_title_id(df["nconst"])
    people_df = df[["Name_ID", "primaryName", "birthYear", "deathYear"]]
    people_df = people_df.rename(columns=
        {
            "primaryName" : "Primary_Name",
            "birthYear"   : "Birth_Year",
            "deathYear"   : "Death_Year"
        }
    )
    people_df["Birth_Year"] = process_year(people_df["Birth_Year"])
    people_df["Death_Year"] = process_year(people_df["Death_Year"])

    print(str(len(df)) + " rows of data to insert")
    database.insert_data("People", people_df)
    database.run_sql("UPDATE People SET Birth_Year = NULL WHERE Birth_Year = -1;")
    database.run_sql("UPDATE People SET Death_Year = NULL WHERE Death_Year = -1;")


# ***************** Crew *****************
def write_into_crew_table():
    df = pd.read_csv("title.crew.tsv", sep="\t")
    df["IMDB_ID"] = get_title_id(df["tconst"])
    df = df[["IMDB_ID", "directors", "writers"]]

    def get_crew_members(df):
        result = []
        for (_, id, directors, writers) in df.itertuples():
            if directors != "\\N":
                director_list = ast.literal_eval(directors)
                for director in director_list:
                    result.append((id, int(director[2:]), "Director"))
            if writers != "\\N":
                writer_list = ast.literal_eval(writers)
                for writer in writer_list:
                    result.append((id, int(writer[2:]), "Writer"))
        return result

    rows = get_crew_members(df)
    del df
    df = pd.DataFrame(result, columns=["IMDB_ID", "Name_ID", "Job"])
    database.insert_data("Crew", df)

# ***************** Principals *****************
def write_into_principals_table():
    df = pd.read_csv("title.principals.tsv", sep="\t")
    print("successfully load dataset")
    df["IMDB_ID"] = get_title_id(df["tconst"])
    df["Name_ID"] = get_title_id(df["nconst"])
    
    del df["tconst"]
    del df["nconst"]
    del df["characters"]

    df = df.rename(columns={
        "ordering" : "Ordering",
        "category" : "Category",
        "job"      : "Job"
    })

    df = df[["IMDB_ID", "Name_ID", "Ordering", "Category", "Job"]]
    print(str(len(df)) + " rows of data to insert")
    database.insert_data("Principals", df)
    database.run_sql("UPDATE Principals SET Job = NULL WHERE Job = \"\\N\";")