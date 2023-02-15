//package com.epam.esm.exceptionhandler.controller;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
//@RestController
//public class ExceptionController implements ErrorController {
//
//    @RequestMapping("/error")
//    public ResponseEntity<?> handleError(final HttpServletRequest request,
//                                         final HttpServletResponse response) {
//
//        return new ResponseEntity<>(Map.of("HTTP Status", "" + HttpStatus.FORBIDDEN, "response body", Map.of("message", "invalid token detected or token has already expired")), HttpStatus.FORBIDDEN);
//    }
//
//}
