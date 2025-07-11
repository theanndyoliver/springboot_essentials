package academy.devdojo.springboot2.integration;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.util.AnimeCreatorPostRequestBody;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode  = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AnimeControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;

    @Autowired
    private AnimeRepository animeRepository;


    @Test
    @DisplayName("List returns list of animes inside page object when sucessful")
    void list_ReturnsListOfAnimesInsidePageObjects_WhenSucessful() {

       Anime SavedAnime =  animeRepository.save(AnimeCreator.createAnimeToBeSaveD());

        String expectedname = SavedAnime.getName();

       PageableResponse<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET,
               null, new ParameterizedTypeReference<PageableResponse<Anime>>() {
        }).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList()).isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList()).isNotNull();

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedname);

    }

    @Test
    @DisplayName("ListAll returns list of animes when sucessful")
    void listAll_ReturnsListOfAnimes_WhenSucessful() {
        Anime SavedAnime =  animeRepository.save(AnimeCreator.createAnimeToBeSaveD());

        String expectedname = SavedAnime.getName();

        List<Anime> animeFullListNonpage = testRestTemplate.exchange("/animes/all", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();


        Assertions.assertThat(animeFullListNonpage).isNotNull().isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeFullListNonpage.get(0).getName()).isEqualTo(expectedname);

    }

    @Test
    @DisplayName("findById returns anime when sucessful")
    void findById_ReturnsAnime_WhenSucessful() {
        Anime SavedAnime =  animeRepository.save(AnimeCreator.createAnimeToBeSaveD());

        Long expectedId = SavedAnime.getId();

        Anime animeFindById = testRestTemplate.getForObject("/animes/{id}",Anime.class,expectedId);


        Assertions.assertThat(animeFindById).isNotNull();
        ;

        Assertions.assertThat(animeFindById.getId()).isNotNull().isEqualTo(expectedId);

    }

    @Test
    @DisplayName("findByName returns a list of anime when sucessful")
    void findByName_ReturnsListOfAnime_WhenSucessful() {
        Anime SavedAnime =  animeRepository.save(AnimeCreator.createAnimeToBeSaveD());

        String expectedName = SavedAnime.getName();
        String url = String.format("/animes/find?name=%s",expectedName);
        List<Anime> animeFindByName = testRestTemplate.exchange(url, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();


        Assertions.assertThat(animeFindByName)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeFindByName.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findByName returns an empty list of anime when anime not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimeNotFoundSucessful() {



        List<Anime> animeFindByName = testRestTemplate.exchange("/animes/find?name=borutofinal", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();


        Assertions.assertThat(animeFindByName).isNotNull().isEmpty();


    }


    @Test
    @DisplayName("SaveAnime returns anime when Sucessful")
    void SaveAnime_ReturnsAnime_WhenSucessful() {

        AnimePostRequestBody animePostRequestBody = AnimeCreatorPostRequestBody.createAnimePostRequestBody();

        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.postForEntity
                ("/animes",animePostRequestBody,Anime.class);


        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();


    }

    @Test
    @DisplayName("Replace update anime when Sucessful")
    void Replace_UpdateAnime_WhenSucessful() {
        Anime SavedAnime =  animeRepository.save(AnimeCreator.createAnimeToBeSaveD());


        SavedAnime.setName("KLB");

        ResponseEntity<Void> animeResponseEntity = testRestTemplate.
                exchange("/animes",HttpMethod.PUT,new HttpEntity<>(SavedAnime),Void.class);


        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);



    }

    @Test
    @DisplayName("Delete removes anime when Sucessful")
    void Delete_RemovesAnime_WhenSucessful() {

        Anime SavedAnime =  animeRepository.save(AnimeCreator.createAnimeToBeSaveD());


        SavedAnime.setName("KLB");

        ResponseEntity<Void> animeResponseEntity = testRestTemplate.
                exchange("/animes/remove/{id}",HttpMethod.DELETE,null,Void.class,SavedAnime.getId());


        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }




}
