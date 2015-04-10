package otognan;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class FacebookMockController {
    @RequestMapping("/hello_to_google")
    public String hello_to_google() {
        return "Greetings from Spring Boot google!";
    }

}