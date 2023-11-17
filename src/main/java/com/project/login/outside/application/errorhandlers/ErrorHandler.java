package com.project.login.outside.application.errorhandlers;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.project.login.domain.exceptions.EmailServiceException;
import com.project.login.domain.exceptions.UserAuthServiceException;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle("Um ou mais campos inválidos");
        problemDetail.setType(URI.create("https://login-server.com/erros/campos-invalidos"));

        var fields = ex.getBindingResult()
            .getAllErrors()
            .stream()
            .collect(Collectors.toMap(
                objectError -> ((FieldError) objectError).getField(),
                objectError -> messageSource.getMessage(
                    objectError,
                    LocaleContextHolder.getLocale()
                )));

        problemDetail.setProperty("fields", fields);

        return handleExceptionInternal(ex, problemDetail, headers, status, request);
    }

    /**
     * Exception class is used in my application by WebSecurityConfig
     * this method is made to handle exceptions from there
     *
     * @param exception
     * @return response in problemDetail pattern
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle(exception.getMessage());
        problemDetail.setType(URI.create("https://login-server.com/error/web-security-config"));
        return problemDetail;
    }

    /**
     * All exceptions of these types come from SecurityFilter on my application
     *
     * @param exception
     * @return response in problemDetail pattern
     */
    @ExceptionHandler({ServletException.class, IOException.class})
    public ProblemDetail handleSecurityFilter(Exception exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle(exception.getMessage());
        problemDetail.setDetail("Problem is in Method -> doFilterInternal, inside SecurityFilter Class");
        problemDetail.setType(URI.create("https://login-server.com/error/security-filter"));
        return problemDetail;
    }

    /**
     * In case someone tries to delete a user that cannot be deleted inside the
     * database
     *
     * @param exception
     * @return response in problemDetail pattern
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle("Resource is being used");
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setType(URI.create("https://login-server.com/error/data-conflict"));
        return problemDetail;
    }

    /**
     * handles JWT api exceptions
     *
     * @param exception
     * @return response in problemDetail pattern
     */
    @ExceptionHandler(JWTCreationException.class)
    public ProblemDetail handleJwtTokens(RuntimeException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle(exception.getMessage());
        problemDetail.setType(URI.create("https://login-server.com/error/jwt-tokens"));
        return problemDetail;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ProblemDetail handleUsernameNotFound(UsernameNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle(exception.getMessage());
        problemDetail.setType(URI.create("https://login-server.com/error/user-not-found"));
        return problemDetail;
    }

    /**
     * if EmailService fails to send a email for any reason
     *
     * @param exception
     * @return response in problemDetail pattern
     */
    @ExceptionHandler(EmailServiceException.class)
    public ProblemDetail handleEmailService(EmailServiceException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle(exception.getMessage());
        problemDetail.setDetail("For some unknown reason the email could not be sent," +
            " problem happened on method sendEmail inside EmailSenderService");
        problemDetail.setType(URI.create("https://login-server.com/error/email-not-sent"));
        return problemDetail;
    }

    @ExceptionHandler(UserAuthServiceException.class)
    public ProblemDetail handleUserAuth(UserAuthServiceException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle(exception.getMessage());
        problemDetail.setDetail("For some unknown reason a Empty User reached register method inside UserAuthService");
        problemDetail.setType(URI.create("https://login-server.com/error/user-empty"));
        return problemDetail;
    }
}
