package com.tsykul.crawler.demo.v1.actor

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.tsykul.crawler.demo.message.Url

class CrawlMaster(val fetcher: ActorRef, val maxDepth: Long) extends Actor with ActorLogging {
  override def receive: Receive = {
    case url@Url(link, depth) =>
      log.info("Got {}", url)
      if (depth < maxDepth) {
        log.info("Crawling {}", url)
        fetcher ! url
      }
  }
}
