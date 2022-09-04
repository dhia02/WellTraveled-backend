package tn.esprit.voyage.demo.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import tn.esprit.voyage.demo.config.security.filter.JWTAuthenticationFilter;
import tn.esprit.voyage.demo.config.security.filter.JWTAuthorisationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String[] AUTH_LIST = { "/v2/api-docs", "/configuration/ui", "/swagger-resources",
			"/configuration/security", "/swagger-ui.html", "/webjars/**", "/swagger-ui/**", "/bus/v3/api-docs/**" };
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// Définition des permissions sur les services REST
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests().antMatchers("/login/**").permitAll();
		http.authorizeRequests().antMatchers("/user/inscription/**").permitAll();
		http.authorizeRequests().antMatchers("/v2/**").permitAll();
		http.authorizeRequests().antMatchers("/actuator/**").permitAll();

		http.authorizeRequests().antMatchers("/user/**").permitAll();
		http.authorizeRequests().antMatchers("/voyages/**").hasAuthority("EMPLOYE");

		http.authorizeRequests().antMatchers(AUTH_LIST).authenticated().and().httpBasic().and().csrf().disable();
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilter(new JWTAuthenticationFilter(authenticationManager()));
		http.addFilterBefore(new JWTAuthorisationFilter(), UsernamePasswordAuthenticationFilter.class);

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v3/swagger-ui/**", "/v3/api-docs/**");
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	   @Override
//	   @Bean
//	   public AuthenticationManager authenticationManagerBean() throws Exception {
//	      return super.authenticationManagerBean();
//	   }

}
