package dao

import scala.annotation.tailrec
import java.sql.DriverManager
import java.sql.Connection
import java.sql.ResultSet

import models.Movie

class MovieDao extends DataAccessObject {
  def read(imdbId: Int): Option[Movie] = {

    var connection: Connection = null
    var result: Option[Movie] = None

    try {
      connection = connect()
      val statement = connection.createStatement()
      val columns = List(
        "IMDB_ID",
        "Primary_Title",
        "Original_Title",
        "Avg_Rating",
        "Num_Votes",
        "Start_Year",
        "Runtime_Minutes",
        "Poster_Link",
        "Overview"
      ).mkString(", ")
      val resultSet = statement.executeQuery(
        s"SELECT $columns FROM Movies WHERE IMDB_ID = $imdbId;"
      )
      result = Some(
        collect(
          resultSet,
          resultSet => {
            val col: String => String = resultSet.getString
            Movie(
              col("IMDB_ID").toInt,
              col("Primary_Title"),
              col("Original_Title"),
              col("Avg_Rating").toFloat,
              col("Num_Votes").toInt,
              toOption(col("Start_Year"), year => year.toInt),
              col("Runtime_Minutes").toInt,
              toOption(col("Poster_Link"), link => link),
              toOption(col("Overview"), overview => overview)
            )
          }
        ).head
      )
    } catch {
      case e: Throwable => e.printStackTrace
    }
    if (connection != null) {
      connection.close()
    }
    result
  }

  def getSimilarMovies(imdbId: Int, limit: Int): List[Int] = {
    var connection: Connection = null
    var result: List[Int] = List()
    try {
      connection = connect()
      val statement = connection.createStatement()
      val columns = List(
        "Movie_2"
      ).mkString(", ")
      val resultSet = statement.executeQuery(
        s"SELECT $columns FROM Similiar_Movies WHERE Movie_1 = $imdbId ORDER BY Similarity DESC LIMIT $limit;"
      )
      result = collect(
        resultSet,
        resultSet => {
          val col: String => String = resultSet.getString
          col("Movie_2").toInt
        }
      )
    } catch {
      case e: Throwable => e.printStackTrace
    }
    if (connection != null) {
      connection.close()
    }
    result
  }
}
