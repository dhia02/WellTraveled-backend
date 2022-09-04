package tn.esprit.voyage.demo.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "uti_utilisateur")
public class Utilisateur extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "uti_nom", nullable = true)
	private String nom;

	@Column(name = "uti_prenom", nullable = true)
	private String prenom;

	@Column(name = "uti_mail", nullable = false)
	private String email;

	@Column(name = "uti_telephone", nullable = true)
	private String telephone;

	@Column(name = "uti_password", nullable = false)
	private String password;

	// @JsonFormat(pattern = "dd/MM/yyyy")
	@Column(name = "uti_date_naissance", nullable = true)
	private Date dateNaissance;

	@Column(name = "uti_actif")
	private Boolean actif;

	@Lob
	@Column(name = "uti_photo_bytes", columnDefinition = "BLOB", nullable = true)
	private byte[] photoBytes;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_authorities", joinColumns = {
			@JoinColumn(name = "id_utilisateur", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "id_authority", table = "users_authority", referencedColumnName = "id") })
	private Set<Role> roles;

	@ManyToMany(mappedBy = "utilisateurs", fetch = FetchType.LAZY)
	Set<Voyage> voyages;

	@JsonIgnore
	@OneToMany(mappedBy = "utilisateur", fetch = FetchType.EAGER)
	private Set<LikesDislikes> likesDislikes;

}
