package tn.esprit.voyage.demo.security.constant;

public class SecurityConstant {

	public final static String JWT_ISSUER = "jwt.issuer";
	public final static String JWT_AUDIENCE = "jwt.aud";
	public final static String JWT_SECRET = "jwt.secret";
	public final static String JWT_EXPIRED_AT = "jwt.expiration";
	public final static String JWT_EXPIRATION_UNIT = "jwt.expiration.unite";
	public final static String JWT_EXPIRATION_UNIT_DAY = "D";
	public final static String JWT_EXPIRATION_UNIT_HEURE = "H";
	public final static String JWT_EXPIRATION_UNIT_MINUTES = "M";
	public final static String JWT_TOKEN_PREFIX = "Bearer ";
	public final static String JWT_CLAIM_ROLES = "roles";

	public final static String ACCESS_CONTROL_ALLOW_ORIGIN = "*";
	public final static String ACCESS_CONTROL_ALLOW_HEADERS = "Origin, Accept,X-Request-With,Content-Type, Access-Control-Request-Method,Access-Control-Request-Headers,authorization";
	public final static String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Allow-Origin,Access-Control-Allow-Credentials,authorization";

}