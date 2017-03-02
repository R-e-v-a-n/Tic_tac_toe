package com.asuka.game.net.server;

/**
 * Created by Asuka on 10.08.2016.
 */
import com.asuka.game.net.NetReader;
import com.asuka.game.net.netIO;

import java.net.*;

public class ServerThread extends NetReader{
    netIO io;
    int num;
    ServerMain server;
    Player player;

    @Override
    public void send(String command, String text) {
        io.send(command,text);
    }

    @Override
    public void read(String command, String text) {
        try{
            switch (command){
                case "EXIT":{ // player wants to disconnect
                    io.setQuit(true);
                    return;
                }
                case "STOP":{ // stop server
                    io.setQuit(true);
                    server.isQuit = true;
                    server.socket.close();
                    System.out.println("Stopping the server");
                    return;
                }
                case "FIND":{ // find lobby
                    if (player.lobby == null){
                        if (!server.checkFinder(player)) {
                            io.send("TEXT","Now you're finding an opponent.");
                            server.findLobby(player);
                        }
                        else {
                            io.send("TEXT","You're already finding an opponent.");
                        }
                    }else{
                        io.send("TEXT","Error finding lobby. Lobby is already exists.");
                    }
                }
                break;
                case "F_AI":{
                    if (player.lobby == null){
                        server.findAI(player);
                    }else{
                        io.send("TEXT","Error finding lobby. Lobby is already exists.");
                    }
                }
                break;
                case "CLCK":{ // click
                    try{
                        player.lobby.click(player,Integer.parseInt(text));
                    }
                    catch (Exception e){

                    }
                }
                break;
                case "NAME":{ // set name of player
                    player.setName(text);
                    System.out.println("The player #" + num + " was named: " + text);
                    io.send("TEXT","The player #" + num + " was named: " + text);
                }
                break;
                case "TEXT":{
                    if (player.lobby != null){
                        player.lobby.resendMessage(player,player.getName() + " : " + text);
                    }else{
                        io.send(command, "The message have been returned. Lobby is unavailable.");
                    }
                }
                break;
                default:{ // plain text
                    System.out.println("I'm sending it back...");
                    io.send(command, text);
                }
                break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if (player.lobby != null) {
            player.lobby.exitLobby(player);
        }
        server.closeFinder(player);
        System.out.println("Exiting thread #" + num);
    }

    public ServerThread(int n, Socket s, ServerMain srv) {
        this.num      = n;
        this.server   = srv;

        io = new netIO(this,s);
        player = new Player(this);

        System.out.println("Got a client :) ... Finally, someone saw me through all the cover!");
        System.out.println("Starting new thread: " + num);

    }
}
