package otognan;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class FacebookAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider
{
    //private SecurityDao securityDao; 
	
	public FacebookAuthenticationProvider() {
		System.out.println("CREATEING FACEBOOK AUTH");
	}

    @Override
    protected UserDetails retrieveUser(String username,
            UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException
    {
        final String password = authentication.getCredentials().toString();
        System.out.println(username+"===="+password);
        //This line for validating user with database
        boolean isValidUser = true;//UserDAO.INSTANCE.isValidUser(username, password);
        if (isValidUser)
        {
            final List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>() {
				private static final long serialVersionUID = 1L;

			{
                this.add(new SimpleGrantedAuthority("ROLE_USER"));
            }};
            
            //UserDAO.INSTANCE.getAuthoritiesByUser(username);
            //User u=new User(username,password,);
            
            return new User(username, password, true, true, true, true, authorities);
        }
        else
        {
            authentication.setAuthenticated(false);
            throw new BadCredentialsException("Username/Password does not match for " 
                + authentication.getPrincipal());
        }

    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails arg0,
            UsernamePasswordAuthenticationToken arg1)
            throws AuthenticationException {
        // TODO Auto-generated method stub
    }
}