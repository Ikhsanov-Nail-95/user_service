package school.faang.user_service.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.SerializationException;
import school.faang.user_service.exception.JsonSerializationException;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventPublisher<T> implements EventPublisher<T> {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    @Override
    public void publish(T eventType) {
        try {
            redisTemplate.convertAndSend(channelTopic.getTopic(), eventType);
        } catch (SerializationException exception) {
            log.error("Failed to publish [{}] to topic [{}]. Serialization error occurred",
                    eventType.getClass().getSimpleName(),
                    channelTopic.getTopic(),
                    exception);
            throw new JsonSerializationException("Failed to serialize event: " + eventType + " to JSON", exception);
        }
    }
}