package edu.uob;

import java.io.Serial;
import java.io.Serializable;

public class OXOModel implements Serializable {
    @Serial private static final long serialVersionUID = 1;
    private OXOPlayer[][] cells;
    private OXOPlayer[] players;
    private int currentPlayerNumber;
    private OXOPlayer winner;
    private boolean gameDrawn;
    private int winThreshold;

    public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {
        winThreshold = winThresh;
        cells = new OXOPlayer[numberOfRows][numberOfColumns];
        players = new OXOPlayer[2];
    }

    public int getNumberOfPlayers() {
        return players.length;
    }

    public void addPlayer(OXOPlayer player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = player;
                return;
            }
        }
    }

    public OXOPlayer getPlayerByNumber(int number) {
        return players[number];
    }

    public OXOPlayer getWinner() {
        return winner;
    }

    public void setWinner(OXOPlayer player) {
        winner = player;
    }

    public int getCurrentPlayerNumber() {
        return currentPlayerNumber;
    }

    public void setCurrentPlayerNumber(int playerNumber) {
        currentPlayerNumber = playerNumber;
    }

    public int getNumberOfRows() {
        return cells.length;
    }

    public void addRow() {
        if (cells.length <= 9) {
            int curRows = cells.length;
            int cols = cells[0].length;
            curRows += 1;
            this.cells = new OXOPlayer[curRows][cols];
        }
    }

    public void removeRow() {
        if (cells.length > 3) {
            int curRows = cells.length;
            int cols = cells[0].length;
            curRows -= 1;
            this.cells = new OXOPlayer[curRows][cols];
        }
    }

    public int getNumberOfColumns() {
        return cells[0].length;
    }

    public void addColumn() {
        if (cells[0].length <= 9) {
            int rows = cells.length;
            int curCols = cells[0].length;
            curCols += 1;
            this.cells = new OXOPlayer[rows][curCols];
        }
    }

    public void removeColumn() {
        if (cells[0].length > 3) {
            int rows = cells.length;
            int curCols = cells[0].length;
            curCols -= 1;
            this.cells = new OXOPlayer[rows][curCols];
        }
    }



    public OXOPlayer getCellOwner(int rowNumber, int colNumber) {
        return cells[rowNumber][colNumber];
    }

    public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player) {
        cells[rowNumber][colNumber] = player;
    }

    public void setWinThreshold(int winThresh) {
        winThreshold = winThresh;
    }

    public int getWinThreshold() {
        return winThreshold;
    }

    public void setGameDrawn(boolean isDrawn) {
        gameDrawn = isDrawn;
    }

    public boolean isGameDrawn() {
        return gameDrawn;
    }

    public  void reset() {
        int rows = cells.length;
        int cols = cells[0].length;

        // 清空棋盘，把每个格子设为 null
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = null;
            }
        }
    }
}
