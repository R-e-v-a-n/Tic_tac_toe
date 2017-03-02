package com.asuka.game.net.server;

import com.asuka.game.net.NetReader;
import com.asuka.game.net.server.Lobby;

/**
 * Created by Asuka on 13.08.2016.
 */
public class Player {
    private String name = "Empty";
    private NetReader reader;

    public  char marker = 'X';
    public Lobby lobby = null;

    public Player(NetReader netReader){
        reader = netReader;
    }
    public void sendMessage(String text){
        reader.send("TEXT",text);
    }
    public void sendCommand(String command, String text){
        reader.send(command, text);
    }
    public void acceptLobby(Lobby lobby){
        this.lobby = lobby;
        System.out.println("Player \"" + name + "\" accept the lobby");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
