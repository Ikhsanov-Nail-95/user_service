package school.faang.user_service.publisher;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import school.faang.user_service.event.MentorshipStartEvent;

@Service
public class MentorshipEventEventPublisher extends AbstractEventPublisher<MentorshipStartEvent> {
    public MentorshipEventEventPublisher(RedisTemplate<String, Object> redisTemplate,
                                         ChannelTopic mentorshipEventTopic) {
        super(redisTemplate, mentorshipEventTopic);
    }
}