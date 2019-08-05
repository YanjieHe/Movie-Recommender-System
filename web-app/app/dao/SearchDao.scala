package dao

import scala.annotation.tailrec
import java.sql.DriverManager
import java.sql.Connection
import java.sql.ResultSet

import models.People
import models.Movie

class SearchDao extends DataAccessObject {
  def searchMovies(keyword: Int): List[Search] = {

    var connection: Connection = null
    var result: List[Search] = List()

    try {
      connection = connect()
      val statement = connection.createStatement()
      val columns = List(
        "Name_ID",
        "Primary_Name",
        "Birth_Year",
        "Death_Year"
      ).mkString(", ")
      val resultSet = statement.executeQuery(
        s"SELECT IMDB_ID, Primary_Title FROM Movies WHERE Primary_Title LIKE '%$keyword%';"
      )
      result = collect(
        resultSet,
        resultSet => {
          val col: String => String = resultSet.getString
          Search(
            "/movies/" + col("IMDB_ID").toInt,
            col("Primary_Title")
          )
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
