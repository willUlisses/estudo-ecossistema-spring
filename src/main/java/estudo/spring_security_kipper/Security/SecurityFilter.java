package estudo.spring_security_kipper.Security;

import estudo.spring_security_kipper.Repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private TokenService tokenService;
    private UserRepository userRepository;

    public SecurityFilter(TokenService tokenService, UserRepository userRepository){
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = this.recoverToken(request);
        if (token != null) {
            String login = tokenService.validateToken(token); //pegamos o login no token já com o prefixo retirado
            UserDetails user = userRepository.findByLogin(login); // pegamos o usuário com aquele login no DB
            if (user != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()); // encapsulamos esse usuário num novo token de usernamePasswordAuth
                SecurityContextHolder.getContext().setAuthentication(authentication); // encapsulamos num contexto de auth
                // o nosso token do usuario
            }

        }
        filterChain.doFilter(request, response); // agora passamos pro próximo filtro
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization"); // pego o token associado ao header/chave "Authorization"
        if (authHeader == null) return null; // retorna null se não tiver nenhum ‘token’ com aquele header na request
        return authHeader.replace("Bearer ", ""); // tira o prefixo Bearer do token
    }
}
