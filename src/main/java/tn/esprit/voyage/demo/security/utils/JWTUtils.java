package tn.esprit.voyage.demo.security.utils;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import tn.esprit.voyage.demo.security.constant.SecurityConstant;

public class JWTUtils {

	protected static Logger logger = LoggerFactory.getLogger(JWTUtils.class);

	public static String getIssuer() {
		return ConfigUtils.getProperty(SecurityConstant.JWT_ISSUER);
	}

	public static String getAudience() {
		return ConfigUtils.getProperty(SecurityConstant.JWT_AUDIENCE);
	}

	public static String getSecret() {
		return ConfigUtils.getProperty(SecurityConstant.JWT_SECRET);
	}

	private static Date getExpirationDate(Date expirationAt) {
		Date expirationDate = null;
		Integer expiration = Integer.parseInt(ConfigUtils.getProperty(SecurityConstant.JWT_EXPIRED_AT));
		switch (ConfigUtils.getProperty(SecurityConstant.JWT_EXPIRATION_UNIT)) {
		case SecurityConstant.JWT_EXPIRATION_UNIT_DAY:

			expirationDate = new Date(System.currentTimeMillis() + 24 * expiration * 3600 * 1000);
			break;
		case SecurityConstant.JWT_EXPIRATION_UNIT_HEURE:

			expirationDate = new Date(System.currentTimeMillis() + expiration * 3600 * 1000);
			break;
		case SecurityConstant.JWT_EXPIRATION_UNIT_MINUTES:

			expirationDate = new Date(System.currentTimeMillis() + expiration * 60 * 1000);
			break;
		}

		return expirationDate;
	}

	public static String encoder(String userName, List<String> roles)

	{
		String token = JWT.create().withIssuer(getIssuer()).withAudience(getAudience()).withSubject(userName)
				.withArrayClaim(SecurityConstant.JWT_CLAIM_ROLES, roles.toArray(new String[roles.size()]))
				// .withClaim("id", userName)
				.withExpiresAt(getExpirationDate(new Date())).sign(Algorithm.HMAC256(getSecret()));
		return token;

	}

	public static DecodedJWT decoder(String token)

	{
		JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(getSecret())).build();
		DecodedJWT decodedJWT = jwtVerifier.verify(token.substring(SecurityConstant.JWT_TOKEN_PREFIX.length()));
		return decodedJWT;

	}

}