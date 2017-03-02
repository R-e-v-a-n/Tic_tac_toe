package com.asuka.game.net.server;

import java.util.Random;
import java.util.Vector;

/**
 * Created by Asuka on 14.08.2016.
 */
public class AI {
    private Game game;
    char marker;
    int lastPosition = -1;

    public AI(Game game){
        this.game = game;
    }

    public void setMarker(char marker) {
        this.marker = marker;
    }
    private int checkLines(char mark){
        int markCountInLine = 0;
        boolean hasOtherMark = false;
        int emptyPosition = -1;

        // Horisontal
        for (int j = 0; j < 3; j++){
            for(int i = 0; i < 3; i++)
            {
                if(game.isEmpty(j*3+i)) emptyPosition = j*3+i;
                else if (game.map[j*3+i] == mark) markCountInLine++;
                else {
                    hasOtherMark = true;
                    break;
                }
            }
            if((!hasOtherMark) &&(markCountInLine == 2)) return emptyPosition;
            markCountInLine = 0;
            hasOtherMark = false;
            emptyPosition = -1;
        }

        // Vertical
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (game.isEmpty(j * 3 + i)) emptyPosition = j * 3 + i;
                else if (game.map[j * 3 + i] == mark) markCountInLine++;
                else {
                    hasOtherMark = true;
                    break;
                }
            }
            if ((!hasOtherMark) && (markCountInLine == 2)) return emptyPosition;
            markCountInLine = 0;
            hasOtherMark = false;
            emptyPosition = -1;
        }

        //Diagonal LR
        for(int i = 0; i < 3; i++)
        {
            if(game.isEmpty(i*4)) emptyPosition = i*4;
            else if (game.map[i*4] == mark) markCountInLine++;
            else {
                hasOtherMark = true;
                break;
            }
        }
        if((!hasOtherMark) &&(markCountInLine == 2)) return emptyPosition;

        markCountInLine = 0;
        hasOtherMark = false;
        emptyPosition = -1;

        //Diagonal RL
        for(int i = 0; i < 3; i++)
        {
            if(game.isEmpty(i*2+2)) emptyPosition = i*2+2;
            else if (game.map[i*2+2] == mark) markCountInLine++;
            else {
                hasOtherMark = true;
                break;
            }
        }
        if((!hasOtherMark) &&(markCountInLine == 2)) return emptyPosition;

        return -1;
    }
    private int getBestPosition(){
        Random random = new Random();
        if (!game.isFilled(4)) return 4;

        int bestPosition[] = {0,2,6,8};
        Vector<Integer> emptyPositions = new Vector<>();
        Vector<Integer> markedPositions = new Vector<>();
        for (int i = 0; i < 4; i++) {
            if (game.isEmpty(bestPosition[i])) emptyPositions.add(bestPosition[i]);
            else markedPositions.add(bestPosition[i]);
        }
        if (emptyPositions.size() == 4) {
            if (marker == 'O'){
                if (game.isFilled(1) && game.isFilled(3)) emptyPositions.remove(3);
                if (game.isFilled(1) && game.isFilled(5)) emptyPositions.remove(2);
                if (game.isFilled(7) && game.isFilled(3)) emptyPositions.remove(1);
                if (game.isFilled(7) && game.isFilled(5)) emptyPositions.remove(0);
            }
            return emptyPositions.elementAt(random.nextInt(emptyPositions.size()));
        }
        if (emptyPositions.size() == 3){
            switch (markedPositions.elementAt(0)){
                case 0: return 8;
                case 2: return 6;
                case 6: return 2;
                case 8: return 0;
            }
        }
        if (emptyPositions.size() == 2){
            // Найти линию имеющую 1 свой знак и 0 знаков соперника
            if(marker == 'X') {
                switch (lastPosition) {
                    case 0:
                        if (game.isEmpty(1)) return 1;
                        else return 3;
                    case 2:
                        if (game.isEmpty(1)) return 1;
                        else return 5;
                    case 6:
                        if (game.isEmpty(7)) return 7;
                        else return 3;
                    case 8:
                        if (game.isEmpty(7)) return 7;
                        else return 5;
                }
            }
            else{
                return emptyPositions.elementAt(random.nextInt(2));
            }
        }

        Vector<Integer> fields = new Vector<>();
        fields.clear();
        for (int i = 0; i < 9; i++)
            if (!game.isFilled(i))
                fields.add(i);

        return fields.elementAt(random.nextInt(fields.size()));
    }
    public int getNextPosition(){
        int result = checkLines(marker);
        if (result != -1) {
            lastPosition = result;
            return result;
        }

        result = checkLines((marker == 'X') ? 'O' : 'X');
        if (result != -1) {
            lastPosition = result;
            return result;
        }

        lastPosition = getBestPosition();
        return lastPosition;
    }
}
