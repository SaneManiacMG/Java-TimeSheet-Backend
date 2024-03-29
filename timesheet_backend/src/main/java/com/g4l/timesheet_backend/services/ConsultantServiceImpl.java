package com.g4l.timesheet_backend.services;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.g4l.timesheet_backend.interfaces.ConsultantService;
import com.g4l.timesheet_backend.interfaces.UserService;
import com.g4l.timesheet_backend.models.entities.Consultant;
import com.g4l.timesheet_backend.models.enums.SequenceType;
import com.g4l.timesheet_backend.models.requests.UserRequest;
import com.g4l.timesheet_backend.models.responses.ConsultantResponse;
import com.g4l.timesheet_backend.repositories.ConsultantRepository;
import com.g4l.timesheet_backend.utils.SequenceGenerator;
import com.g4l.timesheet_backend.utils.mappers.models.UserMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ConsultantServiceImpl implements ConsultantService {

    private final ConsultantRepository consultantRepository;
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public ConsultantResponse createConsultant(UserRequest userRequest) {
        Consultant consultant = userMapper.userRequestToConsultant(userRequest);

        consultant.setId(SequenceGenerator.generateSequence(SequenceType.CONSULTANT_ID));

        consultant = (Consultant) userService.createUser(consultant);

        consultantRepository.save(consultant);

        return userMapper.consultantToUserResponse(consultant);
    }

    @Override
    public ConsultantResponse updateConsultant(UserRequest userRequest) {
        Consultant consultant = (Consultant) userService.getUser(userRequest.getUserName(),
                userRequest.getIdNumber(),
                userRequest.getEmail());

        if (consultant == null)
            return null;

        consultant = (Consultant) userService.updateUserDetails(consultant, userRequest);

        consultantRepository.save(consultant);

        return userMapper.consultantToUserResponse(consultant);
    }

    @Override
    public ConsultantResponse getConsultantById(@NonNull String consultantId) {
        Consultant consultant = consultantRepository.findById(consultantId).orElse(null);

        return userMapper.consultantToUserResponse(consultant);
    }

    @Override
    public ConsultantResponse getConsultant(@NonNull String userId) {
        Consultant consultant = (Consultant) userService.getUser(userId);
        if (consultant != null)
            return userMapper.consultantToUserResponse(consultant);

        return null;
    }

    @Override
    public String deleteConsultant(@NonNull String consultantId) {
        consultantRepository.deleteById(consultantId);
        return "Consultant with consultant id " + consultantId + " deleted";
    }

    @Override
    public List<ConsultantResponse> getAllConsultants() {
        List<ConsultantResponse> consultantResponses = consultantRepository.findAll().stream()
                .map(consultant -> userMapper.consultantToUserResponse(consultant)).toList();

        return consultantResponses;
    }

    @Override
    public ConsultantResponse assignConsultantToClientTeam(@NonNull String consultantId, @NonNull String clientTeamId) {
        Consultant consultant = consultantRepository.findById(consultantId).orElse(null);

        if (consultant == null)
            return null;

        consultant.setClientTeamId(clientTeamId);
        consultant.setDateModified(LocalDateTime.now());

        consultantRepository.save(consultant);

        return userMapper.consultantToUserResponse(consultant);
    }

}
