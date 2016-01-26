package com.tsykul.crawler.demo.v3.actor

import akka.actor.SupervisorStrategy.{Escalate, Restart, Stop}
import akka.actor._
import akka.cluster.Cluster
import akka.cluster.ddata.Replicator.{Update, WriteLocal, Changed, Subscribe}
import akka.cluster.ddata.{GSet, GSetKey, DistributedData}
import com.tsykul.crawler.demo.message.Url
import com.tsykul.crawler.demo.v2.exception.{ExecutionFailedException, RandomFailureException}

import scala.concurrent.duration._

class UrlHandler(val fetcher: ActorRef, val initialUrl: Url, val curDepth: Long) extends Actor with ActorLogging {

  val replicator = DistributedData(context.system).replicator
  implicit val node = Cluster(context.system)

  val DataKey = GSetKey[String]("urls")

  replicator ! Subscribe(DataKey, self)

  var urls = Set[String]()

  override def preStart(): Unit = {
    log.info("Got initial {}", initialUrl)

    fetcher ! initialUrl
  }

  override def receive: Receive = {
    case url@Url(link, depth) =>
      log.info("Got {}", url)
      replicator ! Update(DataKey, GSet.empty[String], WriteLocal)(_ + link)

//      if (urls.contains(link))
//        log.info("Got an already processed url: {}", link)

      if (curDepth - 1 > 0) {
        log.info("Crawling {}", url)
        context.actorOf(Props(classOf[UrlHandler], fetcher, url, curDepth - 1))
      }
    case c @ Changed(DataKey) =>
      val data = c.get(DataKey)
      log.info("Current urls: {}", data.elements)

      urls = data.elements
  }
}
