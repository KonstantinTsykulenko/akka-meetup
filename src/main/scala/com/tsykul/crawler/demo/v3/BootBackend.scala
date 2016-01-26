package com.tsykul.crawler.demo.v3

import akka.actor.{ActorSystem, Props}
import akka.routing.FromConfig
import com.tsykul.crawler.demo.actor.{FetcherActor, ParserActor}
import com.tsykul.crawler.demo.v3.actor.CrawlMaster
import com.typesafe.config.ConfigFactory

object BootBackend extends App {
  implicit val system = ActorSystem("CrawlerSystem", ConfigFactory.load("v3-backend"))

  val parser = system.actorOf(Props[ParserActor].withRouter(FromConfig), "parsers")
  val fetcher = system.actorOf(Props(classOf[FetcherActor], parser).withRouter(FromConfig), "fetchers")

  val master = system.actorOf(Props(classOf[CrawlMaster], fetcher), "master")
}
