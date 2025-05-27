package school.faang.user_service.event;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ProfileViewEvent {
    @NotNull
    private Long observerId;
    @NotNull
    private Long observedId;
    @NotNull
    private LocalDateTime viewedAt;
}
