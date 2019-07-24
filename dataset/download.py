import os
import argparse
import sys

def download(url):
    os.system("wget " + url)


def decompress_gzip(file_name):
    os.system("gzip -d " + file_name)

def decompress_zip(file_name):
    os.system("unzip " + file_name)


def movie_lens():
    download("http://files.grouplens.org/datasets/movielens/ml-20m.zip")
    decompress_zip("ml-20m.zip")


def imdb():
    download("https://datasets.imdbws.com/name.basics.tsv.gz")
    download("https://datasets.imdbws.com/title.akas.tsv.gz")
    download("https://datasets.imdbws.com/title.basics.tsv.gz")
    download("https://datasets.imdbws.com/title.crew.tsv.gz")
    download("https://datasets.imdbws.com/title.episode.tsv.gz")
    download("https://datasets.imdbws.com/title.principals.tsv.gz")
    download("https://datasets.imdbws.com/title.ratings.tsv.gz")

    decompress_gzip("name.basics.tsv.gz")
    decompress_gzip("title.akas.tsv.gz")
    decompress_gzip("title.basics.tsv.gz")
    decompress_gzip("title.crew.tsv.gz")
    decompress_gzip("title.episode.tsv.gz")
    decompress_gzip("title.principals.tsv.gz")
    decompress_gzip("title.ratings.tsv.gz")

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument('--movie-lens', action='store_true')
    parser.add_argument('--imdb', action='store_true')
    args = parser.parse_args()
    if (not args.movie_lens) and (not args.imdb):
        parser.print_help(sys.stderr)
    else:
        if args.movie_lens:
            movie_lens()
        if args.imdb:
            imdb()
