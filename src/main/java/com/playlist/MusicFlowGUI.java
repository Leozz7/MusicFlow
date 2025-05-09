package com.playlist;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import entity.Musica;

public class MusicFlowGUI {
    private JFrame frame;
    private JPanel panel;
    private JButton exibirMusicas, adicionarMusica, criarPlaylist, exibirPlaylists, adicionarMusicaPlaylist, buscarMusica, deletarMusica, listarPlaylist, sair;
    private MusicaDAO dao;

    public MusicFlowGUI() {
        dao = new MusicaDAO();
        frame = new JFrame("MusicFlow");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new GridLayout(9, 1, 10, 10));
        panel.setBackground(new Color(30, 30, 30));

        exibirMusicas = createButton("Exibir Todas Músicas");
        adicionarMusica = createButton("Adicionar Música");
        criarPlaylist = createButton("Criar Playlist");
        exibirPlaylists = createButton("Exibir Playlists");
        adicionarMusicaPlaylist = createButton("Adicionar Música à Playlist");
        buscarMusica = createButton("Buscar Música");
        deletarMusica = createButton("Deletar Música");
        listarPlaylist = createButton("Listar Playlist");
        sair = createButton("SAIR");

        panel.add(exibirMusicas);
        panel.add(adicionarMusica);
        panel.add(criarPlaylist);
        panel.add(exibirPlaylists);
        panel.add(adicionarMusicaPlaylist);
        panel.add(buscarMusica);
        panel.add(deletarMusica);
        panel.add(listarPlaylist);
        panel.add(sair);

        frame.add(panel);
        frame.setVisible(true);

        addActionListeners();
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(50, 50, 50));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void addActionListeners() {
        exibirMusicas.addActionListener(e -> JOptionPane.showMessageDialog(frame, dao.exibirMusicas()));

        adicionarMusica.addActionListener(e -> {
            JTextField titulo = new JTextField();
            JTextField artista = new JTextField();
            JTextField album = new JTextField();
            JTextField genero = new JTextField();
            JTextField ano = new JTextField();

            Object[] fields = {
                    "Título:", titulo,
                    "Artista:", artista,
                    "Álbum:", album,
                    "Gênero:", genero,
                    "Ano:", ano
            };

            int option = JOptionPane.showConfirmDialog(frame, fields, "Adicionar Música", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                dao.adicionarMusica(titulo.getText(), artista.getText(), album.getText(), genero.getText(), Integer.parseInt(ano.getText()));
            }
        });

        criarPlaylist.addActionListener(e -> {
            String nomePlaylist = JOptionPane.showInputDialog("Nome da Playlist:");
            if (nomePlaylist != null) {
                dao.criarPlaylist(nomePlaylist);
            }
        });

        exibirPlaylists.addActionListener(e -> dao.listarPlaylists());

        adicionarMusicaPlaylist.addActionListener(e -> {
            String nomePlaylist = JOptionPane.showInputDialog("Nome da Playlist:");
            String idMusica = JOptionPane.showInputDialog("ID da Música:");
            if (nomePlaylist != null && idMusica != null) {
                dao.adicionarMusicaAPlaylist(nomePlaylist, Integer.parseInt(idMusica));
            }
        });

        buscarMusica.addActionListener(e -> {
            String[] options = {"Título", "Artista", "Álbum", "Gênero"};
            int choice = JOptionPane.showOptionDialog(frame, "Buscar por:", "Buscar Música",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice >= 0) {
                String parametro = options[choice].toLowerCase();
                String valor = JOptionPane.showInputDialog("Digite o " + options[choice] + ":");
                JOptionPane.showMessageDialog(frame, dao.buscarMusica(parametro, valor));
            }
        });

        deletarMusica.addActionListener(e -> {
            String nomePlaylist = JOptionPane.showInputDialog("Nome da Playlist:");
            String idMusica = JOptionPane.showInputDialog("ID da Música:");
            if (nomePlaylist != null && idMusica != null) {
                dao.deletarMusica(nomePlaylist, Integer.parseInt(idMusica));
            }
        });

        listarPlaylist.addActionListener(e -> {
            String nomePlaylist = JOptionPane.showInputDialog("Nome da Playlist:");
            JOptionPane.showMessageDialog(frame, dao.exibirPlaylist(nomePlaylist));
        });

        sair.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        new MusicFlowGUI();
    }
}
