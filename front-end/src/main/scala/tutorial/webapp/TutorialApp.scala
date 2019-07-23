package tutorial.webapp

import org.querki.jquery._
import org.scalajs.dom.ext.Ajax
import util._
import org.scalajs.dom.ext._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

case class Movie(movieId: Int, movies: List[Int])

object TutorialApp {
  def main(args: Array[String]): Unit = {
    $(() => setupUI())
  }

  def setupUI(): Unit = {
    $("""<button type="button">Click me!</button>""")
      .click(() => addClickedMessage())
      .appendTo($("body"))
    $("body").append("<p>Hello Scala!</p>")
  }

  def addClickedMessage(): Unit = {
    val url = "http://localhost:9000/posts/32"
    val f = Ajax.get(url)
    f.onComplete {
      case Success(xhr) =>
        val json = scalajs.js.JSON.parse(xhr.responseText)
        val movieId = json.movieId.toString

        $("body").append("<p>" + movieId + "</p>")
      case Failure(e) => println(e.toString)
    }
  }
}
