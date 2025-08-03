package school.faang.user_service.publisher;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import school.faang.user_service.event.ProfileAppearedInSearchEvent;

@Component
public class ProfileAppearedInSearchEventPublisher extends AbstractEventPublisher<ProfileAppearedInSearchEvent> {

    public ProfileAppearedInSearchEventPublisher(RedisTemplate<String, Object> redisTemplate,
                                                 ChannelTopic profileAppearedInSearchTopic) {
        super(redisTemplate, profileAppearedInSearchTopic);
    }
}