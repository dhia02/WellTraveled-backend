package tn.esprit.voyage.demo.dtos;

import java.util.Date;
import java.util.Set;

import lombok.Data;
import tn.esprit.voyage.demo.entities.Role;

@Data
public class InscriptionForm {

	private String nom;
	private String prenom;
	private String password;
	private String validatePassword;
	private Date dateNaissance;
	private String email;
	private String mobile;
	private String fixe;
	private String fax;
	private byte[] photo;
	private String photoBase64;
	private Set<Role> roles;
	private Boolean actif;
	// TODO
	// private Adresse adresse ;

}