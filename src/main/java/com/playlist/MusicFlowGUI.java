package com.playlist;

import entity.Musica;
import entity.MusicaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class MusicFlowGUI {
    private JFrame frame;
    private MusicaService service;
    private MusicaDAO dao;

    public MusicFlowGUI() {
        dao = new MusicaDAO();
        service = new MusicaService();
        dao.criarTabelaMusica();

        frame = new JFrame("MusicFlow");
        frame.setSize(450, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        Color backgroundColor = new Color(18, 32, 47);  // Azul escuro
        Color buttonColor = new Color(52, 152, 219);    // Azul claro
        Color textColor = Color.WHITE;

        JPanel panel = new JPanel(new GridLayout(9, 1, 10, 10));
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] buttons = {
                "Exibir Músicas", "Adicionar Música", "Buscar Música", "Ordenar Músicas",
                "Adicionar PlayList", "Exibir Playlist", "Adicionar Musica a Playlist",
                "Deletar Música", "Sair"
        };

        for (String b : buttons) {
            JButton btn = new JButton(b) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);  // Arredondamento aqui!
                    super.paintComponent(g);
                    g2.dispose();
                }
            };
            btn.setBackground(buttonColor);
            btn.setForeground(textColor);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setBorder(new EmptyBorder(10, 20, 10, 20));  // Espaçamento interno
            btn.setContentAreaFilled(false);  // Para ativar o efeito customizado
            btn.setOpaque(false);
            btn.addActionListener(e -> executarAcao(b));
            panel.add(btn);
        }

        frame.add(panel);
        frame.getContentPane().setBackground(backgroundColor);
        frame.setVisible(true);
    }

    private void executarAcao(String acao) {
        switch (acao) {
            case "Exibir Músicas" -> exibirLista(dao.listarMusicas());
            case "Adicionar Música" -> adicionarMusica();
            case "Buscar Música" -> buscarMusica();
            case "Ordenar Músicas" -> ordenarMusicas();
            case "Adicionar PlayList" -> adicionarPlaylist();
            case "Exibir Playlist" -> exibirPlaylist();
            case "Adicionar Musica a Playlist" -> adicionarMusicaPlaylist();
            case "Deletar Música" -> deletarMusica();
            case "Sair" -> System.exit(0);
        }
    }

    private void exibirLista(List<Musica> musicas) {
        if (musicas == null || musicas.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nenhuma música encontrada.");
            return;
        }
        StringBuilder sb = new StringBuilder("ID | Título | Artista | Álbum | Gênero | Ano\n");
        sb.append("------------------------------------------------------------\n");
        for (Musica m : musicas) {
            sb.append(m.getId()).append(" | ").append(m.getTitulo()).append(" | ")
                    .append(m.getArtista()).append(" | ").append(m.getAlbum()).append(" | ")
                    .append(m.getGenero()).append(" | ").append(m.getAno()).append("\n");
        }
        JOptionPane.showMessageDialog(frame, sb.toString());
    }

    private void adicionarMusica() {
        JTextField[] fields = {new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField()};
        String[] labels = {"Título:", "Artista:", "Álbum:", "Gênero:", "Ano:"};
        Object[] inputs = new Object[labels.length * 2];
        for (int i = 0; i < labels.length; i++) {
            inputs[i * 2] = labels[i];
            inputs[i * 2 + 1] = fields[i];
        }

        int ok = JOptionPane.showConfirmDialog(frame, inputs, "Adicionar Música", JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION) {
            Musica m = new Musica();
            m.setTitulo(fields[0].getText());
            m.setArtista(fields[1].getText());
            m.setAlbum(fields[2].getText());
            m.setGenero(fields[3].getText());
            try {
                m.setAno(Integer.parseInt(fields[4].getText()));
                dao.adicionarMusica(m);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Ano inválido. Digite um número inteiro.");
            }
        }
    }

    private void buscarMusica() {
        String[] campos = {"titulo", "artista", "album", "genero"};
        String campo = (String) JOptionPane.showInputDialog(frame, "Buscar por:", "Campo",
                JOptionPane.PLAIN_MESSAGE, null, campos, campos[0]);
        String valor = JOptionPane.showInputDialog("Digite o valor:");
        if (campo != null && valor != null) {
            List<Musica> resultado = dao.buscarPorCampo(campo, valor);
            exibirLista(resultado);
        }
    }

    private void ordenarMusicas() {
        String[] campos = {"titulo", "artista", "ano"};
        String campo = (String) JOptionPane.showInputDialog(frame, "Ordenar por:", "Ordenação",
                JOptionPane.PLAIN_MESSAGE, null, campos, campos[0]);
        int ordem = JOptionPane.showConfirmDialog(frame, "Ordem crescente?", "Ordem",
                JOptionPane.YES_NO_OPTION);
        boolean asc = ordem == JOptionPane.YES_OPTION;

        List<Musica> lista = dao.listarMusicas();
        service.ordenar(lista, campo, asc);
        exibirLista(lista);
    }

    private void deletarMusica() {
        String idStr = JOptionPane.showInputDialog("Digite o ID da música a deletar:");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                dao.deletarMusica(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "ID inválido.");
            }
        }
    }

    private void adicionarPlaylist() {
        String nome = JOptionPane.showInputDialog("Digite o nome da playlist:");
        if (nome != null && !nome.trim().isEmpty()) {
            dao.criarPlaylist(nome.trim());
        }
    }

    private void exibirPlaylist() {
        String nome = JOptionPane.showInputDialog("Digite o nome da playlist:");
        if (nome != null && !nome.trim().isEmpty()) {
            String resultado = dao.exibirPlaylist(nome.trim());
            JOptionPane.showMessageDialog(frame, resultado);
        }
    }

    private void adicionarMusicaPlaylist() {
        String playlist = JOptionPane.showInputDialog("Digite o nome da playlist:");
        String idStr = JOptionPane.showInputDialog("Digite o ID da música:");
        if (playlist != null && idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                dao.adicionarMusicaAPlaylist(playlist.trim(), id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "ID inválido.");
            }
        }
    }

    public static void main(String[] args) {
        new MusicFlowGUI();
    }
}
