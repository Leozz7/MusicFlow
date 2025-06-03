package entity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MusicaService {

    public <T extends Musica> void ordenar(List<T> musicas, String campo, boolean asc) {
        Comparator<T> comparator = switch (campo.toLowerCase()) {
            case "titulo" -> Comparator.comparing(Musica::getTitulo, String.CASE_INSENSITIVE_ORDER);
            case "artista" -> Comparator.comparing(Musica::getArtista, String.CASE_INSENSITIVE_ORDER);
            case "album" -> Comparator.comparing(Musica::getAlbum, String.CASE_INSENSITIVE_ORDER);
            case "genero" -> Comparator.comparing(Musica::getGenero, String.CASE_INSENSITIVE_ORDER);
            case "ano" -> Comparator.comparingInt(Musica::getAno);
            default -> throw new IllegalArgumentException("Campo inválido: " + campo);
        };

        if (!asc) comparator = comparator.reversed();
        Collections.sort(musicas, comparator);
    }
}
