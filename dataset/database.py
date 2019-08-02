import pandas as pd
import pymysql
import numpy as np

def create_connection():
    connection = pymysql.connect(host='localhost',
                             user='root',
                             password='123456',
                             db='mydb',
                             cursorclass=pymysql.cursors.DictCursor)
    return connection


def insert_data(table_name, df):
    connection = create_connection()
    try:
        items = ", ".join(["%s" for _ in df.columns])
        sql = "INSERT INTO `" + table_name + "` VALUES (" + items + ");"
        for row in df.itertuples():
            line = tuple(map(str, row[1:]))
            insert_line(connection, table_name, sql, line)
    finally:
        connection.close()

def insert_line(connection, table_name, sql, row):
    with connection.cursor() as cursor:
        cursor.execute(sql, row)
    connection.commit()


def download_data(table_name, columns):
    connection = pymysql.connect(host='localhost',
                            user='root',
                            password='123456',
                            db='mydb',
                            cursorclass=pymysql.cursors.DictCursor)
    sql = "SELECT * FROM " + table_name
    cur = connection.cursor(pymysql.cursors.DictCursor)
    cur.execute(sql)
    lines = []
    for row in cur:
        line = []
        for i in range(len(columns)):
            line.append(row[columns[i]])
        lines.append(line)
    connection.close()
    return pd.DataFrame(lines, columns=columns)


def run_sql(sql):
    connection = create_connection()
    try:
        cur = connection.cursor(pymysql.cursors.DictCursor)
        cur.execute(sql)
    finally:
        connection.close()
