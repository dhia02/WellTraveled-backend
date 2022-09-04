package tn.esprit.voyage.demo.dtos;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import lombok.Data;
import tn.esprit.voyage.demo.entities.Role;

@Data
public class UtilisateurDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String nom;
	private String prenom;
	private String email;
	private String telephone;
	private Date dateNaissance;
	private byte[] photoBytes;
	private Boolean actif;
	private Set<Role> roles;
}
