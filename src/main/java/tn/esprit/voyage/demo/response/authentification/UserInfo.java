package tn.esprit.voyage.demo.response.authentification;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import lombok.Data;

@Data
public class UserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2315477333860243557L;

	private Long id;
	private String login;
	private String nom;
	private String prenom;
	private Boolean actif;
	private Set<AuthorityInfo> authorities;
	private String password;
	private Date dateNaissance;
	private byte[] photoBytes;
	private String telephone;
	private String email;

}