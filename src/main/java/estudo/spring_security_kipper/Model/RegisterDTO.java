package estudo.spring_security_kipper.Model;

public record RegisterDTO(String login, String password, UserRole role) {
}
