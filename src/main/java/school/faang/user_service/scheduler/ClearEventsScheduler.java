package school.faang.user_service.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import school.faang.user_service.service.event.EventService;


@Service
@RequiredArgsConstructor
public class ClearEventsScheduler {
    private final EventService eventService;

    @Scheduled(cron = "${clearEvents.cron}")
    public void clearEvents() {
        eventService.clearEvents();
    }
}
