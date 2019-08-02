import pandas as pd 
import pymysql

def create_connection():
    connection = pymysql.connect(host='localhost',
                             user='root',
                             password='123456',
                             db='mydb',
                             cursorclass=pymysql.cursors.DictCursor)
    return connection


df = pd.read_csv("poster_links_and_overview.csv")

print(df.head())

def process(text):
    return text.replace("\"", "\\\"")

count = 0

connection = create_connection()

with connection.cursor() as cursor:
    for (_, imdbId, posterLink, overview) in df.itertuples():
        count += 1
        if count % 1000 == 0:
            print(count / len(df))
        sql = 'UPDATE Movies SET Poster_Link = "{1}", Overview = "{2}" WHERE IMDB_ID = {0};'.format(imdbId, posterLink, process(overview))
        cursor.execute(sql)
        connection.commit()

print("done!")
