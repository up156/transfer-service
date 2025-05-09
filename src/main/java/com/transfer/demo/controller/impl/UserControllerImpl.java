package com.transfer.demo.controller.impl;

import com.transfer.demo.config.security.JwtUser;
import com.transfer.demo.controller.UserController;
import com.transfer.demo.dto.UserDto;
import com.transfer.demo.dto.UserSearchDto;
import com.transfer.demo.request.UserDeleteContactsRequest;
import com.transfer.demo.request.UserSearchRequest;
import com.transfer.demo.request.UserUpdateRequest;
import com.transfer.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<UserDto> getMyProfile(JwtUser user) {
        return ResponseEntity.ok(userService.getUserProfile(user));
    }

    @Override
    public ResponseEntity<UserDto> update(JwtUser user, UserUpdateRequest request) {
        return ResponseEntity.ok(userService.update(user, request));
    }

    @Override
    public ResponseEntity<Void> deleteContacts(JwtUser user, UserDeleteContactsRequest request) {
        userService.deleteContacts(user, request);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Page<UserDto>> searchUsers(String name, String email, String phone, LocalDate dateOfBirth, Pageable pageable) {
        UserSearchRequest request = new UserSearchRequest(name, email, phone, dateOfBirth);
        UserSearchDto cachedUserSearchDto = userService.search(request, pageable);

        return ResponseEntity.ok(
                new PageImpl<>(cachedUserSearchDto.getUsers(),
                        PageRequest.of(cachedUserSearchDto.getPage(), cachedUserSearchDto.getSize()),
                        cachedUserSearchDto.getTotalElements()));
    }
}
