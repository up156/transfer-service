package com.transfer.demo.ex.handler;


import com.transfer.demo.dto.ErrorDto;
import com.transfer.demo.ex.AlreadyExistException;
import com.transfer.demo.ex.EmailNotFoundException;
import com.transfer.demo.ex.PhoneNotFoundException;
import com.transfer.demo.ex.UserNotFoundException;
import com.transfer.demo.ex.UserUnauthorizedException;
import com.transfer.demo.ex.WrongFromAmountException;
import com.transfer.demo.ex.WrongTransferTargetException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class TransferExceptionHandler {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class, EmailNotFoundException.class, PhoneNotFoundException.class})
    public ResponseEntity<ErrorDto> handleNotFoundException(Exception ex) {
        log.error("not found ex: {}", ex.getMessage());
        return new ResponseEntity<>(ErrorDto.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UserUnauthorizedException.class})
    public ResponseEntity<ErrorDto> handleNoAuthorityException(UserUnauthorizedException ex) {
        log.error("unauthorized: {}", ex.getMessage());
        return new ResponseEntity<>(ErrorDto.builder()
                .message("unauthorized")
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({WrongTransferTargetException.class, WrongFromAmountException.class, AlreadyExistException.class})
    public ResponseEntity<ErrorDto> handleBadRequestException(Exception ex, WebRequest webRequest) {
        log.error("bad request: {}", ex.getMessage());
        return new ResponseEntity<>(ErrorDto.builder()
                .message("bad request: " + ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            ConstraintViolationException.class, MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class, DataIntegrityViolationException.class
    })
    public ResponseEntity<ErrorDto> handleWrongArgsException(Exception ex, WebRequest webRequest) {
        log.error("wrong args: {}", ex.getMessage());
        return new ResponseEntity<>(ErrorDto.builder()
                .message("bad request")
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> handleOtherException(Exception ex) {
        ex.printStackTrace();
        log.error("some problem: {}", ex.getMessage());
        return new ResponseEntity<>(ErrorDto.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}