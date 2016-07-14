package doodlebot

import com.twitter.io.{Reader, Buf}
import com.twitter.finagle.Http
import io.finch._
import io.circe.generic.auto._
import io.finch.circe._

object DoodleBot {

  final case class Authenticated(userName: String, credentials: String)

  val resourceLoader = this.getClass()

  val index: Endpoint[Buf] = get("index") {
    val stream = resourceLoader.getResourceAsStream("/index.html")
    val reader = Reader.fromStream(stream)
    Ok(Reader.readAll(reader)).withContentType(Some("text/html"))
  }

  val ui: Endpoint[Buf] = get("ui" / strings) { (tail: Seq[String]) =>
    val path = tail.mkString("/","/","")
    println(s"Loading resource $path")
    val stream = resourceLoader.getResourceAsStream(path)
    val reader = Reader.fromStream(stream)
    Ok(Reader.readAll(reader)).withContentType(Some("application/javascript"))
  }

  val signup: Endpoint[Authenticated] = post("signup" :: param("email") :: param("userName") :: param("password")) { (email: String, userName: String, password: String) =>
    Ok(Authenticated(userName, "credentials")).withContentType(Some("application/json"))
  }

  val service = index :+: ui :+: signup
 
  val server = Http.serve(":8080", service.toService)
}
