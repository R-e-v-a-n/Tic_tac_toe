package com.asuka.game.net.server;

import java.util.Random;

/**
 * Created by Asuka on 13.08.2016.
 */

public class Lobby {
    String name;
    Game game;
    private Player player_1;
    private Player player_2;
    private Player currentPlayer;
    private Random random;
    private boolean withAI;
    private AI ai;

    public void startGame(){
        game.clear();

        player_1.marker = (random.nextBoolean()) ? 'X' : 'O';
        player_1.sendMessage("Your marker is: " + player_1.marker);
        player_1.sendCommand("DATA",game.toString());

        if (withAI) {
            if (player_1.marker == 'X') {
                ai.setMarker('O');
                player_1.sendMessage("You are first.");
            }
            else {
                ai.setMarker('X');
                clickAI();
            }
        }
        else {
            player_2.marker = (player_1.marker == 'X') ? 'O' : 'X';
            currentPlayer = player_1.marker == 'X' ? player_1 : player_2;
            player_2.sendMessage("Your marker is: " + player_2.marker);
            currentPlayer.sendMessage("You are first.");
            player_2.sendCommand("DATA",game.toString());
        }
    }

    public Lobby(Player p1, Player p2, boolean withAI){
        this.withAI = withAI;
        game = new Game();
        ai = new AI(game);

        random = new Random();

        player_1 = p1;
        player_1.acceptLobby(this);

        if(withAI){
            player_1.sendMessage("Your opponent is: AI");
            name = player_1.getName() + " vs AI";

            player_1.sendCommand("J_AI","");
        }
        else{
            player_2 = p2;
            player_2.acceptLobby(this);
            player_1.sendMessage("Your opponent is: " + player_2.getName());
            player_2.sendMessage("Your opponent is: " + player_1.getName());
            name = player_1.getName() + " vs " + player_2.getName();
            player_1.sendCommand("JOIN","");
            player_2.sendCommand("JOIN","");
        }

        System.out.println("Lobby was created. Lobby name is: " + name);

        startGame();
    }

    public void resendMessage(Player player, String text){
        if (withAI) return;
        if (player == player_1) {
            player_2.sendMessage(text);
        }
        if (player == player_2){
            player_1.sendMessage(text);
        }
    }

    public void exitLobby(Player player){
        if (player == null) return;

        if (!withAI) {
            resendMessage(player,"Player \"" + player.getName() + "\" left from lobby \"" + name + "\"");
            if (player == player_1) player_2.sendCommand("CLBB", "");
            if (player == player_2) player_1.sendCommand("CLBB", "");
            player_2.acceptLobby(null);
            player_2 = null;
        }
        player_1.acceptLobby(null);
        player_1 = null;

        System.out.println("The lobby \"" + name + "\" was closed.");
    }
    public void switchPlayers(){
        currentPlayer = currentPlayer == player_1 ? player_2 : player_1;
    }

    public boolean checkWinner(){
        char winner = game.checkWinner();
        if (winner != ' '){
            String text;
            if(withAI){
                if (winner == 'D') text = "Draw";
                else {
                    text = (player_1.marker == winner) ? "Player \"" + player_1.getName() + "\" won." : "AI won.";
                }
                player_1.sendMessage(text);
            }
            else{
                if (winner == 'D') text = "Draw";
                else {
                    text = (player_1.marker == winner) ? player_1.getName() : player_2.getName();
                    text = "Player \"" + text + "\" won.";
                }
                player_1.sendMessage(text);
                player_2.sendMessage(text);
            }

            startGame();
        }
        return winner != ' ';
    }

    private void clickAI(){
        game.fill(ai.marker, ai.getNextPosition());
        player_1.sendCommand("DATA",game.toString());
        checkWinner();
    }

    public void click(Player player, int index){
        if(!withAI){
            if (player != currentPlayer) return;
            if (game.fill(currentPlayer.marker, index)) switchPlayers();
            player_1.sendCommand("DATA",game.toString());
            player_2.sendCommand("DATA",game.toString());
            checkWinner();
        }
        else{
            if (!game.fill(player_1.marker, index)) return;
            player_1.sendCommand("DATA",game.toString());
            if (!checkWinner()) clickAI();
        }
    }
}
