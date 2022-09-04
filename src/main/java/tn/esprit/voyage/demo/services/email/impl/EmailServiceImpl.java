package tn.esprit.voyage.demo.services.email.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import tn.esprit.voyage.demo.entities.Utilisateur;
import tn.esprit.voyage.demo.exceptions.AlreadySubscribedEmploye;
import tn.esprit.voyage.demo.security.utils.JWTUtils;
import tn.esprit.voyage.demo.services.account.AccountService;
import tn.esprit.voyage.demo.services.email.EmailService;

/**
 * 
 * 
 *
 */
@Service("emailService")
public class EmailServiceImpl implements EmailService {
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Configuration config;

	@Autowired
	private AccountService accountService;

	/**
	 * This method will send compose and send the message
	 */

	public void sendMailWithAttachment(String to, String numFact, String fullName, MultipartFile fileMulti) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
				mimeMessage.setFrom(new InternetAddress("toutou.developper@gmail.com"));
				mimeMessage.setSubject(String.format("EasyFact factureN� %s", numFact));
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
				helper.setText(String.format(
						"Bonjour %s, <br/><br/>Merci de votre confiance � EasyFact ! Vous pouvez consulter vos factures sur notre application � tout moment.<br/>Veuillez trouver ci-joint Votre facture.<br/>N'h�sitez pas � nous contacter pour tout renseignement.<br/><br/>Cordialement.",
						fullName), true);
				helper.addAttachment(String.format("%s_%s.pdf", "factureN", numFact), fileMulti);
			}
		};

		try {
			mailSender.send(preparator);
		} catch (MailException ex) {
			// simply log it and go on...
			System.err.println(ex.getMessage());
		}
	}

	@Override
	public String sendMailwithtemplate(String subject, String link, String to) {
		String token = JWTUtils.encoder(to, new ArrayList<String>());
		MimeMessage message = mailSender.createMimeMessage();
		try {
			// set mediaType
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			Template t = config.getTemplate("templateMail.ftlh");
			Map<String, Object> model = new HashMap();
			model.put("token", token);
			model.put("link", link);
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
			helper.setTo(to);
			helper.setText(html, true);
			helper.setSubject(subject);
			helper.setFrom(new InternetAddress("toutou.developper@gmail.com"));
			mailSender.send(message);

		} catch (MessagingException | IOException | TemplateException ex) {
			System.err.println(ex.getMessage());
		}
		return token;

	}

	@Override
	public void sendMailwithtemplateRegistration(String subject, String link, String to, Long idU) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			// set mediaType
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			Template t = config.getTemplate("templateRegister.ftlh");
			Map<String, Object> model = new HashMap<>();
			model.put("idU", Long.toString(idU));
			model.put("link", link);
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
			helper.setTo(to);
			helper.setText(html, true);
			helper.setSubject(subject);
			helper.setFrom(new InternetAddress("toutou.developper@gmail.com"));
			mailSender.send(message);

		} catch (MessagingException | IOException | TemplateException ex) {
			System.err.println(ex.getMessage());
		}

	}

	@Override
	public void sendMailwithtemplateRegistrationSalarie(String subject, String link, String to, Long idS) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			// set mediaType
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			Template t = config.getTemplate("templateRegisterSalarie.ftlh");
			Map<String, Object> model = new HashMap<>();
			model.put("link", link);
			model.put("idS", Long.toString(idS));
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
			helper.setTo(to);
			helper.setText(html, true);
			helper.setSubject(subject);
			helper.setFrom(new InternetAddress("toutou.developper@gmail.com"));
			mailSender.send(message);
		} catch (MessagingException | IOException | TemplateException ex) {
			System.err.println(ex.getMessage());
		}
	}

	@Override
	public void sendMailwithtemplateToEmployee(String subject, String link, String email, String nom, String prenom) {

		Utilisateur u = accountService.loadUserByEmailAndIsActif(email);
		if (u != null) {
			throw new AlreadySubscribedEmploye();
		} else {

			MimeMessage message = mailSender.createMimeMessage();
			try {
				// set mediaType
				MimeMessageHelper helper = new MimeMessageHelper(message,
						MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
				Template t = config.getTemplate("templateRegisterSalarie.ftlh");
				Map<String, Object> model = new HashMap<>();
				model.put("link", link);
				model.put("email", email);
				model.put("nom", nom);
				model.put("prenom", prenom);
				String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
				helper.setTo(email);
				helper.setText(html, true);
				helper.setSubject(subject);
				helper.setFrom(new InternetAddress("toutou.developper@gmail.com"));
				mailSender.send(message);
			} catch (MessagingException | IOException | TemplateException ex) {
				System.err.println(ex.getMessage());
			}
		}
	}

}