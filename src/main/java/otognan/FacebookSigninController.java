package otognan;

//import org.apache.commons.lang3.StringUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.utils.URIBuilder;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.json.simple.JSONObject;
//import org.json.simple.JSONValue;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextImpl;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//@Controller
//public class FacebookSigninController {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(FacebookSigninController.class);;
//
//    @Value("${fb.client_id}")
//    private String clientId;
//
//    @Value("${fb.secret}")
//    private String clientSecret;
//
//    @Value("${fb.redirect_uri}")
//    private String redirectUri;
//
//    @RequestMapping("/auth/facebook")
//    public String auth() {
//        try {
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("client_id", clientId));
//            params.add(new BasicNameValuePair("redirect_uri", redirectUri));
//            params.add(new BasicNameValuePair("scope", "email"));
//            URI uri = buildURI("https", "facebook.com", "/dialog/oauth", params);
//            LOGGER.info("Facebook auth invoked: " + uri.toString());
//            return "redirect:" + uri.toString();
//        } catch (URISyntaxException e) {
//            LOGGER.error("Facebook auth URI syntax error!");
//        }
//        return "redirect:/index.html?error=true";
//    }
//
//    @RequestMapping("/signin/facebook")
//    public String signIn(@RequestParam("code") String code) throws Exception {
//        LOGGER.info("facebook signin invoked, code: " + code);
//        String redirect = "redirect:/home";
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        try {
//            String token = getFacebookToken(httpClient, code);
//            getFacebookJSON(httpClient, token);
//            redirect = "redirect:/home";
//        }  catch (Exception e) {
//            LOGGER.error("Exception: " + e);
//        } finally {
//            try {
//                httpClient.close();
//            } catch (IOException e) {
//                LOGGER.error("Exception occurs on httpClient.close()");
//            }
//        }
//        System.out.println("DONE signing..");
//        return redirect;
//    }
//
//    private String getFacebookToken(HttpClient httpClient, String code) throws Exception {
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("client_id", clientId));
//        params.add(new BasicNameValuePair("redirect_uri", redirectUri));
//        params.add(new BasicNameValuePair("client_secret", clientSecret));
//        params.add(new BasicNameValuePair("code", code));
//        // create GET request for get access_token
//        HttpGet httpget = new HttpGet(buildURI("https", "graph.facebook.com", "/oauth/access_token", params));
//        HttpResponse response = httpClient.execute(httpget);
//        HttpEntity entity = response.getEntity();
//        return StringUtils.substringBetween(EntityUtils.toString(entity), "access_token=", "&expires");
//    }
//
//    private void getFacebookJSON(HttpClient httpClient, String token) throws Exception {
//        if (StringUtils.isEmpty(token)) {
//            throw new IllegalArgumentException("Facebook access_token empty!");
//        }
//        LOGGER.info("access_token=" + token);
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("access_token", token));
//        params.add(new BasicNameValuePair("redirect_uri", redirectUri));
//        // create GET request for get user info
//        HttpGet httpget = new HttpGet(buildURI("https", "graph.facebook.com", "/me", params));
//        HttpResponse response = httpClient.execute(httpget);
//        HttpEntity entity = response.getEntity();
//        String json = EntityUtils.toString(entity);
//        LOGGER.info(json);
//        JSONObject obj = (JSONObject)JSONValue.parse(json);
//        // Auth in spring security
//        fakeAuth((String)obj.get("email"));
//    }
//
//    private void fakeAuth(String email) {
//        Authentication auth = new UsernamePasswordAuthenticationToken(email, "fb_pwd_fake",
//        		Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
//        org.springframework.security.core.context.SecurityContext sc = new SecurityContextImpl();
//        sc.setAuthentication(auth);
//        org.springframework.security.core.context.SecurityContextHolder.setContext(sc);
//    }
//
//    private URI buildURI(String scheme, String host, String path, List<NameValuePair> params) throws URISyntaxException {
//        return new URIBuilder()
//                .setScheme(scheme)
//                .setHost(host)
//                .setPath(path)
//                .addParameters(params)
//                .build();
//    }
//}
