package tn.esprit.voyage.demo.services.email;

import org.springframework.web.multipart.MultipartFile;

public interface EmailService {

	public void sendMailWithAttachment(String to, String numFact, String fullName, MultipartFile fileMulti);

	public String sendMailwithtemplate(String subject, String link, String to);

	public void sendMailwithtemplateRegistration(String subject, String link, String to, Long idU);

	public void sendMailwithtemplateRegistrationSalarie(String subject, String link, String to, Long idS);

	public void sendMailwithtemplateToEmployee(String subject, String link, String email, String nom, String prenom);

}
