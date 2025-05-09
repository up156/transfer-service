package com.transfer.demo.service;

import com.transfer.demo.config.security.JwtUser;
import com.transfer.demo.dto.UserDto;
import com.transfer.demo.dto.UserSearchDto;
import com.transfer.demo.request.UserDeleteContactsRequest;
import com.transfer.demo.request.UserSearchRequest;
import com.transfer.demo.request.UserUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;


@Tag(name = "UserService", description = "Сервис для работы с аккаунтом пользователя")
public interface UserService {

    UserDto getUserProfile(JwtUser user);

    UserDto update(JwtUser user, UserUpdateRequest request);

    UserSearchDto search(UserSearchRequest request, Pageable pageable);

    void deleteContacts(JwtUser user, UserDeleteContactsRequest request);

}