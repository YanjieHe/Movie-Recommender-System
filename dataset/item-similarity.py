import pandas as pd
import math
import sys
df = pd.read_csv("imdbId_with_ratings.csv", names=["userId", "movieId", "rating"])

movies = df["movieId"].unique().tolist()

table = { }
result = {}
for movie in movies:
    table[movie] = {}
    result[movie] = {}

for (_, userId, movieId, rating) in df.itertuples():
    table[movieId][userId] = rating


def similarity(table, m1, m2):
    r1 = table[m1]
    r2 = table[m2]
    users = []
    for key in r1.keys():
        if key in r2:
            users.append(key)
    s = 0
    for user in users:
        t = (r1[user] - r2[user])
        s = s + t * t
    return math.sqrt(s)

total = sum(range(len(movies)))
count = 0
with open("similarity.csv", "w") as f:
    f.write("Movie_1,Movie_2,Similarity\n")
    for i in range(len(movies)):
        for j in range(i + 1, len(movies)):
            m1 = movies[i]
            m2 = movies[j]
            sim = similarity(table, m1, m2)
            result[m1][m2] = sim
            
            count += 1
            if count % 1000 == 0:
                sys.stdout.write("\r" + str(count / total) + (" " * 10))
                sys.stdout.flush()
    print("\n")