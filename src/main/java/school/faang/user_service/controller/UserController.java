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

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable("userId") @Positive(message = "User ID must be positive") long userId) {
        return userService.getUser(userId);
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

    @PostMapping("/deactivate/{userId}")
    public void deactivate(@PathVariable("userId") long userId) {
        userService.deactivate(userId);
    }

}