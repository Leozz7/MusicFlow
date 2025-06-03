package com.playlist;

import entity.Musica;
import entity.MusicaService;

import javax.swing.*;
import java.awt.*;
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

        JPanel panel = new JPanel(new GridLayout(9, 1, 10, 10));
        panel.setBackground(new Color(20, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] buttons = {
                "Exibir Músicas", "Adicionar Música", "Buscar Música", "Ordenar Músicas",
                "Deletar Música", "Sair"
        };

        for (String b : buttons) {
            JButton btn = new JButton(b);
            btn.setBackground(new Color(50, 50, 50));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.addActionListener(e -> executarAcao(b));
            panel.add(btn);
        }

        frame.add(panel);
        frame.setVisible(true);
    }

    private void executarAcao(String acao) {
        switch (acao) {
            case "Exibir Músicas" -> exibirLista(dao.listarMusicas());
            case "Adicionar Música" -> adicionarMusica();
            case "Buscar Música" -> buscarMusica();
            case "Ordenar Músicas" -> ordenarMusicas();
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

    public static void main(String[] args) {
        new MusicFlowGUI();
    }
}
