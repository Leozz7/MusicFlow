import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MusicaDAO {
    public void salvarMusica(String titulo, String artista, String album, String genero, int ano, double duracao, String caminhoArquivo) {
        String sql = "INSERT INTO Musica (titulo, artista, album, genero, ano, duracao, arquivo) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = DriverManager.getConnection("jdbc:mysql://localhost/musica_db", "root", "senha");
             PreparedStatement stmt = conexao.prepareStatement(sql);
             FileInputStream arquivo = new FileInputStream(new File(caminhoArquivo))) {

            stmt.setString(1, titulo);
            stmt.setString(2, artista);
            stmt.setString(3, album);
            stmt.setString(4, genero);
            stmt.setInt(5, ano);
            stmt.setDouble(6, duracao);
            stmt.setBinaryStream(7, arquivo, (int) new File(caminhoArquivo).length());

            stmt.executeUpdate();
            System.out.println("Música salva com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MusicaDAO musicaDAO = new MusicaDAO();
        musicaDAO.salvarMusica("Bohemian Rhapsody", "Queen", "A Night at the Opera", "Rock", 1975, 5.55, "C:/musicas/bohemian.mp3");
    }
}
