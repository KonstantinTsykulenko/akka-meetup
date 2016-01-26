package com.tsykul.crawler.demo.actor

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.pattern.pipe
import com.tsykul.crawler.demo.message.{UrlContents, Url}
import spray.client.pipelining._
import spray.http._

import scala.concurrent.Future

class FetcherActor(val parser: ActorRef) extends Actor with ActorLogging {

  import context.dispatcher

  val pipeline: HttpRequest => Future[HttpResponse] = sendReceive

  override def receive: Receive = {
    case Url(link, depth) =>
      log.debug("Fetching {}", link)
      val sanitizedLink = link.replaceAll("\\s+", "")
      val requester = sender
      pipeline(Get(link)).map(UrlContents(Url(link, depth), _, requester)).pipeTo(parser)
  }
}
