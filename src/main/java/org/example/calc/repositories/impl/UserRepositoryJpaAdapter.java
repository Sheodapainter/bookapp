package org.example.calc.repositories.impl;

import org.example.calc.models.User;
import org.example.calc.repositories.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryJpaAdapter {

    private final UserJpaRepository delegate;

    public UserRepositoryJpaAdapter(UserJpaRepository delegate) { this.delegate = delegate; }

    public List<User> findAll() {
        return delegate.findAll();
    }
    public Optional<User> findById(String id) {
        return delegate.findById(id);
    }
    public Optional<User> findByLogin(String login) {
        Optional<User> user = delegate.findAll().stream().filter(u -> Objects.equals(u.getLogin(), login)).findFirst();
        return user.flatMap(value -> delegate.findById(value.getId()));
    }
    public User save(User user) {
        if(user.getId()==null) {
            user.setId(UUID.randomUUID().toString());
        }
        return delegate.save(user);
    }
    public void deleteById(String id) {
        delegate.deleteById(id);
    }
}
