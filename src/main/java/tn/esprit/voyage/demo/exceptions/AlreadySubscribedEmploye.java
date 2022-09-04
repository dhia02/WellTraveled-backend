package tn.esprit.voyage.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Employé déja inscrit") // 400
public class AlreadySubscribedEmploye extends RuntimeException {

}
