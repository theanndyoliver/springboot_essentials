package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;

public class AnimeCreatorPostRequestBody {

    public static AnimePostRequestBody createAnimePostRequestBody() {
        return AnimePostRequestBody.builder().name(AnimeCreator.createAnimeToBeSaveD().getName()).build();
    }
}
