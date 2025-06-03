package com.playlist;

import entity.Musica;
import entity.MusicaService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MusicaDAO {

    public void inicializarBanco() {
        String criarTabela = """
            CREATE TABLE IF NOT EXISTS Musica (
                id INT AUTO_INCREMENT PRIMARY KEY,
                titulo VARCHAR(255),
                artista VARCHAR(255),
                album VARCHAR(255),
                genero VARCHAR(50),
                ano INT
            );
        """;

        try (Connection conn = Conexao.getConexao(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(criarTabela);
            System.out.println("Banco inicializado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao inicializar banco: " + e.getMessage());
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

    public List<Musica> buscarMusicas() {
        List<Musica> lista = new ArrayList<>();
        String sql = "SELECT * FROM Musica";

        try (Connection conn = Conexao.getConexao(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Musica m = new Musica();
                m.setId(rs.getInt("id"));
                m.setTitulo(rs.getString("titulo"));
                m.setArtista(rs.getString("artista"));
                m.setAlbum(rs.getString("album"));
                m.setGenero(rs.getString("genero"));
                m.setAno(rs.getInt("ano"));
                lista.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar músicas: " + e.getMessage());
        }

        return lista;
    }

    public List<Musica> buscarPorCampo(String campo, String valor) {
        List<Musica> lista = new ArrayList<>();
        String sql = "SELECT * FROM Musica WHERE " + campo + " LIKE ?";

        try (Connection conn = Conexao.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + valor + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Musica m = new Musica();
                m.setId(rs.getInt("id"));
                m.setTitulo(rs.getString("titulo"));
                m.setArtista(rs.getString("artista"));
                m.setAlbum(rs.getString("album"));
                m.setGenero(rs.getString("genero"));
                m.setAno(rs.getInt("ano"));
                lista.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar: " + e.getMessage());
        }

        return lista;
    }

    public void deletarMusica(int id) {
        String sql = "DELETE FROM Musica WHERE id = ?";

        try (Connection conn = Conexao.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar: " + e.getMessage());
        }
    }
}
