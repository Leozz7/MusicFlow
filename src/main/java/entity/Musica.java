package entity;

public class Musica implements Comparable<Musica> {
    private int id;
    private String titulo;
    private String artista;
    private String album;
    private String genero;
    private int ano;

    public Musica() {}

    public Musica(int id, String titulo, String artista, String album, String genero, int ano) {
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.album = album;
        this.genero = genero;
        this.ano = ano;
    }

    @Override
    public int compareTo(Musica outra) {
        return this.titulo.compareToIgnoreCase(outra.titulo);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }
}