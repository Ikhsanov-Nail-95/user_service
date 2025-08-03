package school.faang.user_service.publisher;

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
import school.faang.user_service.event.ProfileAppearedInSearchEvent;
import school.faang.user_service.exception.JsonSerializationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileAppearedInSearchEventPublisherTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ChannelTopic channelTopic;

    @InjectMocks
    private ProfileAppearedInSearchEventPublisher searchAppearanceEventPublisher;

    private ProfileAppearedInSearchEvent profileAppearedInSearchEvent;

    private final String topic = "search-appearance-topic";

    @BeforeEach
    void setUp() {
        when(channelTopic.getTopic()).thenReturn(topic);
        profileAppearedInSearchEvent = new ProfileAppearedInSearchEvent();
    }

    @Test
    @DisplayName("Should serialize profileAppearedInSearchEvent to JSON and send it to Redis channel")
    void publish_shouldPublishedToRedis_whenValidEvent() {
        searchAppearanceEventPublisher.publish(profileAppearedInSearchEvent);

        verify(redisTemplate, times(1)).convertAndSend(topic, profileAppearedInSearchEvent);
    }

    @Test
    @DisplayName("Should throw JsonSerializationException when RedisTemplate throws SerializationException")
    void testPublish_FailedSerialization() {
        SerializationException serializationException = new SerializationException("test serialization failure");
        doThrow(serializationException)
                .when(redisTemplate)
                .convertAndSend(topic, profileAppearedInSearchEvent);

        JsonSerializationException thrown = assertThrows(JsonSerializationException.class,
                ()-> searchAppearanceEventPublisher.publish(profileAppearedInSearchEvent));

        assertEquals("Failed to serialize event: " + profileAppearedInSearchEvent + " to JSON", thrown.getMessage());
        assertSame(serializationException, thrown.getCause());
    }
}