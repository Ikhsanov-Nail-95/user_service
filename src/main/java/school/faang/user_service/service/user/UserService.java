package school.faang.user_service.service.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.faang.user_service.entity.User;
import school.faang.user_service.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User with id " + id + " is not exists"));
    }

    public void saveUser(User savedUser) {
        if (existsUserById(savedUser.getId())) {
            userRepository.save(savedUser);
        }
    }

    public boolean existsUserById(long id) {
        return userRepository.existsById(id);
    }
}
