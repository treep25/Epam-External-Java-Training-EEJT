package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST,
        reason = "Bad request body")
public class BadRequestBody extends RuntimeException {

}
