package tn.esprit.voyage.demo.services.account.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import tn.esprit.voyage.demo.dtos.InscriptionForm;
import tn.esprit.voyage.demo.dtos.UtilisateurDto;
import tn.esprit.voyage.demo.entities.Role;
import tn.esprit.voyage.demo.entities.Utilisateur;
import tn.esprit.voyage.demo.exceptions.EntityNotFoundException;
import tn.esprit.voyage.demo.repositories.RoleRepository;
import tn.esprit.voyage.demo.repositories.UserRepository;
import tn.esprit.voyage.demo.services.account.AccountService;
import tn.esprit.voyage.demo.services.utils.Tools;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	protected static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private Tools tools;

	@Override
	public Utilisateur loadUserByUserName(String userName) {
		return userRepository.findByEmail(userName);

	}

	public Utilisateur creerCompte(InscriptionForm inscriptionForm) {
		if (inscriptionForm.getPassword().equals(inscriptionForm.getValidatePassword())
				&& !isExiste(inscriptionForm.getEmail()) && tools.validatePassword(inscriptionForm.getPassword())
				&& tools.validateEmail(inscriptionForm.getEmail())) {
			Utilisateur user = new Utilisateur();
			user.setPassword(bCryptPasswordEncoder.encode(inscriptionForm.getPassword()));
			user.setNom(inscriptionForm.getNom());
			user.setEmail(inscriptionForm.getEmail());
			user.setPrenom(inscriptionForm.getPrenom());
			user.setTelephone(inscriptionForm.getMobile());
			user.setDateNaissance(inscriptionForm.getDateNaissance());
			user.setActif(inscriptionForm.getActif());
			user.setPhotoBytes(inscriptionForm.getPhoto());
			Set<Role> roles = new HashSet<Role>();
			inscriptionForm.getRoles().forEach(r -> {
				Role role = roleRepository.findByName(r.getName());
				if (role != null) {
					roles.add(role);
				}
			});
			user.setRoles(roles);
			userRepository.save(user);
			return user;
		} else
			throw new IllegalArgumentException("Email incorrect/deja exsistant ou mot de passe incorrect");

	}

	@Override
	public List<Utilisateur> getUsers() {
		return userRepository.findAll();
	}

	@Override
	public Boolean isExiste(String email) {
		return userRepository.findByEmail(email) != null;
	}

	@Override
	public void updatePassword(String userName, String pass) {
		Utilisateur u = loadUserByUserName(userName);
		if (u == null) {
			throw new EntityNotFoundException();
		} else {
			String newPassword = bCryptPasswordEncoder.encode(pass);
			u.setPassword(newPassword);
			userRepository.save(u);
		}
	}

	@Override
	public Utilisateur updateProfile(Long idU, Utilisateur u) {
		Utilisateur oldU = userRepository.findById(idU).get();
		oldU.setNom(u.getNom());
		oldU.setPrenom(u.getPrenom());
		oldU.setTelephone(u.getTelephone());
		oldU.setEmail(u.getEmail());
		oldU.setPhotoBytes(u.getPhotoBytes());
		return userRepository.save(oldU);
	}

	@Override
	public UtilisateurDto convertToDto(Utilisateur utilisateur) {
		UtilisateurDto utilisateurDto = modelMapper.map(utilisateur, UtilisateurDto.class);
		return utilisateurDto;
	}

	@Override
	public Utilisateur getById(Long idU) {
		return userRepository.findById(idU).get();
	}

	@Override
	public void updateActif(Long idU) {
		Utilisateur u = userRepository.findById(idU).get();
		u.setActif(true);
		userRepository.save(u);
	}

	@Override
	public void lockUser(Long idU) {
		Utilisateur u = userRepository.findById(idU).get();
		u.setActif(false);
		userRepository.save(u);
	}

	@Override
	public Utilisateur loadUserByEmailAndIsActif(String userName) {
		return userRepository.findByEmailAndActifTrue(userName);

	}

}
