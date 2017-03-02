package com.asuka.game.net.server;

/**
 * Created by Asuka on 13.08.2016.
 */
public class Game {
    char[] map;

    public Game(){
        map = new char[9];
    }
    public boolean isFilled(int index){
        return map[index] != ' ';
    }
    public boolean isEmpty(int index){
        return map[index] == ' ';
    }
    public boolean fill(char marker, int index){
        if (isFilled(index)){
            return false;
        }
        map[index] = marker;
        return true;
    }
    public void clear(){
        for (int i = 0; i < 9; i++){
            map[i] = ' ';
        }
    }
    public String toString(){
        String result = "";
        for (int i = 0; i < 9; i++) result = result + map[i];
        return result;
    }
    public char checkWinner(){
        // D  - Draw
        // X  - Player 1
        // O  - Player 2

        if ((map[0] == map[1]) && (map[1] == map[2])) if(map[1] != ' ') return map[1];
        if ((map[3] == map[4]) && (map[4] == map[5])) if(map[4] != ' ') return map[4];
        if ((map[6] == map[7]) && (map[7] == map[8])) if(map[7] != ' ') return map[7];

        if ((map[0] == map[3]) && (map[3] == map[6])) if(map[3] != ' ') return map[3];
        if ((map[1] == map[4]) && (map[4] == map[7])) if(map[4] != ' ') return map[4];
        if ((map[2] == map[5]) && (map[5] == map[8])) if(map[5] != ' ') return map[5];

        if ((map[0] == map[4]) && (map[4] == map[8])) if(map[4] != ' ') return map[4];
        if ((map[2] == map[4]) && (map[4] == map[6])) if(map[4] != ' ') return map[4];

        int filled = 0;
        for (int i = 0; i < 9; i++)
            if (isFilled(i)) filled++;

        if (filled == 9) return 'D';
        return ' ';
    }
}
