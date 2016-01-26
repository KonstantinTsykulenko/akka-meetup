package com.tsykul.crawler.demo.v3

import java.util.UUID

import akka.actor.{ActorSystem, Props}
import akka.routing.ConsistentHashingRouter.ConsistentHashableEnvelope
import akka.routing.FromConfig
import com.tsykul.crawler.demo.message.Url
import com.tsykul.crawler.demo.v3.actor.CrawlMaster
import com.typesafe.config.ConfigFactory

object BootClient extends App {
  implicit val system = ActorSystem("CrawlerSystem", ConfigFactory.load("v3-client"))

  val workerRouter = system.actorOf(FromConfig.props(Props(classOf[CrawlMaster], null)),
    name = "workerRouter")

  Thread.sleep(5000L)

  for (i <- 1 to 20)
    workerRouter ! ConsistentHashableEnvelope(Url("https://en.wikipedia.org/wiki/Akka_(toolkit)"), UUID.randomUUID())
}
