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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class FacebookMockController {
	
	private String email;
	
    @RequestMapping("/hello_to_google")
    public String hello_to_google() {
        return "Greetings from Spring Boot google!";
    }
    
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
    		@RequestParam("email") String email
    		)
    {
    	this.email = email;
      
		URI uri = null;
		try {
			uri = URI.create(java.net.URLDecoder.decode(redirectUri, "UTF-8") + "?code=123");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		HttpGet httpget = new HttpGet(uri);
		//CloseableHttpClient httpClient = HttpClients.createDefault();
		SSLContextBuilder builder = new SSLContextBuilder();
	    try {
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
		} catch (NoSuchAlgorithmException | KeyStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
			        builder.build(),
			        SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(
		            sslsf).build();
			
			try {
				HttpResponse response = httpClient.execute(httpget);
				HttpEntity entity = response.getEntity();
				return httpget.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (KeyManagementException | NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	        
    	return email;
    }
    
    private URI buildURI(String path, List<NameValuePair> params) throws URISyntaxException {
        return new URIBuilder()
                .setScheme("https")
                .setHost("localhost")
                .setPort(8443)
                .setPath(path)
                .addParameters(params)
                .build();
    }
    
}