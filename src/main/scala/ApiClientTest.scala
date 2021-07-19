import clients.SttpBasedApiClient
import models.ApiSuccess
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import sttp.client3.{HttpURLConnectionBackend, Request, Response}
import sttp.model.StatusCode

class ApiClientTest
    extends AnyFlatSpec
    with Matchers
    with MockitoSugar
    with EitherValues {

  "ApiClient" should "make a successful call" in {

    /**
      * Setup mocks
      */
    val mockBackend = mock[HttpURLConnectionBackend]
    val anyRequest = any[Request[Either[String, String], Any]]

    when(mockBackend.send(anyRequest)) thenReturn
      Response(
        body = Right("Hello World"),
        code = StatusCode(200)
      )

    /**
      * Verify API response
      */
    new SttpBasedApiClient(
      host = "localhost",
      port = 0,
      backend = mockBackend
    ).sayHello()
      .value
      .shouldBe(ApiSuccess("Hello World"))
  }
}
