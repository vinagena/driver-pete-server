package otognan;

import java.security.Principal;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String index() {
        return "Greetings from Spring Boot!";
    }
    
    @RequestMapping("/username")
    public String username(Principal principal) {
    	String username = "Unknown";
    	if (principal != null) {
    		username = principal.getName();
    	}
        return username;
    }

}