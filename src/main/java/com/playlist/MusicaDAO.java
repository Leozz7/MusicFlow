package com.playlist;

import com.playlist.Conexao;
import entity.Musica;

import java.sql.*;
import java.util.*;

public class MusicaDAO {

    public void criarTabelaMusica() {
        String sql = """
                CREATE TABLE IF NOT EXISTS Musica (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    titulo VARCHAR(255) NOT NULL,
                    artista VARCHAR(255) NOT NULL,
                    album VARCHAR(255),
                    genero VARCHAR(50),
                    ano INT
                );
                """;
        try (Connection conn = Conexao.getConexao(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabela Musica, ou ja existe ");
        }
    }

    public void criarPlaylist(String nome) {
        String sql = "CREATE TABLE IF NOT EXISTS " + nome +
                " (id INT AUTO_INCREMENT PRIMARY KEY, titulo VARCHAR(255), artista VARCHAR(255), album VARCHAR(255), genero VARCHAR(50), ano INT)";
        try (Connection conn = Conexao.getConexao();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Playlist '" + nome + "' criada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao criar a playlist: " + e.getMessage());
        }
    }

    public String exibirPlaylist(String nome) {
        StringBuilder play = new StringBuilder();
        String sql = "SELECT id, titulo, artista, album, genero, ano FROM " + nome;

        try (Connection conn = Conexao.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            play.append(String.format("%-5s | %-15s | %-15s | %-10s | %-10s | %-4s%n",
                    "ID", "Título", "Artista", "Álbum", "Gênero", "Ano"));
            play.append("-------------------------------------------------------------------------------------------").append(System.lineSeparator());

            while (rs.next()) {
                play.append(String.format("%-5d | %-15s | %-15s | %-10s | %-10s | %-4d%n",
                        rs.getInt("id"), rs.getString("titulo"), rs.getString("artista"), rs.getString("album"), rs.getString("genero"), rs.getInt("ano")));
            }
        } catch (SQLException e) {
            return "Erro ao exibir a playlist: " + e.getMessage();
        }
        return play.toString();
    }

    public void listarPlaylists() {
        String sql = "SHOW TABLES";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Playlists disponíveis:");
            while (rs.next()) {
                System.out.println("- " + rs.getString(1));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao exibir playlists: " + e.getMessage());
        }
    }

    public void adicionarMusicaAPlaylist(String playlist, int idMusica) {
        String sql = "INSERT INTO " + playlist + " (titulo, artista, album, genero, ano) SELECT titulo, artista, album, genero, ano FROM Musica WHERE id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMusica);

            if (ps.executeUpdate() > 0) {
                System.out.println("Música adicionada à playlist com sucesso!");
            } else {
                System.out.println("Erro ao adicionar música à playlist.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar música à playlist: " + e.getMessage());
        }
    }

    public void adicionarMusica(Musica m) {
        String sql = "INSERT INTO Musica (titulo, artista, album, genero, ano) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getTitulo());
            ps.setString(2, m.getArtista());
            ps.setString(3, m.getAlbum());
            ps.setString(4, m.getGenero());
            ps.setInt(5, m.getAno());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar música: " + e.getMessage());
        }
    }

    public List<Musica> listarMusicas() {
        List<Musica> musicas = new ArrayList<>();
        String sql = "SELECT * FROM Musica";
        try (Connection conn = Conexao.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Musica m = new Musica(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("artista"),
                        rs.getString("album"),
                        rs.getString("genero"),
                        rs.getInt("ano")
                );
                musicas.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar músicas: " + e.getMessage());
        }
        return musicas;
    }

    public List<Musica> buscarPorCampo(String campo, String valor) {
        List<Musica> musicas = new ArrayList<>();
        List<String> camposValidos = Arrays.asList("titulo", "artista", "album", "genero");

        if (!camposValidos.contains(campo.toLowerCase())) {
            System.err.println("Campo inválido para busca.");
            return musicas;
        }

        String sql = "SELECT * FROM Musica WHERE " + campo + " LIKE ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + valor + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Musica m = new Musica(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("artista"),
                        rs.getString("album"),
                        rs.getString("genero"),
                        rs.getInt("ano")
                );
                musicas.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar músicas: " + e.getMessage());
        }
        return musicas;
    }

    public void deletarMusica(int id) {
        String sql = "DELETE FROM Musica WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar música: " + e.getMessage());
        }
    }
}
