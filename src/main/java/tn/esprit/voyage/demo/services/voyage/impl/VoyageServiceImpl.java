package tn.esprit.voyage.demo.services.voyage.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.esprit.voyage.demo.entities.Utilisateur;
import tn.esprit.voyage.demo.entities.Voyage;
import tn.esprit.voyage.demo.enums.Objet;
import tn.esprit.voyage.demo.exceptions.AlreadyPartcipatedException;
import tn.esprit.voyage.demo.exceptions.EntityNotFoundException;
import tn.esprit.voyage.demo.repositories.UserRepository;
import tn.esprit.voyage.demo.repositories.VoyageRepository;
import tn.esprit.voyage.demo.services.account.AccountService;
import tn.esprit.voyage.demo.services.voyage.VoyageService;

@Service
@Transactional
public class VoyageServiceImpl implements VoyageService {
	@Autowired
	private AccountService accountService;
	@Autowired
	private VoyageRepository voyageRepository;
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<Voyage> getMyVoyage(Long idU) {
		Utilisateur u = accountService.getById(idU);
		return new ArrayList<>(u.getVoyages());
	}

	@Override
	public Voyage addMyVoyage(Long idU, Voyage v) {
		Utilisateur u = accountService.getById(idU);
		Set<Utilisateur> utis = new HashSet<>();
		utis.add(u);
		v.setUtilisateurs(utis);
		return voyageRepository.save(v);
	}

	@Override
	public void deleteVoyage(Long idU, Long idV) {
		Utilisateur u = accountService.getById(idU);
		Voyage v = voyageRepository.findById(idV).orElseThrow(() -> new EntityNotFoundException());
		if (v.getParticipants() == 1) {
			voyageRepository.deleteById(idV);
		} else {
			v.setParticipants(v.getParticipants() - 1);
			Set<Utilisateur> mesParticipant = v.getUtilisateurs();
			mesParticipant.remove(u);
			v.setUtilisateurs(mesParticipant);
			userRepository.save(u);
			voyageRepository.save(v);
		}

	}

	@Override
	public Voyage modifyVoyage(Long idV, Voyage v) {
		Voyage oldVoy = voyageRepository.findById(idV).orElseThrow(() -> new EntityNotFoundException());
		oldVoy.setDateDebut(v.getDateDebut());
		oldVoy.setDateFin(v.getDateFin());
		oldVoy.setObjet(v.getObjet());
		oldVoy.setDestination(v.getDestination());
		oldVoy.setNbDisLikes(v.getNbDisLikes());
		oldVoy.setNbLikes(v.getNbLikes());
		oldVoy.setParticipants(v.getParticipants());
		return voyageRepository.save(oldVoy);

	}

	@Override
	public List<Voyage> getVoyageSimilaire(Long idU) {
		Utilisateur u = accountService.getById(idU);
		List<Voyage> mesVoyages = getMyVoyage(idU);
		List<String> mesDestination = mesVoyages.stream().map(Voyage::getDestination).distinct()
				.collect(Collectors.toList());
		List<Objet> mesObjets = mesVoyages.stream().map(Voyage::getObjet).distinct().collect(Collectors.toList());
		List<Voyage> mesVoyagesSimilaire = voyageRepository
				.findByDateDebutGreaterThanAndDestinationInOrDateDebutGreaterThanAndObjetIn(new Date(), mesDestination,
						new Date(), mesObjets);
		return mesVoyagesSimilaire.stream().filter((v) -> !v.getUtilisateurs().contains(u))
				.collect(Collectors.toList());

	}

	@Override
	public void addParticipantToVoyage(Long idU, Long idV) {
		Utilisateur u = accountService.getById(idU);
		Voyage v = voyageRepository.findById(idV).orElseThrow(() -> new EntityNotFoundException());
		Set<Utilisateur> mesParticipants = v.getUtilisateurs();
		if (mesParticipants.contains(u)) {
			throw new AlreadyPartcipatedException();
		} else {
			mesParticipants.add(u);
			v.setUtilisateurs(mesParticipants);
			v.setParticipants(v.getParticipants() + 1);
			voyageRepository.save(v);
		}
	}

}
