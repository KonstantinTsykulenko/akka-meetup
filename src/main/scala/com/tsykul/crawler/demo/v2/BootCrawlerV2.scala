package com.tsykul.crawler.demo.v2

import akka.actor.{ActorSystem, Props}
import akka.routing.FromConfig
import com.tsykul.crawler.demo.actor.{FetcherActor, ParserActor}
import com.tsykul.crawler.demo.message.Url
import com.tsykul.crawler.demo.v2.actor.UrlHandler
import com.typesafe.config.ConfigFactory

object BootCrawlerV2 extends App {
  implicit val system = ActorSystem("CrawlerSystem", ConfigFactory.load("v2"))

  val parser = system.actorOf(Props[ParserActor].withRouter(FromConfig), "parsers")
  val fetcher = system.actorOf(Props(classOf[FetcherActor], parser).withRouter(FromConfig), "fetchers")

  val master = system.actorOf(Props(classOf[UrlHandler], fetcher,
    Url("https://en.wikipedia.org/wiki/Akka_(toolkit)"), 2L), "handler")
}
