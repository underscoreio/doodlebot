package doodlebot
package endpoint

import com.twitter.io.{Reader, Buf}
import io.finch._

object Static {
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

  val static = index :+: ui
}
