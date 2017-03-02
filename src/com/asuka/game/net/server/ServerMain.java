package com.asuka.game.net.server;

/**
 * Created by Asuka on 10.08.2016.
 */

import  java.net.*;

public class ServerMain {
    public boolean isQuit;
    public ServerSocket socket;
    private Player playerFinder = null;

    public void findLobby(Player player){
        System.out.println("Player \"" + player.getName() + "\" is finding an opponent.");
        if (playerFinder == null) {
            playerFinder = player;
        } else {
            new Lobby(playerFinder,player,false);
            playerFinder = null;
        }
    }
    public void findAI(Player player){
        System.out.println("Player \"" + player.getName() + "\" is finding an AI.");
        new Lobby(player,null,true);
    }
    public void closeFinder(Player player){
        if (player == playerFinder) {
            playerFinder = null;
            System.out.println("Player \"" + player.getName() + "\" is not finding an opponent.");
        }
    }
    public boolean checkFinder(Player player){
        return player == playerFinder;
    }

    public static void main(String[] args) {
        new ServerMain();
    }

    public ServerMain() {
        int i = 0;

        try {
            socket = new ServerSocket(8954, 0, InetAddress.getByName("localhost"));
            System.out.println("Server is started");

            isQuit = false;
            System.out.println("Waiting for a client...");

            while (!isQuit) {
                new ServerThread(i, socket.accept(), this);
                i++;
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
