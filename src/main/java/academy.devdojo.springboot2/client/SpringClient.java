package academy.devdojo.springboot2.client;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.mapper.AnimeMapper;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@ResponseBody
@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Anime> entity= new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class,4);
        log.info(entity);

        Anime object = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class,4);
        log.info(object);

        Anime[] animes = new RestTemplate().getForObject("http://localhost:8080/animes/all", Anime[].class);
        log.info(Arrays.toString(animes));


        ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange("http://localhost:8080/animes/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {});
        log.info(exchange.getBody());


      //  Anime kingdom = Anime.builder().name("kingdom").build();
       // Anime savedAnime =new RestTemplate().postForObject("http://localhost:8080/animes",kingdom,Anime.class);
       // log.info("saved anime {}",savedAnime);


        //Anime samuraiChamploo = Anime.builder().name("Samurai Champloo").build();
       // ResponseEntity<Anime> samuraiChamplooSaved =new RestTemplate().exchange("http://localhost:8080/animes",HttpMethod.POST,
               // new HttpEntity<>(samuraiChamploo,createJsonHeader()),Anime.class);
        //log.info("saved anime {}",samuraiChamplooSaved);



        Anime bokuNoHero = Anime.builder().id(1302l).name("Boku no Hero - Temporada").build();

        ResponseEntity<Void> bokuNoHeroUpdate = new RestTemplate().exchange("http://localhost:8080/animes",HttpMethod.PUT,
                new HttpEntity<>(bokuNoHero,createJsonHeader()),Void.class);
        log.info(bokuNoHeroUpdate);


        Anime deleteAnimeXBB = Anime.builder().id(1352l).build();
        ResponseEntity<Void> animeDeleteXD = new RestTemplate().exchange("http://localhost:8080/animes/remove/{id}",HttpMethod.DELETE,
                null,Void.class,deleteAnimeXBB.getId());
        log.info(animeDeleteXD);

    }

    private static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

}
