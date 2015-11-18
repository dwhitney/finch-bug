import com.twitter.util.Await

import io.finch._
import io.finch.request._

import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.Http
import com.twitter.finagle.http.exp.Multipart.FileUpload
import com.twitter.server.TwitterServer

object Main extends TwitterServer{

  def dev: Endpoint[com.twitter.io.Buf]  = get("index") {
    Ok(com.twitter.io.Buf.ByteArray.Owned("""
    <html>
    <body>
      <h1>Upload Form</h1>
      <form enctype="multipart/form-data" method="POST" action="/upload">
        <input type="file" name="image"/>
        <input type="hidden" name="query" value="hello, world!"/>
        <input type="submit"/>
      </form>
    </body>
    </html>
    """.getBytes("UTF-8"))).withContentType(Some("text/html"))
  }

  def upload = post(
    "upload" ? (fileUploadOption("image") :: paramOption("query"))
  ){ (upload: Option[FileUpload], query: Option[String]) =>
    println(upload)
    println("Should not be None: " + query)
    Ok(query.toString)
  }

  val api: Service[Request, Response] = (dev :+: upload).toService

  def main(): Unit = {
    val server = Http.server
      .serve(":9000", api)

    onExit { server.close() }

    Await.ready(adminHttpServer)

  }

}
