package school.faang.user_service.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProfileAppearedInSearchEvent {
    private Long viewedUserId;
    private Long searchingUserId;
    private ZonedDateTime appearedAt;
}
