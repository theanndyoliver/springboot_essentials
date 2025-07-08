package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.AnimeCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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


}