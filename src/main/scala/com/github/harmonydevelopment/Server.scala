package com.github.harmonydevelopment

import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.circe._
import io.circe.Codec
import io.circe.generic.semiauto.{deriveCodec}
import cats.effect.Concurrent
import cats.syntax.all._
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonOf,jsonEncoderOf}
import fs2.concurrent.Topic

class Server[T[_]: Concurrent](val topic: Topic[T, String]):
  def send(text: String): T[Unit] =
    topic.publish1(text).map(_ => ())
  def receive: T[String] =
    topic.subscribe(1).take(1).compile.onlyOrError

case class Message(text: String)

object Message:
  given Codec[Message] = deriveCodec[Message]
  given [F[_]: Concurrent]: EntityDecoder[F, Message] = jsonOf
  given [F[_]]: EntityEncoder[F, Message] = jsonEncoderOf

object Routes:
  def routes[F[_]: Concurrent](S: Server[F]): HttpRoutes[F] =
    val dsl = Http4sDsl[F]
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "messages" =>
        S.receive.flatMap(str => Ok(Message(str)))
      case req @ POST -> Root / "messages" =>
        for
          msg <- req.as[Message]
          _ <- S.send(msg.text)
          resp <- Ok()
        yield resp
    }
