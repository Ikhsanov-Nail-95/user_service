package school.faang.user_service.controller.mentorship;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import school.faang.user_service.dto.mentorship.MentorshipRequestDto;
import school.faang.user_service.dto.mentorship.Rejection;
import school.faang.user_service.dto.mentorship.RequestFilter;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.exception.ErrorMessage;
import school.faang.user_service.service.mentorship.MentorshipRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MentorshipRequestController {

    private final MentorshipRequestService mentorshipRequestService;

    @PostMapping("/mentorship/request")
    public MentorshipRequestDto requestMentorship(@RequestBody @Validated MentorshipRequestDto mentorshipRequest) {
        return mentorshipRequestService.requestMentorship(mentorshipRequest);
    }

    @PostMapping("/mentorship/request/list")
    public List<MentorshipRequestDto> getRequests(@RequestBody @Validated RequestFilter filter) {
        return mentorshipRequestService.getRequests(filter);
    }

    @PostMapping("/mentorship/request/{id}/accept")
    public MentorshipRequestDto acceptRequest(@PathVariable long id) {
        return mentorshipRequestService.acceptRequest(id);
    }

    @PostMapping("/mentorship/request/{id}/reject")
    public MentorshipRequestDto rejectRequest(@PathVariable long id, @RequestBody @Validated Rejection rejection) {
        return mentorshipRequestService.rejectRequest(id, rejection);
    }
}