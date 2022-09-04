package tn.esprit.voyage.demo.config.security;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tn.esprit.voyage.demo.entities.Utilisateur;
import tn.esprit.voyage.demo.services.account.AccountService;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private AccountService accountService;

	/**
	 * Récupération des information du user via son login
	 */
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Utilisateur user = accountService.loadUserByUserName(userName);
		System.out.println(user);
		if (user == null)
			throw new UsernameNotFoundException("Utilisateur inexistant");
		Collection<GrantedAuthority> authorities = new HashSet<>();
		user.getRoles().forEach(r -> {
			authorities.add(new SimpleGrantedAuthority(r.getName()));
		});
		return new User(user.getEmail(), user.getPassword(), authorities);
	}

}
