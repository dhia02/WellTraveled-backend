package tn.esprit.voyage.demo.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import tn.esprit.voyage.demo.enums.Objet;

@Getter
@Setter
@Entity
@Table(name = "voy_voyage")
public class Voyage extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "voy_objet", nullable = false)
	@Enumerated(EnumType.STRING)
	private Objet objet;

	@Column(name = "voy_date_debut", nullable = false)
	private Date dateDebut;

	@Column(name = "voy_date_fin", nullable = false)
	private Date dateFin;

	@Column(name = "voy_participants", nullable = false, columnDefinition = "integer default 1")
	private Integer participants;

	@Column(name = "voy_nb_likes", nullable = false, columnDefinition = "integer default 0")
	private Integer nbLikes;

	@Column(name = "voy_nb_dislikes", nullable = false, columnDefinition = "integer default 0")
	private Integer nbDisLikes;

	@Column(name = "voy_destination", nullable = false)
	private String destination;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "utilisateur_voyage", joinColumns = @JoinColumn(name = "voyage_id"), inverseJoinColumns = @JoinColumn(name = "utilisateur_id"))
	Set<Utilisateur> utilisateurs;

	@JsonIgnore
	@OneToMany(mappedBy = "voyage", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Set<LikesDislikes> likesDislikes;

}
