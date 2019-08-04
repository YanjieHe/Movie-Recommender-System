import pandas as pd

def get_title_id(column):
    result = []
    for item in column:
        result.append(int(item[2:]))
    return result

def make_known_for_titles_dataset(df):
    with open("People_Known_For_Titles.csv", "w") as f:
        f.write("IMDB_ID,Name_ID\n")
        for (_, imdb_id, knownForTitles) in df.itertuples():
            if knownForTitles != "\\N":
                titles = knownForTitles.split(",")
                for title in titles:
                    f.write(str(imdb_id))
                    f.write(",")
                    f.write(title[2:])
                    f.write("\n")


df = pd.read_csv("name.basics.tsv",sep="\t")

df = df[["nconst", "knownForTitles"]]

df["IMDB_ID"] = get_title_id(df["nconst"])
df = df[["IMDB_ID", "knownForTitles"]]

make_known_for_titles_dataset(df)
