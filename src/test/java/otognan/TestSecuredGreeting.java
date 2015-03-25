package otognan;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URL;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = {WebSecurityConfig.class, MockServletContext.class})
@ContextConfiguration(classes = {WebSecurityConfig.class, MockServletContext.class})
@WebAppConfiguration
@TestExecutionListeners(listeners={
	ServletTestExecutionListener.class,
	DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class,
	WithSecurityContextTestExecutionListener.class})
public class TestSecuredGreeting {

	@Autowired
    private WebApplicationContext context;
	
	@Autowired
	private Filter springSecurityFilterChain;

    private MockMvc mvc;

    @Before
    public void setup() {
//    	mvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                //.defaultRequest(MockMvcRequestBuilders.get("/").with(testSecurityContext()))
                .addFilters(springSecurityFilterChain)
                .build();
    }
	
	@Test
	@WithMockUser("MyUser")
	public void getUsername() throws Exception {
		
		String authentication = SecurityContextHolder.getContext()
                .getAuthentication().getName();
		
		System.out.println("ABAAPP" + authentication);
		
		mvc.perform(MockMvcRequestBuilders.get("/hello")
				.accept(MediaType.APPLICATION_JSON))
			//.andExpect(MockMvcResultMatchers.redirectedUrl("/messages/123"));
				.andExpect(status().isOk())
//				.andExpect(content().string(equalTo("Unknown")))
				;
	}
}


//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = MockServletContext.class)
//@WebAppConfiguration
//public class TestSecuredGreeting {
//
//	private MockMvc mvc;
//
//	@Before
//	public void setUp() throws Exception {
//		mvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();
//	}
//
//	@Test
//	public void getGreetings() throws Exception {
//		mvc.perform(MockMvcRequestBuilders.get("/test_greeting").accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(content().string(equalTo("Greetings!")));
//	}
//}