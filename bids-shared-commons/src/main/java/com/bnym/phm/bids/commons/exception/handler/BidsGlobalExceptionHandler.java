package com.bnym.phm.bids.commons.exception.handler;

import com.bnym.phm.bids.commons.exception.BIDSApplicationException;
import com.bnym.phm.bids.commons.exception.model.ApiError;
import com.bnym.phm.bids.commons.exception.model.ApiFieldError;
import feign.Response;
import io.vavr.API;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.parser.Entity;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class BidsGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BIDSApplicationException.class)
    public ResponseEntity<Object> handleBidsApplicationException(BIDSApplicationException e, HttpServletResponse response) throws IOException{
        ApiError apiError = ApiError.builder()
                .status(e.getHttpStatusCode().value())
                .error(e.getHttpStatusCode().getReasonPhrase())
                .message(e.getMessage()).build();
        return new ResponseEntity<>(apiError,e.getHttpStatusCode());

    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> constraintViolationException(ConstraintViolationException e,HttpServletResponse response)throws IOException {
        List<ApiFieldError> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errors.add(new ApiFieldError(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        ApiError apiError = ApiError.builder().status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("One or more errors found").errors(errors).build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request){

        List<ApiFieldError> apiFieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ApiFieldError(fieldError.getField(),fieldError.getCode(),fieldError.getRejectedValue(),fieldError.getDefaultMessage()))
                .collect(toList());

        ApiError apiError = ApiError.builder().status(status.value())
                .error(status.getReasonPhrase())
                .message("One or more errors found").errors(apiFieldErrors).build();
        return new ResponseEntity(apiError,headers,status);
    }

}