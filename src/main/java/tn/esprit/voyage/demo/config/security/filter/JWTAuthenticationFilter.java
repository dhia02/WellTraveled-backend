package tn.esprit.voyage.demo.config.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import tn.esprit.voyage.demo.response.authentification.UserInfo;
import tn.esprit.voyage.demo.security.constant.SecurityConstant;
import tn.esprit.voyage.demo.security.utils.JWTUtils;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private Gson json = new Gson();
	private AuthenticationManager authenticationManager;

	/**
	 * @param authenticationManager
	 */
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		super();
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		UserInfo user = null;
		try {
			user = new ObjectMapper().readValue(request.getInputStream(), UserInfo.class);
			return authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
		} catch (IOException e) {

			e.printStackTrace();
			throw new RuntimeException("Request invalide");
		}

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		User springUser = (User) authResult.getPrincipal();
		List<String> roles = new ArrayList<>();
		springUser.getAuthorities().forEach(r -> {
			roles.add(r.getAuthority());
		});
		String token = JWTUtils.encoder(springUser.getUsername(), roles);
		System.out.println(roles);
		response.addHeader("Authorization", SecurityConstant.JWT_TOKEN_PREFIX + token);
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("userName", springUser.getUsername());
		maps.put("roles", roles);
		response.getWriter().write(json.toJson(maps));
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.getWriter().write(HttpServletResponse.SC_FORBIDDEN + ":" + "User /mot de passe invalide");
		response.getWriter().flush();
		response.getWriter().close();
	}

}
