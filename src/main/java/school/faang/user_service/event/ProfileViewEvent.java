package school.faang.user_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ProfileViewEvent {
    private long observedId;
    private long observerId;
    private ZonedDateTime viewedAt;
}