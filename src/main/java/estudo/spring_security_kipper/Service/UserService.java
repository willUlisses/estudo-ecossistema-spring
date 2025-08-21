package estudo.spring_security_kipper.Service;

import estudo.spring_security_kipper.Exception.UserNotFoundException;
import estudo.spring_security_kipper.Model.User;
import estudo.spring_security_kipper.Repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User post(User user) {
        if (user.getUsername() == null)
            throw new UsernameNotFoundException("Não existe usuario com esse username");

        return repository.save(user);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(Integer id) {
        Optional<User> userOptional = repository.findById(id);
        return userOptional.orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
    }

}
