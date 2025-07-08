package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;

public class AnimeCreatorPutRequestBody {

    public static AnimePutRequestBody createAnimePutRequestBody() {
        return AnimePutRequestBody.builder().name(AnimeCreator.createValidUpdateAnime().getName()).id(
                AnimeCreator.createValidUpdateAnime().getId()).build();
    }
}
