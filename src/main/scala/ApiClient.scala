import models.{ApiFailure, ApiSuccess}
import sttp.client3._
import sttp.model.Uri

package object models {
  case class ApiSuccess(
      responseBody: String
  )
  case class ApiFailure(
      status: Int
  )
}

package object clients {

  trait ApiClient {
    def sayHello(): Either[ApiFailure, ApiSuccess]
  }

  class SttpBasedApiClient(
      private val host: String,
      private val port: Int,
      private val backend: SttpBackend[Identity, Any]
  ) extends ApiClient {

    def sayHello(): Either[ApiFailure, ApiSuccess] = {

      val value = basicRequest
        .get(Uri(host, port, Seq("sayhello")))

      val response = value
        .send(backend)

      response.body match {
        case Right(body) => Right(ApiSuccess(body))
        case Left(_)     => Left(ApiFailure(response.code.code))
      }
    }
  }

}
