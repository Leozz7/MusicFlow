package com.playlist;

import entity.Musica;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MusicFlowGUI {
    private JFrame frame;
    private JPanel panel;
    private MusicaDAO dao;

    public MusicFlowGUI() {
        dao = new MusicaDAO();
        frame = new JFrame("MusicFlow");
        frame.setSize(450, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        panel = new JPanel();
        panel.setLayout(new GridLayout(9, 1, 10, 10));
        panel.setBackground(new Color(20, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        dao.criarDatabase();

        String[] buttonLabels = {
                "Exibir Todas Músicas", "Adicionar Música", "Criar Playlist", "Exibir Playlists",
                "Adicionar Música à Playlist", "Buscar Música", "Deletar Música", "Listar Playlist", "SAIR"
        };

        for (String label : buttonLabels) {
            JButton button = createStyledButton(label);
            panel.add(button);
            addAction(button, label);
        }

        frame.add(panel);
        frame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(50, 50, 50));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    private void addAction(JButton button, String action) {
        button.addActionListener(e -> {
            switch (action) {
                case "Exibir Todas Músicas":
                    JOptionPane.showMessageDialog(frame, dao.exibirMusicas());
                    break;
                case "Adicionar Música":
                    adicionarMusica();
                    break;
                case "Criar Playlist":
                    String nomePlaylist = JOptionPane.showInputDialog("Nome da Playlist:");
                    if (nomePlaylist != null) dao.criarPlaylist(nomePlaylist);
                    break;
                case "Exibir Playlists":
                    dao.listarPlaylists();
                    break;
                case "Adicionar Música à Playlist":
                    adicionarMusicaAPlaylist();
                    break;
                case "Buscar Música":
                    buscarMusica();
                    break;
                case "Deletar Música":
                    deletarMusica();
                    break;
                case "Listar Playlist":
                    listarPlaylist();
                    break;
                case "SAIR":
                    System.exit(0);
                    break;
            }
        });
    }

    private void exibirMusicasOrdenadas() {
        String[] campos = {"titulo", "artista", "ano"};
        String campo = (String) JOptionPane.showInputDialog(frame, "Ordenar por:",
                "Critério de Ordenação", JOptionPane.PLAIN_MESSAGE, null, campos, campos[0]);
        if (campo == null) return;

        int ordem = JOptionPane.showConfirmDialog(frame, "Ordenar de forma crescente? (Não para decrescente)",
                "Direção da Ordenação", JOptionPane.YES_NO_OPTION);
        boolean asc = (ordem == JOptionPane.YES_OPTION);

        List<Musica> musicas = dao.listarMusicasOrdenadasPor(campo, asc);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-5s | %-15s | %-15s | %-10s | %-10s | %-4s%n",
                "ID", "Título", "Artista", "Álbum", "Gênero", "Ano"));
        sb.append("-----------------------------------------------------------------------------\n");

        for (Musica m : musicas) {
            sb.append(String.format("%-5d | %-15s | %-15s | %-10s | %-10s | %-4d%n",
                    m.getId(), m.getTitulo(), m.getArtista(), m.getAlbum(), m.getGenero(), m.getAno()));
        }

        JOptionPane.showMessageDialog(frame, sb.toString());
    }


    private void adicionarMusica() {
        JTextField titulo = new JTextField();
        JTextField artista = new JTextField();
        JTextField album = new JTextField();
        JTextField genero = new JTextField();
        JTextField ano = new JTextField();

        Object[] fields = {"Título:", titulo, "Artista:", artista, "Álbum:", album, "Gênero:", genero, "Ano:", ano};
        int option = JOptionPane.showConfirmDialog(frame, fields, "Adicionar Música", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            dao.adicionarMusica(titulo.getText(), artista.getText(), album.getText(), genero.getText(), Integer.parseInt(ano.getText()));
        }
    }

    private void adicionarMusicaAPlaylist() {
        String nomePlaylist = JOptionPane.showInputDialog("Nome da Playlist:");
        String idMusica = JOptionPane.showInputDialog("ID da Música:");
        if (nomePlaylist != null && idMusica != null) {
            dao.adicionarMusicaAPlaylist(nomePlaylist, Integer.parseInt(idMusica));
        }
    }

    private void buscarMusica() {
        String[] options = {"titulo", "artista", "album", "genero"};
        int choice = JOptionPane.showOptionDialog(frame, "Buscar por:", "Buscar Música",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice >= 0) {
            String parametro = options[choice];
            String valor = JOptionPane.showInputDialog("Digite o " + options[choice] + ":");
            JOptionPane.showMessageDialog(frame, dao.buscarMusica(parametro, valor));
        }
    }

    private void deletarMusica() {
        String nomePlaylist = JOptionPane.showInputDialog("Nome da Playlist:");
        String idMusica = JOptionPane.showInputDialog("ID da Música:");
        if (nomePlaylist != null && idMusica != null) {
            dao.deletarMusica(nomePlaylist, Integer.parseInt(idMusica));
        }
    }

    private void listarPlaylist() {
        String nomePlaylist = JOptionPane.showInputDialog("Nome da Playlist:");
        JOptionPane.showMessageDialog(frame, dao.exibirPlaylist(nomePlaylist));
    }

    public static void main(String[] args) {
        new MusicFlowGUI();
    }
}
