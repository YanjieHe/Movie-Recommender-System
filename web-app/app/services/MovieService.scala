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
      genre: String,
      page: Int
  ): List[Movie] = {
    val yearCondition = year match {
      case "any year"          => " TRUE "
      case "2010s"             => " (Start_Year >= 2010 AND Start_Year < 2020) "
      case "2000s"             => " (Start_Year >= 2000 AND Start_Year < 2010) "
      case "'90s"              => " (Start_Year >= 1990 AND Start_Year < 2000) "
      case "'80s"              => " (Start_Year >= 1980 AND Start_Year < 1990) "
      case "'70s"              => " (Start_Year >= 1970 AND Start_Year < 1980) "
      case "'60s"              => " (Start_Year >= 1960 AND Start_Year < 1970) "
      case "earlier than 1960" => " (Start_Year < 1960) "
      case _                   => throw new Exception("wrong format of year range: " + year)
    }
    val orderBySql = orderBy match {
      case "popularity descending" => " Num_Votes DESC "
      case "popularity ascending"  => " Num_Votes ASC "
      case "rating descending"     => " Bayesian_Average_Rating DESC "
      case "rating ascending"      => " Bayesian_Average_Rating ASC "
      case "title (a-z)"           => " Primary_Title ASC "
      case "title (z-a)"           => " Primary_Title DESC "
      case _ =>
        throw new Exception("wrong format of order by criteria: " + orderBy)
    }
    val genreCondition = genre match {
      case "any genre"   => ""
      case "action"      => "Action"
      case "adventure"   => "Adventure"
      case "animation"   => "Animation"
      case "children's"  => "Children"
      case "comedy"      => "Comedy"
      case "crime"       => "Crime"
      case "documentary" => "Documentary"
      case "drama"       => "Drama"
      case "fantasy"     => "Fantasy"
      case "film-noir"   => "Film-Noir"
      case "horror"      => "Horror"
      case "musical"     => "Musical"
      case "mystery"     => "Mystery"
      case "romance"     => "Romance"
      case "sci-fi"      => "Sci-Fi"
      case "thriller"    => "Thriller"
      case "war"         => "War"
      case "western"     => "Western"
      case "imax"        => "IMAX"
    }
    val offset = (page - 1) * 20
    val limit = s" $offset, 20"
    movieDao.filterMovies(yearCondition, orderBySql, genreCondition, limit)
  }
}