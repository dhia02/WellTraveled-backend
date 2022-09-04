package tn.esprit.voyage.demo.services.likesDislikes;

import tn.esprit.voyage.demo.entities.Voyage;

public interface LikesDislikesService {
	Voyage likesDislikes(Long idU, Long idV, Boolean like);
}
