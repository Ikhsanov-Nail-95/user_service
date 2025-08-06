package school.faang.user_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.faang.user_service.config.context.UserContext;
import school.faang.user_service.event.FollowerEvent;
import school.faang.user_service.event.ProfileAppearedInSearchEvent;
import school.faang.user_service.dto.subscription.SubscriptionUserDto;
import school.faang.user_service.dto.subscription.SubscriptionUserFilterDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.filter.user.UserFilter;
import school.faang.user_service.mapper.SubscriptionUserMapper;
import school.faang.user_service.publisher.FollowerEventEventPublisher;
import school.faang.user_service.publisher.ProfileAppearedInSearchEventPublisher;
import school.faang.user_service.repository.SubscriptionRepository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionUserMapper userMapper;
    private final List<UserFilter> userFilters;
    private final FollowerEventEventPublisher followerEventPublisher;
    private final ProfileAppearedInSearchEventPublisher searchAppearanceEventPublisher;
    private final UserContext userContext;

    @Transactional
    public void followUser(long followerId, long followeeId) {
        if (followerId == followeeId) {
            throw new DataValidationException("You can not follow yourself!");
        }
        if (subscriptionRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            throw new DataValidationException("This subscription already exists!");
        }

        subscriptionRepository.followUser(followerId, followeeId);

        FollowerEvent followerEvent = FollowerEvent.builder().followerId(followerId).
                followeeId(followeeId).
                subscriptionDateTime(LocalDateTime.now()).build();

        followerEventPublisher.publish(followerEvent);

        log.info("Successfully sent data to analytics-service" + followerEvent);

    }

    public void unfollowUser(long followerId, long followeeId) {
        if (followerId == followeeId) {
            throw new DataValidationException("You can not unfollow yourself!");
        }
        subscriptionRepository.unfollowUser(followerId, followeeId);
    }

    public List<SubscriptionUserDto> getFollowers(long followeeId, SubscriptionUserFilterDto filter) {
        Stream<User> users = subscriptionRepository.findByFolloweeId(followeeId);
        return filterUsers(users, filter);
    }

    public int getFollowersCount(long followeeId) {
        return subscriptionRepository.findFollowersAmountByFolloweeId(followeeId);
    }

    public List<SubscriptionUserDto> getFollowing(long followeeId, SubscriptionUserFilterDto filter) {
        Stream<User> users = subscriptionRepository.findByFolloweeId(followeeId);
        return filterUsers(users, filter);
    }

    public int getFollowingCount(long followerId) {
        return subscriptionRepository.findFolloweesAmountByFollowerId(followerId);
    }

    private List<SubscriptionUserDto> filterUsers(Stream<User> users, SubscriptionUserFilterDto filters) {
        Stream<User> filteredUsers = users;
        for (UserFilter userFilter : userFilters) {
            if (userFilter.isApplicable(filters)) {
                filteredUsers = userFilter.apply(filteredUsers, filters);
            }
        }

        List<SubscriptionUserDto> filteredUsersList = userMapper.toDto(users.toList());
        List<Long> userIds = filteredUsersList.stream().map(SubscriptionUserDto::getId).toList();

        userIds.forEach(userId -> {
            ProfileAppearedInSearchEvent event = ProfileAppearedInSearchEvent.builder()
                    .viewedUserId(userId)
                    .searchingUserId(userContext.getUserId())
                    .appearedAt(ZonedDateTime.now())
                    .build();
            searchAppearanceEventPublisher.publish(event);
        });

        return filteredUsersList;
    }
}