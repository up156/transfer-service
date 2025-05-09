package com.transfer.demo.service.impl;

import com.transfer.demo.config.security.JwtUser;
import com.transfer.demo.dto.UserDto;
import com.transfer.demo.dto.UserSearchDto;
import com.transfer.demo.ex.AlreadyExistException;
import com.transfer.demo.ex.EmailNotFoundException;
import com.transfer.demo.ex.PhoneNotFoundException;
import com.transfer.demo.ex.UserNotFoundException;
import com.transfer.demo.mapper.UserMapper;
import com.transfer.demo.model.EmailData;
import com.transfer.demo.model.PhoneData;
import com.transfer.demo.model.User;
import com.transfer.demo.repository.UserRepository;
import com.transfer.demo.request.UserDeleteContactsRequest;
import com.transfer.demo.request.UserSearchRequest;
import com.transfer.demo.request.UserUpdateRequest;
import com.transfer.demo.service.UserService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    static final String NOT_FOUND_MESSAGE = "User not found: ";

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserProfile(JwtUser jwtUser) {

        log.info("User service started getUserProfile for user: {}", jwtUser);
        User user = userRepository.findById(jwtUser.getId())
                .orElseThrow(() -> new UserNotFoundException(NOT_FOUND_MESSAGE + jwtUser.getId()));

        return userMapper.mapToUserDto(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "userSearch", allEntries = true)
    public UserDto update(JwtUser jwtUser, UserUpdateRequest request) {

        log.info("User service started update with user: {} and with request: {}", jwtUser, request);
        User user = userRepository.findById(jwtUser.getId())
                .orElseThrow(() -> new UserNotFoundException(NOT_FOUND_MESSAGE + jwtUser.getId()));

        String email = request.email();
        String phone = request.phone();
        if (StringUtils.isNotBlank(email) && userRepository.existsByEmails_email(email)) {
            throw new AlreadyExistException("this email is already used");
        }
        if (!(phone == null) && userRepository.existsByPhones_phone(Long.parseLong(phone))) {
            throw new AlreadyExistException("this phone is already used");
        }

        userMapper.updateUserWithContacts(user, request);
        return userMapper.mapToUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    @CacheEvict(value = "userSearch", allEntries = true)
    public void deleteContacts(JwtUser jwtUser, UserDeleteContactsRequest request) {

        log.info("User service started delete email with user: {} and request: {}", jwtUser, request);
        User user = userRepository.findById(jwtUser.getId())
                .orElseThrow(() -> new UserNotFoundException(NOT_FOUND_MESSAGE + jwtUser.getId()));

        String email = request.email();
        String phone = request.phone();

        if (StringUtils.isNotBlank(email) && !userRepository.existsByEmails_emailAndId(email, jwtUser.getId())) {
            throw new EmailNotFoundException("Email not found: " + email);
        }
        if (StringUtils.isNotBlank(phone) && !userRepository.existsByPhones_phoneAndId(Long.parseLong(phone), jwtUser.getId())) {
            throw new PhoneNotFoundException("Phone not found: " + phone);
        }

        userMapper.removeContacts(user, email, phone);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "userSearch",
            key = "'userSearch:' + #request.name + ':' + #request.email + ':' + #request.phone + ':' + #request.dateOfBirth + ':' + #pageable.pageNumber + ':' + #pageable.pageSize"
    )
    public UserSearchDto search(UserSearchRequest request, Pageable pageable) {

        log.info("User service started search with request: {} and Pageable: {}", request, pageable);
        Specification<User> spec = createSpecification(request);
        Page<UserDto> userDtoPage = userRepository.findAll(spec, pageable).map(userMapper::mapToUserDto);
        return userMapper.toUserSearchDto(userDtoPage);
    }

    private Specification<User> createSpecification(UserSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getDateOfBirth() != null) {
                predicates.add(cb.greaterThan(root.get("dateOfBirth"), request.getDateOfBirth()));
            }
            if (request.getPhone() != null) {
                Join<User, PhoneData> phoneJoin = root.join("phones", JoinType.LEFT);
                predicates.add(cb.equal(phoneJoin.get("phone"), request.getPhone()));
            }
            if (request.getName() != null) {
                predicates.add(cb.like(root.get("name"), request.getName() + "%"));
            }
            if (request.getEmail() != null) {
                Join<User, EmailData> emailJoin = root.join("emails", JoinType.LEFT);
                predicates.add(cb.equal(emailJoin.get("email"), request.getEmail()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
