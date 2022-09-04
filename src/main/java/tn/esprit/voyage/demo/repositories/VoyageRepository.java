package tn.esprit.voyage.demo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.voyage.demo.entities.Voyage;
import tn.esprit.voyage.demo.enums.Objet;

@Repository
public interface VoyageRepository extends JpaRepository<Voyage, Long> {
	List<Voyage> findByDateDebutGreaterThanAndDestinationInOrDateDebutGreaterThanAndObjetIn(Date currentDate,
			List<String> mesDestinations, Date currentDate2, List<Objet> mesObjets);
}
