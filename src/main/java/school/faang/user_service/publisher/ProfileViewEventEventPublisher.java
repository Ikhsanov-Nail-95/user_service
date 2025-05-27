package school.faang.user_service.publisher;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import school.faang.user_service.event.ProfileViewEvent;

@Service
public class ProfileViewEventEventPublisher extends AbstractEventPublisher<ProfileViewEvent> {
    public ProfileViewEventEventPublisher(RedisTemplate<String, Object> redisTemplate,
                                          ChannelTopic profileViewEventTopic) {
        super(redisTemplate, profileViewEventTopic);
    }
}