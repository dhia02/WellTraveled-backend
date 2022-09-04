package tn.esprit.voyage.demo.services.utils;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import tn.esprit.voyage.demo.entities.Utilisateur;
import tn.esprit.voyage.demo.response.authentification.AuthorityInfo;
import tn.esprit.voyage.demo.response.authentification.UserInfo;

@Service
public class AuthentificationBR {
	protected static Logger logger = LoggerFactory.getLogger(AuthentificationBR.class);

	public static UserInfo getUserInfo(final Utilisateur user) {
		logger.info("Récuparation du user :" + user.getEmail());

		UserInfo userInfo = new UserInfo();
		userInfo.setId(user.getId());
		userInfo.setActif(user.getActif());
		userInfo.setNom(user.getNom());
		userInfo.setPrenom(user.getPrenom());
		userInfo.setLogin(user.getEmail());
		Set<AuthorityInfo> authorities = new HashSet<AuthorityInfo>();
		user.getRoles().forEach(r -> {
			authorities.add(new AuthorityInfo(r.getId(), r.getName()));
		});
		userInfo.setAuthorities(authorities);
		userInfo.setEmail(user.getEmail());
		userInfo.setTelephone(user.getTelephone());
		userInfo.setPhotoBytes(user.getPhotoBytes());
		userInfo.setDateNaissance(user.getDateNaissance());

		return userInfo;

	}

}
