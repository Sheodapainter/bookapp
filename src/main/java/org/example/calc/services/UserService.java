package org.example.calc.services;

import org.example.calc.models.Order;
import org.example.calc.models.User;
import org.example.calc.repositories.impl.OrderRepositoryJpaAdapter;
import org.example.calc.repositories.impl.UserRepositoryJpaAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class UserService{
    private final UserRepositoryJpaAdapter userRepository;
    private final OrderRepositoryJpaAdapter orderRepository;

    public UserService(UserRepositoryJpaAdapter userRepository, OrderRepositoryJpaAdapter orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(String id) {
        Optional<User> u = userRepository.findById(id);
        return u.orElse(null);
    }

    @Transactional(readOnly = true)
    public User findByLogin(String login) {
        Optional<User> u = userRepository.findByLogin(login);
        return u.orElse(null);
    }

    @Transactional
    public void deleteUser(String id, String loggedUserId) {
        if(id==null) {
            throw new IllegalArgumentException("Id nie może być null!");
        }
        if(Objects.equals(id, loggedUserId)) {
            throw new IllegalArgumentException("Nie można usunąć samego siebie!");
        }
        Optional<Order> r = orderRepository.findAll().stream()
                .filter(order -> order.getUserId().equals(id))
                .filter(order -> order.getFinalizeDateTime()==null).findFirst();
        if(r.isPresent()) {
            throw new IllegalArgumentException("Użytkownik posiada nieukończoną umowę!");
        }
        userRepository.deleteById(id);
    }
}
