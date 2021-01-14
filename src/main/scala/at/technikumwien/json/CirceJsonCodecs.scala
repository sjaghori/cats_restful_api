package at.technikumwien.json

import at.technikumwien.{Cat, Cats, CommandResult}
import cats.Applicative
import cats.effect.{IO, Sync}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, ObjectEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

trait CirceJsonCodecs {

  implicit val catEncoder: ObjectEncoder[Cat] = deriveEncoder[Cat]
  implicit def catEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Cat] = jsonEncoderOf
  implicit val catDecoder: Decoder[Cat] = deriveDecoder[Cat]
  implicit def catEntityDecoder[F[_]: Sync]: EntityDecoder[F, Cat] = jsonOf

  implicit val catIoEncoder: EntityEncoder[IO, Cat] = jsonEncoderOf[IO, Cat]
  implicit val catIoDecoder: EntityDecoder[IO, Cat] = jsonOf[IO, Cat]

  implicit val catsEncoder: ObjectEncoder[Cats] = deriveEncoder[Cats]
  implicit def catsEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Cats] = jsonEncoderOf
  implicit val catsIoEncoder: EntityEncoder[IO, Cats] = jsonEncoderOf[IO, Cats]

  implicit val commandResultEncoder: ObjectEncoder[CommandResult] = deriveEncoder[CommandResult]
  implicit def commandResultEntityEncoder[F[_]: Applicative]: EntityEncoder[F, CommandResult] = jsonEncoderOf
  implicit val commandResultIoEncoder: EntityEncoder[IO, CommandResult] = jsonEncoderOf[IO, CommandResult]
  
}
