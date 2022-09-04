package tn.esprit.voyage.demo.services.voyage;

import java.util.List;

import tn.esprit.voyage.demo.entities.Voyage;

public interface VoyageService {
	List<Voyage> getMyVoyage(Long idU);

	List<Voyage> getVoyageSimilaire(Long idU);

	Voyage addMyVoyage(Long idU, Voyage v);

	void deleteVoyage(Long idU, Long idV);

	Voyage modifyVoyage(Long idV, Voyage v);

	void addParticipantToVoyage(Long idU, Long idV);

}
