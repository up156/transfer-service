package com.transfer.demo.controller.impl;


import com.transfer.demo.config.security.JwtUser;
import com.transfer.demo.controller.TransferController;
import com.transfer.demo.dto.TransferResponseDto;
import com.transfer.demo.request.TransferRequest;
import com.transfer.demo.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class TransferControllerImpl implements TransferController {

    private final TransferService transferService;

    @Override
    public ResponseEntity<TransferResponseDto> transfer(JwtUser user, TransferRequest request) {
        return ResponseEntity.ok(transferService.transfer(user, request));
    }
}
