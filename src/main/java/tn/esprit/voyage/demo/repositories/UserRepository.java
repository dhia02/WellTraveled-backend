package tn.esprit.voyage.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.voyage.demo.entities.Utilisateur;

@Repository
public interface UserRepository extends JpaRepository<Utilisateur, Long> {

	public Utilisateur findByEmail(String userName);

	public Utilisateur findByEmailAndActifTrue(String userName);

}
