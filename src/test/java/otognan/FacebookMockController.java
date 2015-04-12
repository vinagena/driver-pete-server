package otognan;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class FacebookMockController {
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
    	System.out.println(id + redirectUri + ' ' + scope);
    	return "HY";
    }

}