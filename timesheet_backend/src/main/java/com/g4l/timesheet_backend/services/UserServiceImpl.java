package com.g4l.timesheet_backend.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import com.g4l.timesheet_backend.models.requests.PasswordRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.g4l.timesheet_backend.interfaces.UserService;
import com.g4l.timesheet_backend.models.entities.Consultant;
import com.g4l.timesheet_backend.models.entities.Manager;
import com.g4l.timesheet_backend.models.entities.Role;
import com.g4l.timesheet_backend.models.entities.User;
import com.g4l.timesheet_backend.models.enums.AccountStatus;
import com.g4l.timesheet_backend.models.enums.AccountType;
import com.g4l.timesheet_backend.models.requests.UserRequest;
import com.g4l.timesheet_backend.repositories.ConsultantRepository;
import com.g4l.timesheet_backend.repositories.ManagerRepository;
import com.g4l.timesheet_backend.repositories.UserAuthoritiesRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final ConsultantRepository consultantRepository;
    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAuthoritiesRepository userAuthoritiesRepository;

    public Object getUser(String userId) {
        // String cellPhoneNumberPattern = "^\\d{10}$";
        String idNumberPattern = "^\\d{13}$";
        String usernamePattern = "^[a-zA-Z0-9._-]{3,}$";
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

        if (Pattern.matches(idNumberPattern, userId)) {
            return findByIdNumber(userId);
        }

        if (Pattern.matches(usernamePattern, userId)) {
            return findByUserName(userId);
        }

        if (Pattern.matches(emailPattern, userId)) {
            return findByEmail(userId);
        }

        return null;
    }

    private Object findByEmail(String email) {
        if (consultantRepository.findByEmail(email) != null)
            return consultantRepository.findByEmail(email);
        if (managerRepository.findByEmail(email) != null)
            return managerRepository.findByEmail(email);
        return null;
    }

    private Object findByUserName(String username) {
        if (consultantRepository.findByUserName(username) != null)
            return consultantRepository.findByUserName(username);
        if (managerRepository.findByUserName(username) != null)
            return managerRepository.findByUserName(username);
        return null;
    }

    private Object findByIdNumber(String idNumber) {
        if (consultantRepository.findByIdNumber(idNumber) != null)
            return consultantRepository.findByIdNumber(idNumber);
        if (managerRepository.findByIdNumber(idNumber) != null)
            return managerRepository.findByIdNumber(idNumber);
        return null;
    }

    @Override
    public Object getUser(String username, String idNumber, String email) {
        if (getUser(username) != null)
            return getUser(username);
        if (getUser(idNumber) != null)
            return getUser(idNumber);
        if (getUser(email) != null)
            return getUser(email);
        return null;
    }

    @Override
    public <T extends User> T updateUserDetails(T user, UserRequest request) {
        user.setId(request.getIdNumber());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setUserName(request.getUserName());
        user.setDateModified(LocalDateTime.now());

        return user;
    }

    @Override
    public Object resetPassword(PasswordRequest passwordRequest, User user) {
        if (user != null) {
            user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
            user.setDateModified(LocalDateTime.now());
    
            return saveUser((User) user);
        }

        if (getUser(passwordRequest.getUserId()) != null)
            return resetPassword(passwordRequest, (User) getUser(passwordRequest.getUserId()));

        return null;
    }

    @Override
    public Object changePassword(PasswordRequest passwordRequest) {
        User user = (User) getUser(passwordRequest.getUserId());

        if (user == null)
            return null;

        if (passwordEncoder.matches(passwordRequest.getCurrentPassword(), user.getPassword())) 
            return resetPassword(passwordRequest, user);
        else
            return "Password does not match current password";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (consultantRepository.findByUserName(username) != null)
            return consultantRepository.findByUserName(username);
        if (managerRepository.findByUserName(username) != null)
            return managerRepository.findByUserName(username);

        throw new UsernameNotFoundException("User not found");
    }

    @Override
    public Object addAccountType(String userId, AccountType accountType) {
        Object user = getUser(userId);

        if (user == null)
            return null;

        if (user instanceof Consultant) {
            return updateAuthorities((Consultant) user, accountType, false);
        }

        if (user instanceof Manager) {
            return updateAuthorities((Manager) user, accountType, false);
        }

        return null;
    }

    @Override
    public Object removeAccountType(String userId, AccountType accountType) {
        Object user = getUser(userId);

        if (user != null)
            return saveUser((User) user);

        return null;
    }

    private User updateAuthorities(User user, AccountType accountType, boolean remove) {
        Set<Role> authorities = (HashSet) user.getAuthorities();

        if (remove)
            authorities.remove(accountType);

        user.setAuthorities(authorities);
        user.setDateModified(LocalDateTime.now());

        return saveUser(user);
    }

    @Override
    public Object changeAccountStatus(String userId, AccountStatus accountStatus) {
        Object user = getUser(userId);

        if (user == null)
            return null;

        if (user instanceof Consultant) {
            return updateAccountStatus((Consultant) user, accountStatus);
        }

        if (user instanceof Manager) {
            return updateAccountStatus((Manager) user, accountStatus);
        }

        return null;
    }

    private User updateAccountStatus(User user, AccountStatus accountStatus) {
        user.setAccountStatus(accountStatus);
        user.setDateModified(LocalDateTime.now());

        saveUser(user);

        return null;
    }

    private User saveUser(User user) {
        user.setDateModified(LocalDateTime.now());

        if (user instanceof Consultant) {
            consultantRepository.save((Consultant) user);
            return user;
        }

        if (user instanceof Manager) {
            managerRepository.save((Manager) user);
            return user;
        }

        return null;
    }

    @Override
    public <T extends User> T createUser(T user) {
        Set<Role> roles = new HashSet<>();

        user.setAccountStatus(AccountStatus.UNVERIFIED);
        user.setDateCreated(LocalDateTime.now());
        user.setDateModified(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode("NOT_SET"));

        roles.add(userAuthoritiesRepository.findByAuthority("ROLE_UNVERIFIED"));
        user.setAuthorities(roles);

        return user;
    }
}
