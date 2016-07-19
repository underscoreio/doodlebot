package doodlebot
package endpoint

import com.twitter.io.Reader
import io.finch._
import com.twitter.finagle.http.Response

object Static {
  val resourceLoader = this.getClass()

  val index: Endpoint[Response] = get("index") {
    val stream = resourceLoader.getResourceAsStream("/index.html")
    val reader = Reader.fromStream(stream)
    val data = Reader.readAll(reader)

    data.map { buf =>
      val response: Response = new Response.Ok()
      response.content = buf
      response.contentType = "text/html"
      Ok(response)
    }
  }

  val ui: Endpoint[Response] = get("ui" :: strings) { (tail: Seq[String]) =>
    val path = tail.mkString("/","/","")
    println(s"Loading resource $path")
    val stream = resourceLoader.getResourceAsStream(path)
    val reader = Reader.fromStream(stream)
    val data = Reader.readAll(reader)

    data.map { buf =>
      val response: Response = new Response.Ok()
      response.content = buf
      response.contentType = "application/javascript"
      Ok(response)
    }
  }

  val static = index :+: ui
}
