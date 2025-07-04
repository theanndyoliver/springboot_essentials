package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime,Long> {

    List<Anime> findByName(String name);

    @Query("select a.name from Anime a")
    List<AnimePostRequestBody> findAllNames();


}
