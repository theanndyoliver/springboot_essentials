package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.exception.BadRequestException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import academy.devdojo.springboot2.mapper.AnimeMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;


import java.util.List;


@Service
@RequiredArgsConstructor
public class AnimeService {

    //Regras de Negócio//

    private final AnimeRepository animeRepository;

    public Page<Anime> listAll(Pageable pageable) {
        return animeRepository.findAll(pageable);
    }

    public List<Anime> FullList() {
        return animeRepository.findAll();
    }


    public List<AnimePostRequestBody> allNames() {
        return animeRepository.findAllNames();
    }


    public List<Anime> findByName(String name) {
        return animeRepository.findByName(name);
    }

    public Anime findByIdOrThrowBadRequestException(long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Anime not Found"));

    }

    @Transactional
    public Anime save( AnimePostRequestBody animePostRequestBody) {

        return animeRepository.save(AnimeMapper.INSTANCE.toAnime(animePostRequestBody));
       // return animeRepository.save(Anime.builder().name(animePostRequestBody.getName()).build());

    }


    public void deleteById(long id) {
        animeRepository.delete(findByIdOrThrowBadRequestException(id));
    }


    public void replacex(AnimePutRequestBody animePutRequestBody) {
       Anime savedAnime = findByIdOrThrowBadRequestException(animePutRequestBody.getId());
        Anime anime= AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
        anime.setId(savedAnime.getId());
        animeRepository.save(anime);
       // animeRepository.save(Anime.builder().id(savedAnime.getId()).name(animePutRequestBody.getName()).build());



    }


}





