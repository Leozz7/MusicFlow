package com.playlist;

import entity.Musica;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int opcao, opcao2;

        Scanner scanner = new Scanner(System.in);
        MusicaDAO dao = new MusicaDAO();
        Musica m = new Musica();

        dao.criarDatabase();

        System.out.println("-----MusicFlow------");

        do {
            System.out.println("[1] Exibir Todas Músicas");
            System.out.println("[2] Adicionar Música");
            System.out.println("[3] Criar Playlist");
            System.out.println("[4] Exibir Playlists");
            System.out.println("[5] Adicionar Musica a Playlist");
            System.out.println("[6] Buscar Música");
            System.out.println("[7] Deletar Músicas");
            System.out.println("[8] Listar Playlist");
            System.out.println("[9] SAIR");
            opcao = scanner.nextInt();

            scanner.nextLine();

            if (opcao == 1) {
                System.out.println(dao.exibirMusicas());
            } else if (opcao == 2) {
                System.out.println("Titulo: ");
                m.setTitulo(scanner.nextLine());
                System.out.println("Artista: ");
                m.setArtista(scanner.nextLine());
                System.out.println("Album: ");
                m.setAlbum(scanner.nextLine());
                System.out.println("Gênero: ");
                m.setGenero(scanner.nextLine());
                System.out.println("Ano: ");
                m.setAno(scanner.nextInt());
                dao.adicionarMusica(m.getTitulo(), m.getArtista(), m.getAlbum(), m.getGenero(), m.getAno());
            } else if (opcao == 3) {
                System.out.println("NOME DA PLAYLIST: ");
                m.setNomePlaylist(scanner.nextLine());
                dao.criarPlaylist(m.getNomePlaylist());
            } else if (opcao == 4) {
                dao.listarPlaylists();
            } else if (opcao == 5) {
                System.out.println("NOME DA PLAYLIST: ");
                m.setNomePlaylist(scanner.nextLine());
                System.out.println("ID DA MÚSICA: ");
                m.setId(scanner.nextInt());
                dao.adicionarMusicaAPlaylist(m.getNomePlaylist(), m.getId());
            } else if (opcao == 6) {
                System.out.println("BUSCAR POR: ");
                System.out.println("[1] TÍTULO");
                System.out.println("[2] ARTISTA");
                System.out.println("[3] ALBUM");
                System.out.println("[4] GENERO");
                opcao2 = scanner.nextInt();

                scanner.nextLine();

                if (opcao2 == 1) {
                    m.setParametro("titulo");
                    System.out.println("TITULO: ");
                } else if (opcao2 == 2) {
                    m.setParametro("artista");
                    System.out.println("ARTISTA: ");
                } else if (opcao2 == 3) {
                    m.setParametro("album");
                    System.out.println("ALBUM: ");
                } else if (opcao2 == 4) {
                    m.setParametro("genero");
                    System.out.println("GENERO: ");
                }

                m.setValor(scanner.nextLine());
                System.out.println(dao.buscarMusica(m.getParametro(), m.getValor()));
            } else if (opcao == 7) {
                System.out.println("NOME DA PLAYLIST: ");
                m.setNomePlaylist(scanner.nextLine());
                System.out.println("ID DA MÚSICA: ");
                m.setId(scanner.nextInt());
                dao.deletarMusica(m.getNomePlaylist(), m.getId());
            } else if (opcao == 8) {
                System.out.println("NOME DA PLAYLIST: ");
                m.setNomePlaylist(scanner.nextLine());
                System.out.println(dao.exibirPlaylist(m.getNomePlaylist()));
            }
        } while (opcao != 9);
    }
}
