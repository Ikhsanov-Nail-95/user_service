package school.faang.user_service.event;

import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileAppearedInSearchEvent {
    private Long viewedUserId;
    private Long searchingUserId;
    private ZonedDateTime appearedAt;
}
