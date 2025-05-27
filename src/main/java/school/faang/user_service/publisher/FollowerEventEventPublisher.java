package school.faang.user_service.publisher;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import school.faang.user_service.event.FollowerEvent;

@Service
public class FollowerEventEventPublisher extends AbstractEventPublisher<FollowerEvent> {
    public FollowerEventEventPublisher(RedisTemplate<String, Object> redisTemplate,
                                       ChannelTopic followerEventTopic) {
        super(redisTemplate, followerEventTopic);
    }
}