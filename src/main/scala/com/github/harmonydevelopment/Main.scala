package com.github.harmonydevelopment

import cats.effect.{IO, IOApp, Sync, Async}
import fs2.io.net.Network
import org.http4s.ember.server.EmberServerBuilder
import com.comcast.ip4s.{ipv4,port}
import org.http4s.implicits._
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.Slf4jFactory
import fs2.concurrent.Topic
import cats.syntax.all._
import org.http4s.server.middleware.CORS

object Main extends IOApp.Simple:
  given [F[_]: Sync]: LoggerFactory[F] =
    Slf4jFactory.create[F]

  def serve[F[_]: Async: Network]: F[Nothing] =
    for
      topic <- Topic[F, String]
      service <-
        CORS.policy.withAllowOriginAll(
          Routes.routes[F](Server[F](topic))
        )
      _ <-
        EmberServerBuilder.default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8192")
          .withHttpApp(service.orNotFound)
          .build
          .useForever
    yield ???

  def run: IO[Unit] =
    serve[IO]
