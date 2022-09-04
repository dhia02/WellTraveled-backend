package tn.esprit.voyage.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.voyage.demo.entities.LikesDislikes;

@Repository
public interface LikesDislikesRepository extends JpaRepository<LikesDislikes, Long> {
	LikesDislikes findByUtilisateurIdAndVoyageId(Long idU, Long idV);
}
