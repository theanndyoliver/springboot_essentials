package academy.devdojo.springboot2.requests;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class AnimePutRequestBody {
    private Long id;

    @NotEmpty(message = "The name anime cannot  be empty")
    private String name;



}
