package academy.devdojo.springboot2.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
public class AnimePostRequestBody {

    @NotEmpty(message = "The name anime cannot  be empty")
    private String name;
}


