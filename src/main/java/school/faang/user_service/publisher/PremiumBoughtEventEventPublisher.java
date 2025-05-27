package school.faang.user_service.publisher;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import school.faang.user_service.event.PremiumBoughtEvent;

@Service
public class PremiumBoughtEventEventPublisher extends AbstractEventPublisher<PremiumBoughtEvent> {
    public PremiumBoughtEventEventPublisher(RedisTemplate<String, Object> redisTemplate,
                                            ChannelTopic premiumBoughtEventTopic) {
        super(redisTemplate, premiumBoughtEventTopic);
    }
}