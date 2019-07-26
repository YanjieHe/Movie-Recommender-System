package dao

import scala.annotation.tailrec
import java.sql.DriverManager
import java.sql.Connection
import java.sql.ResultSet

import model.Movie

class MovieResourceHandler extends ResourceHandler {
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
        "Poster_Link"
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
              col("Start_Year") match {
                case null => None
                case s    => Some(s.toInt)
              },
              col("Runtime_Minutes").toInt,
              col("Poster_Link") match {
                case null => None
                case link => Some(link)
              }
            )
          }
        ).head
      )
    } catch {
      case e: Throwable => e.printStackTrace
    }
    connection.close()
    result
  }
}
