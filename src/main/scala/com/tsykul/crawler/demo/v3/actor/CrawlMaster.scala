package com.tsykul.crawler.demo.v3.actor

import akka.actor.{ActorRef, Actor, Props}
import com.tsykul.crawler.demo.message.Url

class CrawlMaster(val fetcher: ActorRef) extends Actor {
  override def receive: Receive = {
    case url: Url => context.actorOf(Props(classOf[UrlHandler], fetcher,
      url, 1L))
  }
}
