package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.domain.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSaveD() {
        return Anime.builder().name("Hajime no Ippo").build();
    }

    public static Anime createValidAnime() {
        return Anime.builder().name("Hajime no Ippo").id(1l).build();
    }

    public static Anime createValidUpdateAnime() {
        return Anime.builder().name("Hajime no Ippo 2").id(1l).build();
    }
}
