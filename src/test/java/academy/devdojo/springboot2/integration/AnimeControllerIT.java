package academy.devdojo.springboot2.integration;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.domain.DomainUsers;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.repository.DomainUsersRepository;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.util.AnimeCreatorPostRequestBody;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
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
    @Qualifier(value = "testRestTemplateRoleUserCreator")
    private TestRestTemplate testRestTemplateRoleUser;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdminCreator")
    private TestRestTemplate testRestTemplateRoleAdmin;

    @Autowired
    private AnimeRepository animeRepository;

    private static final DomainUsers USER = DomainUsers.builder().name("devanndy")
            .password("$2a$10$6FBqvp1y9WA6Qmfbi8nAtelJao.bRkrKSwFYH0nnm4fSxsQhsrmrS")
            .username("devuser")
            .authorities("ROLE_USER").build();

    private static final DomainUsers ADMIN2 = DomainUsers.builder().name("devanndy")
            .password("$2a$10$6FBqvp1y9WA6Qmfbi8nAtelJao.bRkrKSwFYH0nnm4fSxsQhsrmrS")
            .username("admin2")
            .authorities("ROLE_USER,ROLE_ADMIN").build();

    @Autowired
    private DomainUsersRepository domainUsersRepository;

    @TestConfiguration
    @Lazy
    static class Config {
        @Bean(name = "testRestTemplateRoleUserCreator")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}")int port){
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("devuser","1234");

            return new TestRestTemplate(restTemplateBuilder);

        }
    }

    @TestConfiguration
    @Lazy
    static class Config2 {
        @Bean(name = "testRestTemplateRoleAdminCreator")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}")int port){
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("admin2","1234");

            return new TestRestTemplate(restTemplateBuilder);

        }
    }


    @Test
    @DisplayName("List returns list of animes inside page object when sucessful")
    void list_ReturnsListOfAnimesInsidePageObjects_WhenSucessful() {

       Anime SavedAnime =  animeRepository.save(AnimeCreator.createAnimeToBeSaveD());

        domainUsersRepository.save(USER);

        String expectedname = SavedAnime.getName();

       PageableResponse<Anime> animePage = testRestTemplateRoleUser.exchange("/animes", HttpMethod.GET,
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

        domainUsersRepository.save(USER);



        String expectedname = SavedAnime.getName();

        List<Anime> animeFullListNonpage = testRestTemplateRoleUser.exchange("/animes/all", HttpMethod.GET,
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

        domainUsersRepository.save(USER);

        Long expectedId = SavedAnime.getId();

        Anime animeFindById = testRestTemplateRoleUser.getForObject("/animes/{id}",Anime.class,expectedId);


        Assertions.assertThat(animeFindById).isNotNull();
        ;

        Assertions.assertThat(animeFindById.getId()).isNotNull().isEqualTo(expectedId);

    }

    @Test
    @DisplayName("findByName returns a list of anime when sucessful")
    void findByName_ReturnsListOfAnime_WhenSucessful() {

        Anime SavedAnime =  animeRepository.save(AnimeCreator.createAnimeToBeSaveD());

        domainUsersRepository.save(USER);

        String expectedName = SavedAnime.getName();
        String url = String.format("/animes/find?name=%s",expectedName);
        List<Anime> animeFindByName = testRestTemplateRoleUser.exchange(url, HttpMethod.GET,
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

        domainUsersRepository.save(USER);

        List<Anime> animeFindByName = testRestTemplateRoleUser.exchange("/animes/find?name=borutofinal", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();


        Assertions.assertThat(animeFindByName).isNotNull().isEmpty();


    }


    @Test
    @DisplayName("SaveAnime returns anime when Sucessful")
    void SaveAnime_ReturnsAnime_WhenSucessful() {

        AnimePostRequestBody animePostRequestBody = AnimeCreatorPostRequestBody.createAnimePostRequestBody();

        domainUsersRepository.save(ADMIN2);

        ResponseEntity<Anime> animeResponseEntity = testRestTemplateRoleAdmin.postForEntity
                ("/animes/admin/",animePostRequestBody,Anime.class);


        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();


    }

    @Test
    @DisplayName("Replace update anime when Sucessful")
    void Replace_UpdateAnime_WhenSucessful() {

        Anime SavedAnime =  animeRepository.save(AnimeCreator.createAnimeToBeSaveD());

        domainUsersRepository.save(USER);


        SavedAnime.setName("KLB");

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.
                exchange("/animes",HttpMethod.PUT,new HttpEntity<>(SavedAnime),Void.class);


        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);



    }

    @Test
    @DisplayName("Delete removes anime when Sucessful")
    void Delete_RemovesAnime_WhenSucessful() {

        Anime SavedAnime =  animeRepository.save(AnimeCreator.createAnimeToBeSaveD());

        domainUsersRepository.save(ADMIN2);


        SavedAnime.setName("KLB");

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleAdmin.
                exchange("/animes/admin/remove/{id}",HttpMethod.DELETE,null,Void.class,SavedAnime.getId());


        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }


    @Test
    @DisplayName("WhenUserIsNotAdmin")
    void Delete2_Returns403_WhenUserIsNotAdmin() {

        Anime SavedAnime =  animeRepository.save(AnimeCreator.createAnimeToBeSaveD());

        domainUsersRepository.save(USER);


        SavedAnime.setName("KLB");

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.
                exchange("/animes/admin/remove/{id}",HttpMethod.DELETE,null,Void.class,SavedAnime.getId());


        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }




}
