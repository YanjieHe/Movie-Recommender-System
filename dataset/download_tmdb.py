import pandas as pd
import json
import requests

df = pd.read_csv("links.csv")

tmdbId_col = df["tmdbId"].dropna()

tmdbId_col = tmdbId_col.astype('int32')


def fetch(tmdbId):
    url = "https://api.themoviedb.org/3/movie/{0}?&api_key=cfe422613b250f702980a3bbf9e90716".format(tmdbId)
    response = requests.get(url)
    json_obj = response.json()
    overview = json_obj["overview"]
    poster_path = json_obj["poster_path"]
    return (tmdbId, overview, "https://image.tmdb.org/t/p/w500" + poster_path)


res = []
for tmdbId in tmdbId_col:
    try:
        res.append(fetch(tmdbId))
        if len(res) % 100 == 0:
            print(len(res) / len(tmdbId_col))
    except Exception as ex:
        print(ex)
        print("error: " + str(tmdbId))

df = pd.DataFrame(res, columns=["tmdbId", "overview", "posterLink"])
df.to_csv("tmdb_data.csv", index=False)
