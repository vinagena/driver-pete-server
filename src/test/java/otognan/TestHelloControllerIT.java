package otognan;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URL;

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
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/*
 * Extra security config that opens access to test controller.
 * Here we extend normal WebSecurityConfig and add allowed url before
 * the more strict rules defined by WebSecurityConfig because spring
 * evaluates antMatchers in the order they are declared.
 * 
 * It doesn't work to extend WebSecurityConfigurerAdapter instead of 
 * main config for some reason.
 * 
 */
@Configuration
@Order(1)  // default order of WebSecurityConfig is 100, so this config has a priority
class TestWebSecurityConfig extends WebSecurityConfig {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/hello_to_google", "/dialog/oauth").permitAll();
        super.configure(http);
    }
    
}


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@WebAppConfiguration
@IntegrationTest({
	"server.port=8443",
	"server.ssl.key-store = src/test/resources/test_keystore",
	"server.ssl.key-store-password = temppwd",
	"fb.login_form_host = localhost:8443",
	"fb.api_host = localhost:8443",
	"fb.client_id=0",
	"fb.secret=0",
	"fb.redirect_uri=https://localhost:8443/signin/facebook"})
public class TestHelloControllerIT {

	@Value("${local.server.port}")
	private int port;
	
	@Value("${fb.login_form_host}")
	private String loginFromHost;
	
	@Value("${fb.api_host}")
	private String apiHost;

	@Value("${fb.redirect_uri}")
	private String redirectUri;
	
	
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
//	
//	@Test
//	public void getHelloToGoogle() throws Exception {
//		//MockRestServiceServer mockServer = MockRestServiceServer.createServer(this.template);
//		//https://issuetracker.springsource.com/browse/STS-3882
//		
//		String url = new URL("https://localhost:" + port + "/hello_to_google").toString();
//		ResponseEntity<String> response = template.getForEntity(url, String.class);
//		assertThat(response.getBody(), equalTo("Greetings from Spring Boot google!"));
//		
//		//mockServer.verify();
//	}
	
	@Test
	public void getSecuredGreetings() throws Exception {
		//MockRestServiceServer mockServer = MockRestServiceServer.createServer(this.template);
		//https://issuetracker.springsource.com/browse/STS-3882
		
		String url = new URL("https://localhost:" + port + "/test_greeting").toString();
		ResponseEntity<String> initialResponse = template.getForEntity(url, String.class);
		
		// Expect redirect to the server login page
		assertTrue(initialResponse.getStatusCode().is3xxRedirection());
		URI loginRedirect = initialResponse.getHeaders().getLocation(); 
		assertThat(loginRedirect.getPath(), equalTo("/auth/facebook"));
		ResponseEntity<String> springLoginResponse = template.getForEntity(loginRedirect.toString(), String.class);
		
		// Expect redirect to the facebook login page 
		assertTrue(springLoginResponse.getStatusCode().is3xxRedirection());
		URI facebookLoginRedirect = springLoginResponse.getHeaders().getLocation();
		assertThat(facebookLoginRedirect.getPath(), equalTo("/dialog/oauth"));
		
		// Lets got to login page now
		System.out.println(facebookLoginRedirect);
		ResponseEntity<String> facebookLoginResponse = template.getForEntity(facebookLoginRedirect.toString(), String.class);
		
		System.out.println(facebookLoginResponse.toString());

		assertThat(facebookLoginResponse.getBody(), equalTo("HY"));
		//assertThat(initial_response.getBody(), equalTo("Greetings from Spring Boot google!"));
		
		//mockServer.verify();
	}
	
}