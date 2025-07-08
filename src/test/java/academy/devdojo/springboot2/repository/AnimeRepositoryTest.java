package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import jakarta.validation.ConstraintViolationException;

import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.List;
import java.util.Optional;

import static academy.devdojo.springboot2.util.AnimeCreator.createAnimeToBeSaveD;


//@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
//@ActiveProfiles("test")
@DataJpaTest
@DisplayName("Tests for Anime Repository")
@Log4j2
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;



    @Test
    @DisplayName("Save Persists Anime when sucessful")
    void save_PersistAnime_WhenSucessful() {
        Anime animeToBeSaved = createAnimeToBeSaveD();

        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        Assertions.assertThat(animeSaved).isNotNull();

        Assertions.assertThat(animeSaved.getId()).isNotNull();

        Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSaved.getName());

    }

    @Test
    @DisplayName("Save Updates Anime when sucessful")
    void save_UpdatesAnime_WhenSucessful() {
        Anime animeToBeSaved = createAnimeToBeSaveD();

        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        animeSaved.setName("Overlord");

        Anime animeUpdate = this.animeRepository.save(animeSaved);

        Assertions.assertThat(animeUpdate).isNotNull();

        Assertions.assertThat(animeUpdate.getId()).isNotNull();

        Assertions.assertThat(animeUpdate.getName()).isEqualTo(animeSaved.getName());

    }


    @Test
    @DisplayName("Delete removes Anime when sucessful")
    void Delete_RemoveAnime_WhenSucessful() {
        Anime animeToBeSaved = createAnimeToBeSaveD();

        Anime animeSaved = animeRepository.save(animeToBeSaved);


        this.animeRepository.delete(animeSaved);

        Optional<Anime> animeOptional = this.animeRepository.findById(animeSaved.getId());

        Assertions.assertThat(animeOptional).isEmpty();


    }

    @Test
    @DisplayName("FindByName returns list of Anime when sucessful")
    void FindByName_ReturnsListOfAnime_WhenSucessful() {
        Anime animeToBeSaved = createAnimeToBeSaveD();

        Anime animeSaved = animeRepository.save(animeToBeSaved);

        String name = animeSaved.getName();

        List<Anime> animes = animeRepository.findByName(name);

        Assertions.assertThat(animes).isNotEmpty();

        Assertions.assertThat(animes).contains(animeSaved);


    }

    @Test
    @DisplayName("FindByName returns empty list  no anime is found")
    void FindByName_ReturnsEmptyList_WhenAnimeIsNotFound() {

        List<Anime> animes = animeRepository.findByName("xaxa");

        Assertions.assertThat(animes).isEmpty();


    }


    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    void save_throwConstraintViolationException_WhenNameIsEmpty() {
        Anime anime = new Anime();


       // Assertions.assertThatThrownBy(()->this.animeRepository.saveAndFlush(anime)).isInstanceOf(ConstraintViolationException.class);

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.animeRepository.saveAndFlush(anime));

    }





}











