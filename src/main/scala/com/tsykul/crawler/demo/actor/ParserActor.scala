package com.tsykul.crawler.demo.actor

import akka.actor.{Actor, ActorLogging}
import com.tsykul.crawler.demo.message.{UrlContents, Url}
import com.tsykul.crawler.demo.parser.HtmlParser
import spray.http.parser.HttpParser

import scala.util.Try

class ParserActor extends Actor with ActorLogging with HtmlParser {
  override def receive: Receive = {
    case UrlContents(Url(link, depth), resp, requester) =>
      val links = parseHtml(resp)
        .map(l => if (l.matches("^[\\/#].*")) link + l else l)
        .filter(l => Try(new Url(l)).isSuccess)
      log.debug("links: {}", links)
      links.foreach(requester ! Url(_, depth + 1))
  }
}
