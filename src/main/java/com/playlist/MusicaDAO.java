package com.playlist;

import entity.Musica;
import entity.MusicaService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MusicaDAO {

    public void criarDatabase() {
        String sql = "CREATE DATABASE IF NOT EXISTS musica";

        try (Connection conn = Conexao.getConexao();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Database criada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao criar database ou database já existe");
        }
    }

    public List<Musica> listarTodasMusicas() {
        List<Musica> musicas = new ArrayList<>();
        String sql = "SELECT * FROM Musica";

        try (Connection conn = Conexao.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Musica m = new Musica();
                m.setId(rs.getInt("id"));
                m.setTitulo(rs.getString("titulo"));
                m.setArtista(rs.getString("artista"));
                m.setAlbum(rs.getString("album"));
                m.setGenero(rs.getString("genero"));
                m.setAno(rs.getInt("ano"));
                musicas.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return musicas;
    }

    public List<Musica> listarMusicasOrdenadasPor(String campo, boolean asc) {
        List<Musica> musicas = listarTodasMusicas();
        MusicaService service = new MusicaService();

        switch (campo.toLowerCase()) {
            case "titulo":
                service.ordenarPorTitulo(musicas, asc);
                break;
            case "artista":
                service.ordenarPorArtista(musicas, asc);
                break;
            case "ano":
                service.ordenarPorAno(musicas, asc);
                break;
            default:
                //Não faz nada
        }
        return musicas;
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

    public String exibirMusicas() {
        return exibirPlaylist("Musica");
    }

    public String buscarMusica(String parametro, String valor) {
        StringBuilder bm = new StringBuilder();
        String sql = "SELECT id, titulo, artista, album, genero, ano FROM Musica WHERE " + parametro + " = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, valor);
            try (ResultSet rs = ps.executeQuery()) {
                bm.append(String.format("%-5s | %-15s | %-15s | %-10s | %-10s | %-4s%n",
                        "ID", "Título", "Artista", "Álbum", "Gênero", "Ano"));
                bm.append("-------------------------------------------------------------------------------------------").append(System.lineSeparator());

                while (rs.next()) {
                    bm.append(String.format("%-5d | %-15s | %-15s | %-10s | %-10s | %-4d%n",
                            rs.getInt("id"), rs.getString("titulo"), rs.getString("artista"), rs.getString("album"), rs.getString("genero"), rs.getInt("ano")));
                }
            }
        } catch (SQLException e) {
            return "Erro ao buscar música: " + e.getMessage();
        }
        return bm.toString();
    }

    public void deletarMusica(String nome, int id) {
        String verificarSql = "SELECT id FROM " + nome + " WHERE id = ?";
        String deletarSql = "DELETE FROM " + nome + " WHERE id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement verificarPs = conn.prepareStatement(verificarSql)) {

            verificarPs.setInt(1, id);
            ResultSet rs = verificarPs.executeQuery();

            if (!rs.next()) {
                System.out.println("Música com ID " + id + " não encontrada.");
                return;
            }

            try (PreparedStatement deletarPs = conn.prepareStatement(deletarSql)) {
                deletarPs.setInt(1, id);
                deletarPs.executeUpdate();
                System.out.println("Música deletada com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar música: " + e.getMessage());
        }
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

    public void adicionarMusica(String titulo, String artista, String album, String genero, int ano) {
        String sql = "INSERT INTO Musica (titulo, artista, album, genero, ano) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, titulo);
            ps.setString(2, artista);
            ps.setString(3, album);
            ps.setString(4, genero);
            ps.setInt(5, ano);

            if (ps.executeUpdate() > 0) {
                System.out.println("Música adicionada com sucesso!");
            } else {
                System.out.println("Erro ao adicionar música.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar música: " + e.getMessage());
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
}
