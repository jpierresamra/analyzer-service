package com.analyzer.security.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import common.obj.vo.user.SecureUserVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil
{
	@Value("${jwt.key}")
	private String SECRET_KEY;

	public String extractUsername(String token)
	{
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token)
	{
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
	{
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public Claims getClaimsFromToken(String token) 
	{
        Claims claims;
        try 
        {
	       claims = Jwts.parser()
	               .setSigningKey(SECRET_KEY)
	               .parseClaimsJws(token)
	               .getBody();
	    }
        catch (Exception e) 
        {
           e.printStackTrace();
           claims = null;
        }
        return claims;
    }

	private Claims extractAllClaims(String token)
	{
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token)
	{
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(Authentication authentication)
	{
		Map<String, Object> claims = new HashMap<>();
		SecureUserVO userDetails = (SecureUserVO) authentication.getPrincipal();
		claims.put("businessId", String.valueOf(userDetails.getBusinessId()));
		claims.put("type", String.valueOf(userDetails.getType()));
		String id = String.valueOf(userDetails.getId());
		return createToken(claims, userDetails.getUserName(), id);
	}

	private String createToken(Map<String, Object> claims, String subject, String id)
	{
		return Jwts.builder().setClaims(claims).setId(id).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}
	
	public Boolean validateToken(String token, SecureUserVO secureUserVO)
	{
		final String username = extractUsername(token);
		return (username.equals(secureUserVO.getUserName()) && !isTokenExpired(token));
	}
}
