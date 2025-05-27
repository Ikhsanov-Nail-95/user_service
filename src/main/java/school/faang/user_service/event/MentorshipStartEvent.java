package school.faang.user_service.event;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MentorshipStartEvent {

    @NotNull
    private long mentorId;
    @NotNull
    private long menteeId;

}
