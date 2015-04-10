package otognan;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.URL;
import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({
	"server.port=8443",
	"server.ssl.key-store = src/test/resources/test_keystore",
	"server.ssl.key-store-password = temppwd",
	"fb.client_id=0",
	"fb.secret=0",
	"fb.redirect_uri=null"})
public class TestHelloControllerIT {

	@Value("${local.server.port}")
	private int port;
	
	private URL base;
	private RestTemplate template;
	
	@Before
	public void setUp() throws Exception {
		this.base = new URL("https://localhost:" + port + "/hello");
		
		SSLContextBuilder builder = new SSLContextBuilder();
	    // trust self signed certificate
	    builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
	    SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
	        builder.build(),
	        SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	    final HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(
	        sslConnectionSocketFactory).build();

	    this.template = new TestRestTemplate();
	    this.template.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient) {
	      @Override
	      protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
	        HttpClientContext context = HttpClientContext.create();
	        RequestConfig.Builder builder = RequestConfig.custom()
	            .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
	            .setAuthenticationEnabled(false).setRedirectsEnabled(false)
	            .setConnectTimeout(1000).setConnectionRequestTimeout(1000).setSocketTimeout(1000);
	        context.setRequestConfig(builder.build());
	        return context;
	      }
	    });
	}
	
//	@Test
//	public void getHello() throws Exception {
//		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
//		assertThat(response.getBody(), equalTo("Greetings from Spring Boot!"));
//	}
	
	@Test
	public void getHelloToGoogle() throws Exception {
		//MockRestServiceServer mockServer = MockRestServiceServer.createServer(this.template);
		
		String url = new URL("https://localhost:" + port + "/hello_to_google").toString();
		ResponseEntity<String> response = template.getForEntity(url, String.class);
		assertThat(response.getBody(), equalTo("Greetings from Spring Boot google!"));
		
		//mockServer.verify();
	}
}