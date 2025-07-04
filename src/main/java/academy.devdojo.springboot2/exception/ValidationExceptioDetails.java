package academy.devdojo.springboot2.exception;


import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ValidationExceptioDetails extends  ExceptionDetails {
    private final String fields;
    private final String fieldsMessage;
}
