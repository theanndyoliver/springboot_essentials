package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.util.AnimeCreatorPostRequestBody;
import academy.devdojo.springboot2.util.AnimeCreatorPutRequestBody;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Anime Controller")
class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeServiceMock;


    @BeforeEach
    void setup() {
       PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);

        BDDMockito.when(animeServiceMock.FullList())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.doNothing().when(animeServiceMock).replacex(ArgumentMatchers.any(AnimePutRequestBody.class));

        BDDMockito.doNothing().when(animeServiceMock).deleteById(ArgumentMatchers.anyLong());



    }
    @Test
    @DisplayName("List returns list of animes inside page object when sucessful")
    void list_ReturnsListOfAnimesInsidePageObjects_WhenSucessful() {
        String expectedname = AnimeCreator.createValidAnime().getName();
        Page<Anime> animePage = animeController.list(null).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList()).isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList()).isNotNull();

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedname);

    }

    @Test
    @DisplayName("ListAll returns list of animes when sucessful")
    void listAll_ReturnsListOfAnimes_WhenSucessful() {
        String expectedname = AnimeCreator.createValidAnime().getName();
        List<Anime> animeFullListNonpage = animeController.FullList().getBody();


        Assertions.assertThat(animeFullListNonpage).isNotEmpty().isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeFullListNonpage.get(0).getName()).isEqualTo(expectedname);

    }

    @Test
    @DisplayName("findById returns anime when sucessful")
    void findById_ReturnsAnime_WhenSucessful() {
        Long expectedId = AnimeCreator.createValidAnime().getId();

        Anime animeFindById = animeController.findById(expectedId).getBody();


        Assertions.assertThat(animeFindById).isNotNull();
                ;

        Assertions.assertThat(animeFindById.getId()).isNotNull().isEqualTo(expectedId);

    }

    @Test
    @DisplayName("findByName returns a list of anime when sucessful")
    void findByName_ReturnsListOfAnime_WhenSucessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animeFindById = animeController.findByName(expectedName).getBody();


        Assertions.assertThat(animeFindById).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(animeFindById.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findByName returns an empty list of anime when anime not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimeNotFoundSucessful() {

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());



        List<Anime> animeFindById = animeController.findByName("anime").getBody();


        Assertions.assertThat(animeFindById).isNotNull().isEmpty();


    }


    @Test
    @DisplayName("SaveAnime returns anime when Sucessful")
    void SaveAnime_ReturnsAnime_WhenSucessful() {

        AnimePostRequestBody animePostRequestBody = AnimeCreatorPostRequestBody.createAnimePostRequestBody();

        Anime savedAnime = animeController.save(animePostRequestBody).getBody();


        Assertions.assertThat(savedAnime).isNotNull().isEqualTo(AnimeCreator.createValidAnime());


    }

    @Test
    @DisplayName("Replace update anime when Sucessful")
    void Replace_UpdateAnime_WhenSucessful() {


        Assertions.assertThatCode(() -> animeController.replacex(AnimeCreatorPutRequestBody.createAnimePutRequestBody()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController.replacex(AnimeCreatorPutRequestBody.createAnimePutRequestBody());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);


    }

    @Test
    @DisplayName("Delete removes anime when Sucessful")
    void Delete_RemovesAnime_WhenSucessful() {


        Assertions.assertThatCode(() -> animeController.deleteById(1))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController.deleteById(1);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);


    }


}