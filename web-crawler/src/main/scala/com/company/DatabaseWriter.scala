package com.company

import java.sql.DriverManager
import java.sql.Connection
import java.sql.ResultSet

object DatabaseWriter {
  val driver = "com.mysql.jdbc.Driver"
  val url = "jdbc:mysql://localhost/mydb"
  val username = "root"
  val password = "123456"

  def connect(): Connection = {
    Class.forName(driver)
    DriverManager.getConnection(url, username, password)
  }

  def update(imdbId: Int, overview: String, posterLink: String) = {
    var connection: Connection = null

    val quote = "\""
    val link = quote + posterLink + quote
    val text = quote + overview.replace(quote, "\\\"") + quote

    try {
      connection = connect()
      val statement = connection.createStatement()
      val sql =
        s"UPDATE Movies SET Poster_Link = $link, Overview = $text WHERE IMDB_ID = $imdbId;"
      // println(sql)
      statement.execute(sql)
    } catch {
      case e: Throwable => e.printStackTrace
    }
    if (connection != null) {
      connection.close()
    }
  }
}
