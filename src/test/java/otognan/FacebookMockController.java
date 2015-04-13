package otognan;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/facebook_mock")
public class FacebookMockController {
	
	private String email;
	
	private int code = 123;
	private int token = 321;
	
	class MeServiceMockResponse {
		private String email;

		public MeServiceMockResponse(String email) {
			this.email = email;
		}
		
		public String getEmail() {
			return this.email;
		}
	};
	
    @RequestMapping("/dialog/oauth")
    public String oauthDialog(
    		@RequestParam("client_id") String id,
    		@RequestParam("redirect_uri") String redirectUri,
    		@RequestParam("scope") String scope
    		) {

    	return "/do_login?redirect_uri=" + redirectUri;
    }

    @RequestMapping("/do_login")
    public String doLoging(
    		@RequestParam("redirect_uri") String redirectUri,
    		@RequestParam("email") String email,
    		HttpServletResponse response
    		) throws IOException
    {
    	this.email = email;
      
		URI uri = URI.create(java.net.URLDecoder.decode(redirectUri, "UTF-8") + "?code=" + this.code);
		HttpGet httpget = new HttpGet(uri);
		CloseableHttpClient httpClient = HttpsUtils.createUnsafeHttpClient();
		HttpResponse callbackResponse = httpClient.execute(httpget);
		Assert.isTrue(callbackResponse.getStatusLine().getStatusCode() == 200);
		String cookie = callbackResponse.getFirstHeader("Set-Cookie").getValue();
		response.addCookie(new Cookie("Set-Cookie", cookie));
		return "OK";
    }
    
    @RequestMapping("/oauth/access_token")
    public String accessToken(
    		@RequestParam("client_id") String clientId,
    		@RequestParam("redirect_uri") String redirectUri,
    		@RequestParam("client_secret") String clientSecret,
    		@RequestParam("code") int tokenCode)
    {
    	Assert.isTrue(tokenCode == this.code);
    	return "access_token=" + this.token + "&expires";
    }
    
    @RequestMapping("/me")
    public MeServiceMockResponse me(
    		@RequestParam("access_token") int accessToken,
    		@RequestParam("redirect_uri") String redirectUri
    		)
    {
    	Assert.isTrue(accessToken == this.token);
    	return new MeServiceMockResponse(this.email);
    }
}