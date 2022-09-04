package tn.esprit.voyage.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Vous avez déja détesté ce voyage") // 400

public class AlreadyDislikedException extends RuntimeException {

}
