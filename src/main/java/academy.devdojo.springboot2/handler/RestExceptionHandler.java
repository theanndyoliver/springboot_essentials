package academy.devdojo.springboot2.handler;

import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.exception.BadRequestExceptionDetails;
import academy.devdojo.springboot2.exception.ValidationExceptioDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
public class RestExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException exception) {
        return new ResponseEntity<>(BadRequestExceptionDetails.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Bad Request Exception,check the documentation!")
                .details(exception.getLocalizedMessage()).developerMessage(exception.getClass().getName())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptioDetails> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
       List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
       String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
       String fieldsmsg = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

        return new ResponseEntity<>
                (ValidationExceptioDetails.builder().timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Bad Request Exception,Invalid fields!").details("Check the fields error!")
                        .developerMessage(exception.getClass().getName()).fields(fields)
                        .fieldsMessage(fieldsmsg).build(),HttpStatus.BAD_REQUEST);

    }

}

