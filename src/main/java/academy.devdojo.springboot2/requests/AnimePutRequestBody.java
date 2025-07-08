package academy.devdojo.springboot2.requests;



import jakarta.validation.constraints.NotEmpty;
import lombok.Data;




@Data
public class AnimePutRequestBody {
    private Long id;

    @NotEmpty(message = "The name anime cannot be empty")
    private String name;



}
