package dao

import scala.annotation.tailrec
import java.sql.DriverManager
import java.sql.Connection
import java.sql.ResultSet

import models.Principals

class PrincipalsDao extends DataAccessObject {
  def getPrincipalList(imdbId: Int): List[Principals] = {

    var connection: Connection = null
    var result: List[Principals] = List()

    try {
      connection = connect()
      val statement = connection.createStatement()
      val columns = List(
        "IMDB_ID",
        "Name_ID",
        "Ordering",
        "Category",
        "Job"
      ).mkString(", ")
      val resultSet = statement.executeQuery(
        s"SELECT $columns FROM Movies WHERE IMDB_ID = $imdbId;"
      )
      result = collect(
        resultSet,
        resultSet => {
          val col: String => String = resultSet.getString
          Principals(
            col("IMDB_ID").toInt,
            col("Name_ID").toInt,
            col("Ordering").toInt,
            col("Category"),
            toOption(col("Job"), job => job.toString)
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
