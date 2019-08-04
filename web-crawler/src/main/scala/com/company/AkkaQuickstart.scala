package com.company

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import scala.io.Source
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

class Crawler extends Actor {

  def processPosterLink(link: String): String = {
    var index = -1
    for (i <- (0 until link.length - 1).reverse) {
      if (link(i) == '@') {
        if (index == -1) {
          index = i
        }
      }
    }
    if (index == -1) {
      link.split("_")(0) + "jpg"
    } else {
      link.substring(0, index + 1) + ".jpg"
    }
  }
  def receive = {
    case imdbId: Int => {
      val html = download(imdbId)
      val doc: Document = Jsoup.parse(html)
      val overview = doc.select("div.summary_text").text()
      val compressedPosterLink =
        doc.select("div.poster").select("img").attr("src")
      println("compressed poster link = " + compressedPosterLink)
      val posterLink = processPosterLink(compressedPosterLink)
      println(overview)
      println(posterLink)
      DatabaseWriter.update(imdbId, overview, posterLink)
    }
  }
  def download(imdbId: Int): String = {
    var idStr = imdbId.toString()
    while (idStr.length() < 7) {
      idStr = "0" + idStr
    }
    val url = s"https://www.imdb.com/title/tt$idStr/?ref_=fn_al_tt_1"
    println(url)
    Source.fromURL(url).mkString
  }
}

object AkkaQuickstart extends App {

  def getMovieIds(): List[Int] = {
    Source
      .fromFile("ids_to_download.csv")
      .getLines()
      .drop(1)
      .map { line =>
        line.toInt
      }
      .toList
  }

  override def main(args: Array[String]): Unit = {
    // Create the 'helloAkka' actor system
    val system: ActorSystem = ActorSystem("helloAkka")

    val crawler: ActorRef = system.actorOf(Props[Crawler], "webCrawler")

    val movies = getMovieIds()
    movies.par.foreach { movie =>
      crawler ! movie
    }
  }
}
