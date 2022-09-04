package tn.esprit.voyage.demo.services.likesDislikes.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.esprit.voyage.demo.entities.LikesDislikes;
import tn.esprit.voyage.demo.entities.Utilisateur;
import tn.esprit.voyage.demo.entities.Voyage;
import tn.esprit.voyage.demo.exceptions.AlreadyDislikedException;
import tn.esprit.voyage.demo.exceptions.AlreadyLikedException;
import tn.esprit.voyage.demo.exceptions.EntityNotFoundException;
import tn.esprit.voyage.demo.repositories.LikesDislikesRepository;
import tn.esprit.voyage.demo.repositories.VoyageRepository;
import tn.esprit.voyage.demo.services.account.AccountService;
import tn.esprit.voyage.demo.services.likesDislikes.LikesDislikesService;

@Transactional
@Service
public class LikesDislikesServiceImpl implements LikesDislikesService {
	@Autowired
	private LikesDislikesRepository likesDislikesRepository;
	@Autowired
	private AccountService accountService;
	@Autowired
	private VoyageRepository voyageRepository;

	@Override
	public Voyage likesDislikes(Long idU, Long idV, Boolean like) {
		Utilisateur u = accountService.getById(idU);
		Voyage v = voyageRepository.findById(idV).orElseThrow(() -> new EntityNotFoundException());
		LikesDislikes likesDislikes = likesDislikesRepository.findByUtilisateurIdAndVoyageId(idU, idV);
		if (likesDislikes == null) {
			if (like) {
				v.setNbLikes(v.getNbLikes() + 1);
			} else {
				v.setNbDisLikes(v.getNbDisLikes() + 1);
			}
			LikesDislikes newLikesDislikes = new LikesDislikes(u, v, like);
			likesDislikesRepository.save(newLikesDislikes);
		} else {
			if (likesDislikes.getLike() == true) {
				if (like) {
					throw new AlreadyLikedException();
				} else {
					v.setNbDisLikes(v.getNbDisLikes() + 1);
					v.setNbLikes(v.getNbLikes() - 1);
					likesDislikes.setLike(like);
				}
			} else {
				if (!like) {
					throw new AlreadyDislikedException();
				} else {
					likesDislikes.setLike(like);
					v.setNbDisLikes(v.getNbDisLikes() - 1);
					v.setNbLikes(v.getNbLikes() + 1);
				}
			}
			voyageRepository.save(v);
			likesDislikes.setUtilisateur(u);
			likesDislikes.setVoyage(v);
			likesDislikesRepository.save(likesDislikes);
		}
		return v;
	}

}
