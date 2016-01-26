package com.tsykul.crawler.demo.message

import akka.actor.ActorRef
import spray.http.HttpResponse

case class UrlContents(url: Url, httpResponse: HttpResponse, requester: ActorRef)
