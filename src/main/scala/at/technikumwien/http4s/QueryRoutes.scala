package at.technikumwien.http4s

import at.technikumwien.{CatAlg, UserError}
import at.technikumwien.json.CirceJsonCodecs
import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.OptionalQueryParamDecoderMatcher

object OptSort extends OptionalQueryParamDecoderMatcher[String]("sort")
object OptPage extends OptionalQueryParamDecoderMatcher[Int]("page")
object OptPageSize extends OptionalQueryParamDecoderMatcher[Int]("pageSize")

class QueryRoutes[F[_]: Sync](service: CatAlg[F])(implicit H: HttpErrorHandler[F, UserError])
  extends Http4sDsl[F]
    with CirceJsonCodecs {

  val routes: HttpRoutes[F] = H.handle(HttpRoutes.of[F] {
    case GET -> Root / IntVar(id) =>
      val trip = service.select(id)
      trip.flatMap(_.fold(NotFound())(Ok(_)))

    case GET -> Root :? OptSort(sort) +& OptPage(page) +& OptPageSize(pageSize) =>
      service
        .selectAll(page, pageSize, sort)
        .flatMap(Ok(_))

  })
}
