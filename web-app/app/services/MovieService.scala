package services

import javax.inject._
import models.Movie
import dao.MovieDao

class MovieService @Inject()(movieDao: MovieDao) {
  def read(imdbId: Int): Movie = {
    movieDao.read(imdbId) match {
      case Some(movie) => movie
      case None        => throw new Exception("cannot read item")
    }
  }

  def getSimilarMovies(imdbId: Int, limit: Int): List[Movie] = {
    val idList = movieDao.getSimilarMovies(imdbId, limit)
    idList.map(read _)
  }

  def filterMovies(
      year: String,
      orderBy: String,
      genre: String
  ): List[Movie] = {
    val yearCondition = year match {
      case "Any Year"          => " TRUE "
      case "2010s"             => " (Start_Year >= 2010 AND Start_Year < 2020) "
      case "2000s"             => " (Start_Year >= 2000 AND Start_Year < 2010) "
      case "'90s"              => " (Start_Year >= 1990 AND Start_Year < 2000) "
      case "'80s"              => " (Start_Year >= 1980 AND Start_Year < 1990) "
      case "'70s"              => " (Start_Year >= 1970 AND Start_Year < 1980) "
      case "'60s"              => " (Start_Year >= 1960 AND Start_Year < 1970) "
      case "Earlier than 1960" => " (Start_Year < 1960) "
      case _                   => throw new Exception("wrong format of year range")
    }
    val orderBySql = orderBy match {
      case "Popularity Descending" => " Num_Votes DESC "
      case "Popularity Ascending"  => " Num_Votes ASC "
      case "Rating Descending"     => " Avg_Rating DESC "
      case "Rating Ascending"      => " Avg_Rating ASC "
      case "Title (A-Z)"           => " Primary_Title ASC "
      case "Title (Z-A)"           => " Primary_Title DESC "
      case _                       => throw new Exception("wrong format of order by criteria: " + orderBy)
    }
    val genreCondition = if (genre == "Any Genre") {
      ""
    } else {
      genre
    }
    movieDao.filterMovies(yearCondition, orderBySql, genreCondition)
  }
}
