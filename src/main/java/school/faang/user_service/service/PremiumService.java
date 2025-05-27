package school.faang.user_service.service;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.faang.user_service.client.PaymentServiceClient;
import school.faang.user_service.controller.premium.PremiumPeriod;
import school.faang.user_service.event.PremiumBoughtEvent;
import school.faang.user_service.dto.PremiumDto;
import school.faang.user_service.dto.payment.Currency;
import school.faang.user_service.dto.payment.PaymentRequest;
import school.faang.user_service.dto.payment.PaymentResponse;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.premium.Premium;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.mapper.PremiumMapper;
import school.faang.user_service.publisher.PremiumBoughtEventEventPublisher;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.premium.PremiumRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class PremiumService {
    private final PremiumRepository premiumRepository;
    private final PaymentServiceClient paymentServiceClient;
    private final UserRepository userRepository;
    private final PremiumMapper premiumMapper;
    private final ExecutorService executorService;
    private final PremiumBoughtEventEventPublisher premiumBoughtEventPublisher;

    @Value("${premium.remover.batchSize}")
    private int batchSize;

    @Transactional
    public PremiumDto buyPremium(long userId, PremiumPeriod premiumPeriod) {
        if (premiumRepository.existsByUserId(userId)) {
            log.warn("The user {} already has Premium subscription", userId);
            throw new DataValidationException("The user " + userId + " already has Premium subscription");
        }

        PaymentRequest paymentRequest = generatePaymentRequest(userId, premiumPeriod);

        ResponseEntity<PaymentResponse> paymentResponse = paymentServiceClient.sendPayment(paymentRequest);
        if (!paymentResponse.getStatusCode().equals(HttpStatus.OK)) {
            throw new DataValidationException("Transaction failed!");
        }
        PaymentResponse payment = paymentResponse.getBody();

        Premium premium = acqiurePremium(userId, premiumPeriod);

        premiumBoughtEventPublisher.publish(
                PremiumBoughtEvent.builder()
                        .userId(userId)
                        .amount(paymentRequest.amount())
                        .currency(paymentRequest.currency())
                        .days(premiumPeriod.getDays())
                        .build());
        return premiumMapper.toDto(premium);
    }

    private PaymentRequest generatePaymentRequest(Long userId, PremiumPeriod premiumPeriod) {
        return PaymentRequest.builder()
                .paymentNumber(UUID.randomUUID())//how can also generate unique payment number??
                .amount(premiumPeriod.getCost())
                .currency(Currency.USD)
                .build();

    }

    private Premium acqiurePremium(Long userId, PremiumPeriod premiumPeriod) {
        User user = userRepository.findById(userId).get();

        Premium premium = Premium.builder()
                .user(user)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plus(premiumPeriod.getDays(), ChronoUnit.DAYS))
                .build();

        Premium premiumNew = premiumRepository.save(premium);
        user.setPremium(premiumNew);
        return premiumNew;
    }

    @Transactional
    public void removeExpiredPremiums() {
        List<Premium> expiredPremiums = premiumRepository.findAllByEndDateBefore(LocalDateTime.now());

        List<List<Premium>> batches = Lists.partition(expiredPremiums, batchSize);

        for (List<Premium> batch : batches) {
            executorService.execute(() -> batch.forEach(premiumRepository::delete));
        }
    }
}