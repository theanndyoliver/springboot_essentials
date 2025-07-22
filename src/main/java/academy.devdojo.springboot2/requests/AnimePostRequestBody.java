package academy.devdojo.springboot2.requests;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnimePostRequestBody {

    @NotEmpty(message = "The name anime cannot be empty")
    @Schema(description = "This is the Anime's name.",example = "Naruto Shippuden", required = true)
    private String name;
}


