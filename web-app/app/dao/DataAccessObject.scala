package dao

import scala.annotation.tailrec
import java.sql.DriverManager
import java.sql.Connection
import java.sql.ResultSet

class DataAccessObject {
  protected val driver = "com.mysql.jdbc.Driver"
  protected val url = "jdbc:mysql://localhost/mydb?serverTimezone=UTC"
  protected val username = "root"
  protected val password = "123456"

  protected def connect(): Connection = {
    Class.forName(driver)
    DriverManager.getConnection(url, username, password)
  }

  protected def collect[A](
      resultSet: ResultSet,
      converter: ResultSet => A
  ): List[A] = {
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

  protected def toOption[A](
      string: String,
      converter: String => A
  ): Option[A] = {
    string match {
      case null => None
      case s    => Some(converter(s))
    }
  }
}
