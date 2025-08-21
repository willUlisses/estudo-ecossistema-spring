package estudo.spring_security_kipper.Security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import estudo.spring_security_kipper.Model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret")
    private String secret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create() //usamos a lib do JWT pra construir o token
                    .withIssuer("auth-api") //colocamos quem criou no payload
                    .withSubject(user.getLogin()) //colocamos o login do usuário no payload
                    .withExpiresAt(generateExpirationDate()) //geramos a data de expiração
                    .sign(algorithm); //assinamos o token utilizando o algoritmo de hash 256 utilizando a secretKey
            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("[ERROR] Error while generatin token", e);
        }
    }



    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)// -> pega o algoritmo usado para assinatura do token juntamente da secret
                    .withIssuer("auth-api")// verifica o issuer
                    .build()
                    .verify(token)//decoda o token
                    .getSubject();// pega o subject, no caso meu usuario
        } catch (JWTVerificationException e) {
            return "";
        }
    }


    private Instant generateExpirationDate() { //colocamos que a expiration date vai ser 2 horas a partir da sua generation
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}
