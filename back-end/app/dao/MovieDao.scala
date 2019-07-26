package dao

import javax.inject.{Inject, Provider}
import play.api.MarkerContext
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json._
import scala.annotation.tailrec

import java.sql.DriverManager
import java.sql.Connection
import java.sql.ResultSet

import model.Movie

class MovieResourceHandler {
  def read(imdbId: Int): Option[Movie] = {
    val driver = "com.mysql.jdbc.Driver"
    val url = "jdbc:mysql://localhost/mydb"
    val username = "root"
    val password = "123456"

    var connection: Connection = null
    var result: Option[Movie] = None

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      // create the statement, and run the select query
      val statement = connection.createStatement()
      val resultSet =
        statement.executeQuery(
          s"SELECT IMDB_ID, Primary_Title, Original_Title, Avg_Rating, Num_Votes, Start_Year, Runtime_Minutes, Poster_Link FROM Movies WHERE IMDB_ID = $imdbId;"
        )
      result = Some(
        collect(
          resultSet,
          resultSet => {
            def get(colName: String): String = {
              resultSet.getString(colName)
            }
            Movie(
              get("IMDB_ID").toInt,
              get("Primary_Title"),
              get("Original_Title"),
              get("Avg_Rating").toFloat,
              get("Num_Votes").toInt,
              get("Start_Year") match {
                case null => None
                case s    => Some(s.toInt)
              },
              get("Runtime_Minutes").toInt,
              get("Poster_Link") match {
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

  def collect[A](resultSet: ResultSet, converter: ResultSet => A): List[A] = {
    @tailrec
    def iter(result: List[A]): List[A] = {
      if (resultSet.next()) {
        iter(converter(resultSet) :: result)
      } else {
        result
      }
    }
    iter(Nil).reverse
  }
}
