package com.gautami.authorization.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationHelper {

	private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationHelper.class);


	private String secret = "thisisthesecretkeyformyapptovalidateusersandreturnbackavalidtokencontainingalltherequireddata";
	private static final long JWT_TOKEN_VALIDITY = 60*60;
	
	public String getUsernameFromToken(String token)
	{
		Claims claims =  getClaimsFromToken(token);
		return claims.getSubject();
	}
	
	public Claims getClaimsFromToken(String token)
	{
		Claims claims = Jwts.parserBuilder().setSigningKey(secret.getBytes())
				.build().parseClaimsJws(token).getBody();
		return claims;
	}
	
	public Boolean isTokenExpired(String token)
	{
		log.debug("Checking if token is still valid");
		Claims claims =  getClaimsFromToken(token);
		Date expDate = claims.getExpiration();
		return expDate.before(new Date());
	}

	public String generateToken(UserDetails userDetails) {
		
		Map<String,Object> claims = new HashMap<>();
		
		return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
		.setIssuedAt(new Date(System.currentTimeMillis()))
		.setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY*1000))
		.signWith(new SecretKeySpec(secret.getBytes(),SignatureAlgorithm.HS512.getJcaName()),SignatureAlgorithm.HS512)
		.compact();
	}
	
}
