package school.faang.user_service.config.redis;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis.channels")
public class RedisChannelsConfig {

    @NotEmpty
    private String userBannerEvent;

    @NotEmpty
    private String followerEvent;


    @NotEmpty
    private String mentorshipEvent;

    @NotEmpty
    private String premiumBoughtEvent;

    @NotEmpty
    private String profileViewEvent;

    @NotEmpty
    private String searchAppearanceEvent;

    @Bean
    public ChannelTopic userBannerEventTopic() {
        return new ChannelTopic(userBannerEvent);
    }

    @Bean
    public ChannelTopic followerEventTopic() {
        return new ChannelTopic(followerEvent);
    }

    @Bean
    public ChannelTopic mentorshipEventTopic() {
        return new ChannelTopic(mentorshipEvent);
    }

    @Bean
    public ChannelTopic premiumBoughtEventTopic() {
        return new ChannelTopic(premiumBoughtEvent);
    }

    @Bean
    public ChannelTopic profileViewEventTopic() {
        return new ChannelTopic(profileViewEvent);
    }

    @Bean
    public ChannelTopic searchAppearanceEventTopic() {
        return new ChannelTopic(searchAppearanceEvent);
    }
}