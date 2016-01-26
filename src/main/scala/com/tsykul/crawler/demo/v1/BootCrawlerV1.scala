package com.tsykul.crawler.demo.v1

import akka.actor.{ActorSystem, Props}
import com.tsykul.crawler.demo.actor.{FetcherActor, ParserActor}
import com.tsykul.crawler.demo.message.Url
import com.tsykul.crawler.demo.v1.actor.CrawlMaster
import com.typesafe.config.ConfigFactory

object BootCrawlerV1 extends App {
  implicit val system = ActorSystem("CrawlerSystem", ConfigFactory.load("v1"))

  val parser = system.actorOf(Props[ParserActor], "parser")
  val fetcher = system.actorOf(Props(classOf[FetcherActor], parser), "fetcher")

  val master = system.actorOf(Props(classOf[CrawlMaster], fetcher, 2L), "master")

  master ! Url("https://en.wikipedia.org/wiki/Akka_(toolkit)")
}
