/**
 * 
 */
package tn.esprit.voyage.demo.config.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;

import tn.esprit.voyage.demo.security.constant.SecurityConstant;
import tn.esprit.voyage.demo.security.utils.JWTUtils;

public class JWTAuthorisationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Headers", SecurityConstant.ACCESS_CONTROL_ALLOW_HEADERS);
		response.addHeader("Access-Control-Expose-Headers", SecurityConstant.ACCESS_CONTROL_EXPOSE_HEADERS);
		response.addHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE, PATCH");

		if (request.getMethod().equals("OPTIONS")) {
			response.setStatus(HttpServletResponse.SC_OK);
//		} else if (request.getRequestURL().indexOf("/login") != -1 || request.getRequestURL().indexOf("/signUp") != -1
//				|| request.getRequestURL().indexOf("/actuator") != -1) {
//			filterChain.doFilter(request, response);
//			return;
		} else {

			String token = request.getHeader("Authorization");

			if (token == null || !token.startsWith(SecurityConstant.JWT_TOKEN_PREFIX)) {
				System.out.println("no token found !!!");
				filterChain.doFilter(request, response);
				return;
			}
			DecodedJWT decodedJWT = JWTUtils.decoder(token);
			// System.out.println("id : "+decodedJWT.getClaim("id").asString());
			List<String> roles = decodedJWT.getClaims().get(SecurityConstant.JWT_CLAIM_ROLES).asList(String.class);
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			roles.forEach(r -> {
				authorities.add(new SimpleGrantedAuthority(r));
			});
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					decodedJWT.getSubject(), null, authorities);
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			// request.setAttribute("username", decodedJWT.getSubject());

			filterChain.doFilter(request, response);
		}
	}

}
