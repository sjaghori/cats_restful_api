package at.technikumwien.http4s

import at.technikumwien.json.CirceJsonCodecs
import at.technikumwien.{Cat, CatService, CommandResult, UserError}
import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class CommandRoutes[F[_] : Sync](service: CatService[F])(implicit H: HttpErrorHandler[F, UserError])
  extends Http4sDsl[F]
    with CirceJsonCodecs {

  val routes: HttpRoutes[F] = H.handle(HttpRoutes.of[F] {
    case req@POST -> Root =>
      for {
        cat <- req.as[Cat]
        i <- service.insert(cat)
        resp <- Ok(CommandResult(i))
      } yield resp

    case req@PUT -> Root / IntVar(id) =>
      for {
        cat <- req.as[Cat]
        i <- service.update(id, cat)
        resp <- Ok(CommandResult(i))
      } yield resp

    case DELETE -> Root / IntVar(id) =>
      for {
        i <- service.delete(id)
        resp <- Ok(CommandResult(i))
      } yield resp
  })
}
