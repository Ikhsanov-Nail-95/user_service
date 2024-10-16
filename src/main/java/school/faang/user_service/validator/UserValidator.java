package school.faang.user_service.validator;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.exception.UserNotFoundException;
import school.faang.user_service.exception.MessageError;

@Data
@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public void userExistenceInRepo(long userId){
        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(MessageError.USER_NOT_FOUND_EXCEPTION);
        }
    }
}
