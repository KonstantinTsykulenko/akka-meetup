package com.tsykul.crawler.demo.v2.actor

import akka.actor.SupervisorStrategy.{Escalate, Stop, Resume, Restart}
import akka.actor._
import com.tsykul.crawler.demo.message.Url
import com.tsykul.crawler.demo.v2.exception.{RandomFailureException, ExecutionFailedException}

import scala.concurrent.duration._

class UrlHandler(val fetcher: ActorRef, val initialUrl: Url, val curDepth: Long) extends Actor with ActorLogging {

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case _: RandomFailureException => Resume
      case _: ExecutionFailedException => Stop
      case _: Exception => Restart
    }

  override def preStart(): Unit = {
    log.info("Got initial {}", initialUrl)

//    if (Math.random() > 0.9) {
//      log.info("Url processing failed {}", initialUrl)
//      throw new ExecutionFailedException
//    }

    fetcher ! initialUrl

//    log.info("Url processed {}", initialUrl)
  }

  override def receive: Receive = {
    case url@Url(link, depth) =>
      log.info("Got {}", url)
      if (curDepth - 1 > 0) {
        log.info("Crawling {}", url)
        context.actorOf(Props(classOf[UrlHandler], fetcher, url, curDepth - 1))
      }
  }
}
