package tn.esprit.voyage.demo.services.account;

import java.util.List;

import tn.esprit.voyage.demo.dtos.InscriptionForm;
import tn.esprit.voyage.demo.dtos.UtilisateurDto;
import tn.esprit.voyage.demo.entities.Utilisateur;

public interface AccountService {

	public Utilisateur loadUserByUserName(String userName);

	public Utilisateur loadUserByEmailAndIsActif(String userName);

	public Utilisateur creerCompte(InscriptionForm inscriptionForm);

	public List<Utilisateur> getUsers();

	public Boolean isExiste(String email);

	public void updatePassword(String userName, String pass);

	public Utilisateur updateProfile(Long idU, Utilisateur u);

	public UtilisateurDto convertToDto(Utilisateur utilisateur);

	public Utilisateur getById(Long idU);

	public void updateActif(Long idU);

	public void lockUser(Long idU);

}
