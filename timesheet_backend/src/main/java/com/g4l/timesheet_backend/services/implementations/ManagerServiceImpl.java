package com.g4l.timesheet_backend.services.implementations;

import java.util.List;
import org.springframework.stereotype.Service;
import com.g4l.timesheet_backend.models.entities.Manager;
import com.g4l.timesheet_backend.models.enums.SequenceType;
import com.g4l.timesheet_backend.models.requests.UserRequest;
import com.g4l.timesheet_backend.models.responses.ManagerResponse;
import com.g4l.timesheet_backend.repositories.ManagerRepository;
import com.g4l.timesheet_backend.services.interfaces.ManagerService;
import com.g4l.timesheet_backend.services.interfaces.UserService;
import com.g4l.timesheet_backend.utils.SequenceGenerator;
import com.g4l.timesheet_backend.utils.mappers.models.UserMapper;
import lombok.NonNull;

@Service
public class ManagerServiceImpl implements ManagerService {
    private final ManagerRepository managerRepository;
    private final UserService userService;
    private final UserMapper userMapper;

    public ManagerServiceImpl(ManagerRepository managerRepository, UserMapper userMapper, UserService userService) {
        this.managerRepository = managerRepository;
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @SuppressWarnings("null")
    @Override
    public Object createManager(UserRequest userRequest) {
        Manager manager = userMapper.userRequestToManager(userRequest);

        manager.setId(SequenceGenerator.generateSequence(SequenceType.MANAGER_ID));
        manager = (Manager) userService.createUser(manager);

        try {
            return managerRepository.save(manager);
        } catch (Exception e) {
            return e;
        }
    }

    @SuppressWarnings("null")
    @Override
    public Object updateManager(UserRequest userRequest) {
        Manager recordToUpdate = (Manager) userService.getUser(
            userRequest.getUserName(), userRequest.getIdNumber(), userRequest.getEmail());

        if (recordToUpdate == null)
            return null;

        recordToUpdate = (Manager) userService.updateUserDetails(recordToUpdate, userRequest);

        try {
            return managerRepository.save(recordToUpdate);
        } catch (Exception e) {
            return e;
        }
    }

    @Override
    public Object getManagerById(@NonNull String managerId) {
        return managerRepository.findById(managerId).orElse(null);
    }

    @Override
    public Object deleteManager(@NonNull String managerId) {
        try {
            managerRepository.deleteById(managerId);
            return "Manager with id: " + managerId + " has been deleted";
        } catch (Exception e) {
            return e;
        }
    }

    @Override
    public List<ManagerResponse> getAllManagers() {
        return managerRepository.findAll().stream()
                .map(manager -> userMapper.managerToUserResponse(manager)).toList();
    }

    @Override
    public Object getManager(@NonNull String userId) {
        Object manager = (Manager) userService.getUser(userId);

        if (manager != null)
            return userMapper.managerToUserResponse((Manager) manager);

        return null;
    }

}
