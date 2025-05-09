package com.transfer.demo.mapper;

import com.transfer.demo.dto.UserDto;
import com.transfer.demo.dto.UserSearchDto;
import com.transfer.demo.model.EmailData;
import com.transfer.demo.model.PhoneData;
import com.transfer.demo.model.User;
import com.transfer.demo.request.UserUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto mapToUserDto(User user);

    @Mapping(target = "phones", ignore = true)
    @Mapping(target = "emails", ignore = true)
    void updateUser(@MappingTarget User toUpdate, UserUpdateRequest request);

    default List<String> mapEmails(Set<EmailData> emails) {
        if (emails == null) return new ArrayList<>();
        return emails.stream()
                .map(EmailData::getEmail)
                .collect(Collectors.toList());
    }

    default List<Long> mapPhones(Set<PhoneData> phones) {
        if (phones == null) return new ArrayList<>();
        return phones.stream()
                .map(PhoneData::getPhone)
                .collect(Collectors.toList());
    }

    default void updateUserWithContacts(@MappingTarget User user, UserUpdateRequest request) {
        updateUser(user, request);

        if (request.phone() != null) {
            PhoneData phoneData = new PhoneData();
            phoneData.setPhone(Long.parseLong(request.phone()));
            phoneData.setUser(user);
            user.getPhones().add(phoneData);
        }

        if (request.email() != null && !request.email().isBlank()) {
            EmailData emailData = new EmailData();
            emailData.setEmail(request.email());
            emailData.setUser(user);
            user.getEmails().add(emailData);
        }
    }

    default void removeContacts(User user, String email, String phone) {
        if (email != null && !email.isBlank()) {
            user.getEmails().removeIf(e -> email.equalsIgnoreCase(e.getEmail()));
        }
        if (phone != null) {
            Long phoneNumber = Long.parseLong(phone);
            user.getPhones().removeIf(e -> phoneNumber.equals(e.getPhone()));
        }
    }

    default UserSearchDto toUserSearchDto(Page<UserDto> page) {
        return new UserSearchDto(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }
}
