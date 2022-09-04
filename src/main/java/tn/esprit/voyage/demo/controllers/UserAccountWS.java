package tn.esprit.voyage.demo.controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import tn.esprit.voyage.demo.dtos.InscriptionForm;
import tn.esprit.voyage.demo.dtos.UtilisateurDto;
import tn.esprit.voyage.demo.entities.Utilisateur;
import tn.esprit.voyage.demo.response.authentification.UserInfo;
import tn.esprit.voyage.demo.services.account.AccountService;
import tn.esprit.voyage.demo.services.email.EmailService;
import tn.esprit.voyage.demo.services.utils.AuthentificationBR;

@RestController
@RequestMapping(value = "/user")
public class UserAccountWS {

	@Autowired
	private EmailService emailService;
	@Autowired
	ServletContext context;

	@Autowired
	private AccountService accountService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Operation(summary = "Find user by login", description = "Find user by login")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content) })
	

	@GetMapping(value = "/{login}")
	public UserInfo getUserByEmail(@PathVariable("login") String userName) {
		return AuthentificationBR.getUserInfo(accountService.loadUserByUserName(userName));
	}

	@PostMapping(value = "/inscription")
	public Long creerCompte(@RequestParam(name = "file", required = false) MultipartFile file,
			@RequestParam("user") String user) throws JsonParseException, JsonMappingException, Exception {
		InscriptionForm inscriptionForm = new ObjectMapper().readValue(user, InscriptionForm.class);
		if (file != null) {
			inscriptionForm.setPhoto(file.getBytes());
		}
		Utilisateur u = accountService.creerCompte(inscriptionForm);
		return u.getId();
	}

	@Operation(summary = "sign Up and save the user with unlocked status until the verification with his email")
	@PostMapping(value = "/signUp")
	public Long register(@RequestParam(name = "file", required = false) MultipartFile file,
			@RequestParam("user") String user) throws JsonParseException, JsonMappingException, Exception {
		InscriptionForm inscriptionForm = new ObjectMapper().readValue(user, InscriptionForm.class);
		if (file != null) {
			inscriptionForm.setPhoto(file.getBytes());
		}
		Utilisateur u = accountService.creerCompte(inscriptionForm);
		emailService.sendMailwithtemplateRegistration("verification mail pour s'inscrire", "login", u.getEmail(),
				u.getId());
		return u.getId();
	}

	@GetMapping("/users")
	@ResponseBody
	public List<UtilisateurDto> getUsers() {
		List<UtilisateurDto> utilisateurs = accountService.getUsers().stream().map(accountService::convertToDto)
				.collect(Collectors.toList());
		return utilisateurs;
	}

	@PostMapping("/send/{to}")
	public String sendWithTemplate(@PathVariable("to") String to) throws IOException {
		return String.format("\"%s\"", emailService
				.sendMailwithtemplate("verification mail pour réinitialiser du mot de passe", "resetPassword", to));
	}

	@PostMapping("/sendConfirmRegister/{to}")
	public String sendConfirmRegister(@PathVariable("to") String to) throws IOException {
		return String.format("\"%s\"",
				emailService.sendMailwithtemplate("verification mail pour s'inscrire", "login", to));
	}

	@PostMapping("/sendEmployeeInvitation")
	public void sendConfirmRegister(@RequestParam("email") String email, @RequestParam("nom") String nom,
			@RequestParam("prenom") String prenom) throws IOException {
		emailService.sendMailwithtemplateToEmployee("invitation mail pour s'inscrire", "register", email, nom, prenom);
	}

	@PostMapping("/resetPassword/{login}")
	public String updatePassword(@PathVariable("login") String userName, @RequestBody String pass) throws IOException {

		accountService.updatePassword(userName, pass);

		return String.format("\"%s\"", "Mot de pass modifié");
	}

	@PutMapping("/editProfile/{idU}")
	public Utilisateur editProfile(@PathVariable("idU") Long idU, @RequestParam(required = false) MultipartFile file,
			@RequestParam("user") String user) throws JsonParseException, JsonMappingException, Exception {
		Utilisateur u = new ObjectMapper().readValue(user, Utilisateur.class);
		Utilisateur oldUser = accountService.getById(idU);
		if (file != null) {
			System.out.println("file changing ...");
			u.setPhotoBytes(file.getBytes());
		} else {
			u.setPhotoBytes(oldUser.getPhotoBytes());
		}
		return accountService.updateProfile(idU, u);
	}

	@PatchMapping("/editActif/{idU}")
	public void editActif(@PathVariable("idU") Long idU) {
		accountService.updateActif(idU);
	}

	@PatchMapping("/lock/{idU}")
	public void lockUser(@PathVariable("idU") Long idU) {
		accountService.lockUser(idU);
	}
}
