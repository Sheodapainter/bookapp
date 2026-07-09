package org.example.calc.services;

import org.example.calc.models.Role;
import org.example.calc.models.User;
import org.example.calc.repositories.impl.UserRepositoryJpaAdapter;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class AuthService{
    private final UserRepositoryJpaAdapter repo;

    @Transactional
    public boolean register(String username, String password) {
        for(User u: repo.findAll()){
            if(Objects.equals(u.getLogin(), username)) {
                return false;
            }
        }
        repo.save(new User(null, username, BCrypt.hashpw(password, BCrypt.gensalt()), Role.USER));
        return true;
    }

    @Transactional
    public Optional<User> login(String username, String password) {
        for(User u: repo.findAll()){
            if(Objects.equals(u.getLogin(), username)) {
                if(BCrypt.checkpw(password, u.getPasswordHash())) {
                    return Optional.of(u);
                }
            }
        }
        return Optional.empty();
    }
    public AuthService(UserRepositoryJpaAdapter r) {
        this.repo=r;
    }
}