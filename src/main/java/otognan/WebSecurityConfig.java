package otognan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/auth/facebook", "/signin/facebook").permitAll()
                .antMatchers("/hello").permitAll()
                .anyRequest().authenticated()
            //    .and()
           // .formLogin()
                //.loginPage("/auth/facebook")
                // uncomment to enable in memory login form
            	//.loginPage("/login")
                //.permitAll()
//                .and()
//            .logout()
//                .permitAll()
            ;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	auth
        .inMemoryAuthentication()
            .withUser("user").password("password").roles("USER");
    }
    
}
