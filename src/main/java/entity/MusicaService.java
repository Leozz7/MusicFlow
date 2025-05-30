package entity;

import entity.Musica;
import java.util.Comparator;
import java.util.List;

public class MusicaService {

    public void ordenarPorTitulo(List<Musica> musicas, boolean asc) {
        Comparator<Musica> comp = Comparator.comparing(Musica::getTitulo, String.CASE_INSENSITIVE_ORDER);
        musicas.sort(asc ? comp : comp.reversed());
    }

    public void ordenarPorArtista(List<Musica> musicas, boolean asc) {
        Comparator<Musica> comp = Comparator.comparing(Musica::getArtista, String.CASE_INSENSITIVE_ORDER);
        musicas.sort(asc ? comp : comp.reversed());
    }

    public void ordenarPorAno(List<Musica> musicas, boolean asc) {
        Comparator<Musica> comp = Comparator.comparingInt(Musica::getAno);
        musicas.sort(asc ? comp : comp.reversed());
    }
}
