package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.util.AnimeCreatorPostRequestBody;
import academy.devdojo.springboot2.util.AnimeCreatorPutRequestBody;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Anime Service")
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepositoryMock;


    @BeforeEach
    void setup() {

        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);

        BDDMockito.when(animeRepositoryMock.findAll()).thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class))).thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString())).thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));


    }

    @Test
    @DisplayName("FindAllPageable Animes - When Sucess")
    void findAllPageable_Animes_WhenSucess() {



        String name = AnimeCreator.createValidAnime().getName();

        Page<Anime> animePage = animeService.listAll(PageRequest.of(1,1));

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(name);




    }



    @Test
    @DisplayName("FindAll Animes - When Sucess")
    void findAll_Animes_WhenSucess() {

        String name = AnimeCreator.createValidAnime().getName();

        List<Anime> animeList = animeService.FullList();

        Assertions.assertThat(animeList).isNotNull().isNotEmpty();

        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(name);

    }


    @Test
    @DisplayName("Save Anime - When Sucess")
    void Save_Anime_WhenSucess() {

        Anime saved = animeService.save(AnimeCreatorPostRequestBody.createAnimePostRequestBody());

        Assertions.assertThat(saved).isNotNull().isEqualTo(AnimeCreator.createValidAnime());

        Assertions.assertThat(saved.getId()).isEqualTo(AnimeCreator.createValidAnime().getId());


    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException Anime - When Sucess")
    void findByIdOrThrowBadRequestException_Anime_WhenSucess() {

        Anime saved = animeService.save(AnimeCreatorPostRequestBody.createAnimePostRequestBody());

        Long id = saved.getId();

        Anime anime = animeService.findByIdOrThrowBadRequestException(id);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getName()).isEqualTo(saved.getName());
        Assertions.assertThat(anime.getId()).isEqualTo(saved.getId());


    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException Anime BadRequestException ")
    void findByIdOrThrowBadRequestException_BadRequestException_Anime_WhenSucess() {

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy( () ->animeService.findByIdOrThrowBadRequestException(1));



    }




    @Test
    @DisplayName("FindByName Anime - When Sucess")
    void FindByName_Anime_WhenSucess() {

        Anime saved = animeService.save(AnimeCreatorPostRequestBody.createAnimePostRequestBody());

        String name = saved.getName();

        List<Anime> buscar = animeService.findByName(name);

        Assertions.assertThat(buscar).isNotNull().isNotEmpty();
        Assertions.assertThat(buscar.get(0).getName()).isEqualTo(name);


    }

    @Test
    @DisplayName("findByName returns an empty list of anime when anime not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimeNotFoundSucessful() {

        BDDMockito.when(animeService.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());


        List<Anime> animeFindByName = animeService.findByName("anime");


        Assertions.assertThat(animeFindByName).isNotNull().isEmpty();

    }



    @Test
    @DisplayName("DeleteById Anime - When Sucess")
    void DeleteById_Anime_WhenSucess() {

        Anime saved = animeService.save(AnimeCreatorPostRequestBody.createAnimePostRequestBody());

        Long id = saved.getId();

        Assertions.assertThatCode(() -> animeService.deleteById(id)).doesNotThrowAnyException();

    }

    @Test
    @DisplayName("Replace Update Anime - When Sucess")
    void ReplaceUpdate_Anime_WhenSucess() {

        Anime saved = animeService.save(AnimeCreatorPostRequestBody.createAnimePostRequestBody());

        Assertions.assertThatCode( () -> animeService.replacex(AnimeCreatorPutRequestBody.createAnimePutRequestBody()))
                .doesNotThrowAnyException();


    }
}