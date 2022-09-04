package tn.esprit.voyage.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import tn.esprit.voyage.demo.entities.Voyage;
import tn.esprit.voyage.demo.services.likesDislikes.LikesDislikesService;
import tn.esprit.voyage.demo.services.voyage.VoyageService;

@RestController
@RequestMapping(value = "/voyages")
public class VoyageWS {
	@Autowired
	private VoyageService voyageService;

	@Autowired
	private LikesDislikesService likesDislikesService;
	
	@Operation(summary = "get mes voyages")
	@GetMapping(value = "/{idU}")
	public List<Voyage> getMyVoyage(@PathVariable("idU") Long idU) {
		return voyageService.getMyVoyage(idU);
	}

	@PostMapping(value = "/{idU}")
	public Voyage addMyVoyage(@RequestBody Voyage v, @PathVariable("idU") Long idU) {
		return voyageService.addMyVoyage(idU, v);
	}

	@DeleteMapping("/{idU}/{idV}")
	public void deleteVoyage(@PathVariable("idU") Long idU, @PathVariable("idV") Long idV) {
		voyageService.deleteVoyage(idU, idV);
	}

	@PutMapping(value = "/{idV}")
	public Voyage modifyVoyage(@RequestBody Voyage v, @PathVariable("idV") Long idV) {
		return voyageService.modifyVoyage(idV, v);
	}

	@GetMapping(value = "/similaire/{idU}")
	public List<Voyage> getVoyageSimilaire(@PathVariable("idU") Long idU) {
		return voyageService.getVoyageSimilaire(idU);
	}

	@PostMapping(value = "/addParticipantToVoyage/{idU}/{idV}")
	public void addParticipantToVoyage(@PathVariable("idU") Long idU, @PathVariable("idV") Long idV) {
		voyageService.addParticipantToVoyage(idU, idV);
	}

	@PostMapping(value = "/likesDislikes/{idU}/{idV}/{like}")
	public Voyage likesDislikes(@PathVariable("idU") Long idU, @PathVariable("idV") Long idV,
			@PathVariable("like") Boolean like) {
		return likesDislikesService.likesDislikes(idU, idV, like);
	}

}
