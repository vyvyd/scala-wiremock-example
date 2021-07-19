import clients.SttpBasedApiClient
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import models.ApiSuccess
import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import sttp.client3._

class ApiClientTest2
    extends AnyFlatSpec
    with Matchers
    with EitherValues {

  private val wiremock = new WireMockServer(
    wireMockConfig().port(4000)
  )

  "ApiClient" should "make a successful call" in {
    /*
     * Setup and then start Wiremock
     */
    wiremock.stubFor(
      get("/sayhello")
        .willReturn(
          aResponse()
            .withBody("Hello World")
            .withHeader("Content-Type", "text/plain")
            .withStatus(200))
    )
    wiremock.start()

    /**
      * Assert response from API
      */
    new SttpBasedApiClient(host = "localhost",
                           port = wiremock.port(),
                           HttpURLConnectionBackend())
      .sayHello()
      .value
      .shouldBe(ApiSuccess("Hello World"))

    /**
      * Verify calls and then shutdown Wiremock
      */
    wiremock.verify(getRequestedFor(urlEqualTo("/sayhello")))
    wiremock.stop()
  }

}
