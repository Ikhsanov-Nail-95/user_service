package school.faang.user_service.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.SerializationException;
import school.faang.user_service.event.SearchAppearanceEvent;
import school.faang.user_service.exception.JsonSerializationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchAppearanceEventPublisherTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ChannelTopic channelTopic;

    @InjectMocks
    private SearchAppearanceEventEventPublisher searchAppearanceEventPublisher;

    private SearchAppearanceEvent searchAppearanceEvent;

    private final String topic = "search-appearance-topic";

    @BeforeEach
    void setUp() {
        when(channelTopic.getTopic()).thenReturn(topic);
        searchAppearanceEvent = new SearchAppearanceEvent();
    }

    @Test
    @DisplayName("Should serialize searchAppearanceEvent to JSON and send it to Redis channel")
    void publish_shouldPublishedToRedis_whenValidEvent() {
        searchAppearanceEventPublisher.publish(searchAppearanceEvent);

        verify(redisTemplate, times(1)).convertAndSend(topic, searchAppearanceEvent);
    }

    @Test
    @DisplayName("Should throw JsonSerializationException when RedisTemplate throws SerializationException")
    void testPublish_FailedSerialization() {
        SerializationException serializationException = new SerializationException("test serialization failure");
        doThrow(serializationException)
                .when(redisTemplate)
                .convertAndSend(topic, searchAppearanceEvent);

        JsonSerializationException thrown = assertThrows(JsonSerializationException.class,
                ()-> searchAppearanceEventPublisher.publish(searchAppearanceEvent));

        assertEquals("Failed to serialize event: " + searchAppearanceEvent + " to JSON", thrown.getMessage());
        assertSame(serializationException, thrown.getCause());
    }
}