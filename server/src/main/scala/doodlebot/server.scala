package doodlebot

import com.twitter.io.{Reader, Buf}
import com.twitter.finagle.Http
import io.finch._
import io.circe.generic.auto._
import io.finch.circe._
import java.io.File

object DoodleBot {

  final case class Authenticated(userName: String, credentials: String)

  val indexFile: Reader = Reader.fromFile(new File("/Users/noel/dev/doodlebot/code/index.html"))

  val index: Endpoint[Buf] = get("index") {
    Ok(Reader.readAll(indexFile)).withContentType(Some("text/html"))
  }

  val ui: Endpoint[Buf] = get("ui" / strings) { (tail: Seq[String]) =>
    val path = tail.mkString("/")
    val file = new File(s"/Users/noel/dev/doodlebot/code/ui/$path")
    val reader = Reader.fromFile(file)

    Ok(Reader.readAll(reader)).withContentType(Some("application/javascript"))
  }

  val signup: Endpoint[Authenticated] = post("signup" :: param("email") :: param("userName") :: param("password")) { (email: String, userName: String, password: String) =>
    Ok(Authenticated(userName, "credentials")).withContentType(Some("application/json"))
  }

  val service = index :+: ui :+: signup
 
  val server = Http.serve(":8080", service.toService)
}
