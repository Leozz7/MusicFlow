package com.playlist;

import entity.Musica;
import entity.MusicaService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MusicFlowGUI {
    private final MusicaDAO dao = new MusicaDAO();
    private final MusicaService service = new MusicaService();

    public MusicFlowGUI() {
        dao.inicializarBanco();
        criarInterface();
    }

    private void criarInterface() {
        JFrame frame = new JFrame("MusicFlow - TDE2");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(0, 1, 10, 10));

        String[] opcoes = {
                "Adicionar Música", "Exibir Músicas", "Buscar Música", "Ordenar Músicas", "Deletar Música", "Sair"
        };

        for (String opcao : opcoes) {
            JButton btn = new JButton(opcao);
            btn.addActionListener(e -> executarAcao(opcao));
            frame.add(btn);
        }

        frame.setVisible(true);
    }

    private void executarAcao(String acao) {
        switch (acao) {
            case "Adicionar Música" -> adicionarMusica();
            case "Exibir Músicas" -> mostrarLista(dao.buscarMusicas());
            case "Buscar Música" -> buscarMusica();
            case "Ordenar Músicas" -> ordenarMusicas();
            case "Deletar Música" -> deletarMusica();
            case "Sair" -> System.exit(0);
        }
    }

    private void adicionarMusica() {
        JTextField t = new JTextField(), a = new JTextField(), al = new JTextField(), g = new JTextField(), an = new JTextField();
        Object[] msg = {"Título:", t, "Artista:", a, "Álbum:", al, "Gênero:", g, "Ano:", an};

        if (JOptionPane.showConfirmDialog(null, msg, "Nova Música", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                Musica m = new Musica(t.getText(), a.getText(), al.getText(), g.getText(), Integer.parseInt(an.getText()));
                dao.adicionarMusica(m);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
            }
        }
    }

    private void mostrarLista(List<Musica> lista) {
        StringBuilder sb = new StringBuilder("ID   | Título          | Artista         | Álbum     | Gênero    | Ano\n");
        sb.append("--------------------------------------------------------------------------\n");
        for (Musica m : lista) {
            sb.append(m.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void buscarMusica() {
        String[] campos = {"titulo", "artista", "album", "genero"};
        String campo = (String) JOptionPane.showInputDialog(null, "Campo de busca:", "Buscar", JOptionPane.PLAIN_MESSAGE, null, campos, campos[0]);
        if (campo != null) {
            String valor = JOptionPane.showInputDialog("Digite o valor:");
            if (valor != null) {
                mostrarLista(dao.buscarPorCampo(campo, valor));
            }
        }
    }

    private void ordenarMusicas() {
        String[] campos = {"titulo", "artista", "album", "genero", "ano"};
        String campo = (String) JOptionPane.showInputDialog(null, "Ordenar por:", "Ordenação", JOptionPane.PLAIN_MESSAGE, null, campos, campos[0]);
        if (campo != null) {
            int opc = JOptionPane.showConfirmDialog(null, "Ordem crescente?", "Direção", JOptionPane.YES_NO_OPTION);
            boolean asc = opc == JOptionPane.YES_OPTION;
            List<Musica> musicas = dao.buscarMusicas();
            service.ordenar(musicas, campo, asc);
            mostrarLista(musicas);
        }
    }

    private void deletarMusica() {
        String idStr = JOptionPane.showInputDialog("ID da música a deletar:");
        try {
            int id = Integer.parseInt(idStr);
            dao.deletarMusica(id);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MusicFlowGUI::new);
    }
}
