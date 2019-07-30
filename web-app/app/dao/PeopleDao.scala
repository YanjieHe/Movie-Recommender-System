package dao

import scala.annotation.tailrec
import java.sql.DriverManager
import java.sql.Connection
import java.sql.ResultSet

import models.People

class PeopleDao extends DataAccessObject {
  def read(nameId: Int): Option[People] = {

    var connection: Connection = null
    var result: Option[People] = None

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
        s"SELECT $columns FROM People WHERE Name_ID = $nameId;"
      )
      result = Some(
        collect(
          resultSet,
          resultSet => {
            val col: String => String = resultSet.getString
            People(
              col("Name_ID").toInt,
              col("Primary_Name"),
              toOption(col("Birth_Year"), year => year.toInt),
              toOption(col("Death_Year"), year => year.toInt)
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
}
