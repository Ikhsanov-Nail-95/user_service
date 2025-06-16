package school.faang.user_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.faang.user_service.dto.UserDto;
import school.faang.user_service.service.user.UserService;

import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") @Positive(message = "User ID must be positive") long id) {
        return userService.getUser(id);
    }

    @PostMapping("/create")
    public UserDto create(@RequestBody @Valid UserDto userDto) {
        return userService.create( userDto );
    }

    @PostMapping
    public List<UserDto> getUsersByIds(@RequestBody List<Long> ids) {
        log.info("getUsersByIds was called");
        return userService.getUsersByIds(ids);
    }

    @PostMapping("/deactivate/{id}")
    public void deactivate(@PathVariable("id") long id) {
        userService.deactivate(id);
    }
}

/**
 * Как исправить:
 * 🔧 1. Исправь UserService, чтобы он возвращал 404, а не 500:
 * В контроллере UserController#getUser:
 *
 * java
 * Копировать
 * Редактировать
 * @GetMapping("/{id}")
 * public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
 *     try {
 *         UserResponse user = userService.getUser(id);
 *         return ResponseEntity.ok(user);
 *     } catch (UserNotFoundException e) {
 *         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // или кастомный body
 *     }
 * }
 * Или лучше — пробрось исключение, а в глобальном @ControllerAdvice обработай его:
 *
 * java
 * Копировать
 * Редактировать
 * @ResponseStatus(HttpStatus.NOT_FOUND)
 * @ExceptionHandler(UserNotFoundException.class)
 * public ErrorResponse handleNotFound(UserNotFoundException ex) {
 *     return new ErrorResponse("User not found.");
 * }
 * Это даст:
 *
 * rust
 * Копировать
 * Редактировать
 * HTTP 404 -> FeignException.NotFound
 * И в PostService ты сможешь ловить FeignException.NotFound корректно, и @Retryable больше не будет повторять вызовы.
 */