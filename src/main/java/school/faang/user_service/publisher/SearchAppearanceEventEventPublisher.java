package school.faang.user_service.publisher;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import school.faang.user_service.event.SearchAppearanceEvent;

@Component
public class SearchAppearanceEventEventPublisher extends AbstractEventPublisher<SearchAppearanceEvent> {

    public SearchAppearanceEventEventPublisher(RedisTemplate<String, Object> redisTemplate,
                                               ChannelTopic searchAppearanceEventTopic) {
        super(redisTemplate, searchAppearanceEventTopic);
    }
}