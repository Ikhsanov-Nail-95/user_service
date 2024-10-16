package school.faang.user_service.repository.recommendation;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import school.faang.user_service.entity.recommendation.RecommendationRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRequestRepository extends CrudRepository<RecommendationRequest, Long> {

    @Query(nativeQuery = true, value = """
            SELECT * FROM recommendation_request
            WHERE requester_id = ?1 AND receiver_id = ?2 AND status = 1
            ORDER BY created_at DESC
            LIMIT 1
            """)
    Optional<RecommendationRequest> findLatestPendingRequest(long requesterId, long receiverId);

    @Modifying
    @Query(value = "INSERT INTO recommendation_request (requester_id, receiver_id, status) VALUES (?1, ?2, 'pending')", nativeQuery = true)
    void createRequest(Long requesterId, Long receiverId);

    boolean existsByRequesterIdAndReceiverId(Long requesterId, Long receiverId);

    List<RecommendationRequest> findAllByRequesterIdAndReceiverId(Long requesterId, Long receiverId);

    List<RecommendationRequest> findAllByRequesterId(Long requesterId);

    List<RecommendationRequest> findAllByReceiverId(Long receiverId);
}