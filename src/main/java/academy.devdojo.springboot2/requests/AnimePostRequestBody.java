package academy.devdojo.springboot2.requests;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class AnimePostRequestBody {

    @NotEmpty(message = "The name anime cannot be empty")
    private String name;
}


