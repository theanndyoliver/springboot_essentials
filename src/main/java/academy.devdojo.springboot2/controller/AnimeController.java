package academy.devdojo.springboot2.controller;


import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/animes")
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeService animeService;


  @GetMapping
  @Operation(summary = "List all Animes paginated",description = "The default size is 20,use the parameter size to change to default value",
  tags = {"anime"})
  public ResponseEntity<Page<Anime>> list(@ParameterObject Pageable pageable) {
     return ResponseEntity.ok(animeService.listAll(pageable));
 }


    @GetMapping(path = "/all")
    public ResponseEntity<List<Anime>> FullList() {
        return ResponseEntity.ok(animeService.FullList());
    }

    @GetMapping(path = "/names")
    public ResponseEntity<List<AnimePostRequestBody>> listx() {
        return ResponseEntity.ok(animeService.allNames());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable long id) {
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "by-id/{id}")
    public ResponseEntity<Anime> findByIdAuthenticationPrincipal(@PathVariable long id,
                                                                 @AuthenticationPrincipal UserDetails userDetails) {
      log.info(userDetails);
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Anime>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(animeService.findByName(name));
    }

    @PostMapping(path = "/admin/")
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody) {
        return new ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/admin/remove/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204" ,description = "Sucessful operation"),
            @ApiResponse(responseCode = "400" ,description = "When Anime Does Not Exist in The Database")

    })
    public ResponseEntity<Void> deleteById(@PathVariable  long id) {
        animeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> replacex(@RequestBody @Valid AnimePutRequestBody animePutRequestBody) {
        animeService.replacex(animePutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}