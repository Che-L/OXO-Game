package edu.uob;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OXOModel implements Serializable {
    @Serial private static final long serialVersionUID = 1;
    private List<List<OXOPlayer>> cells;
    private OXOPlayer[] players;
    private int currentPlayerNumber;
    private OXOPlayer winner;
    private boolean gameDrawn;
    private int winThreshold;

    public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {
        winThreshold = winThresh;
        cells = new ArrayList<>();
        players = new OXOPlayer[2];

        for (int i = 0; i < numberOfRows; i++) {
            List<OXOPlayer> row = new ArrayList<>();
            for (int j = 0; j < numberOfColumns; j++) {
                row.add(null); // 空格子
            }
            cells.add(row);
        }
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
        return cells.size();
    }

    public void addRow() {
        if (cells.size() < 9) {
            int cols = getNumberOfColumns();
            List<OXOPlayer> newRow = new ArrayList<>();
            for (int i = 0; i < cols; i++) {
                newRow.add(null);
            }
            cells.add(newRow);
        }
    }

    public void removeRow() {
        if (cells.size() > 3) {
            List<OXOPlayer> lastRow = cells.get(cells.size() - 1);
            for (OXOPlayer p : lastRow) {
                if (p != null) return;
            }
            cells.remove(cells.size() - 1);
        }
    }

    public int getNumberOfColumns() {
        return cells.get(0).size();
    }

    public void addColumn() {
        if (cells.get(0).size() < 9) {
            for (int i = 0; i < cells.size(); i++) {
                cells.get(i).add(null);
            }
        }
    }

    public void removeColumn() {
        if (cells.get(0).size() > 3) {
            int lastCol = cells.get(0).size() - 1;

            for (List<OXOPlayer> row : cells) {
                if (row.get(lastCol) != null) return;
            }

            for (List<OXOPlayer> row : cells) {
                row.remove(lastCol);
            }
        }
    }

    public OXOPlayer getCellOwner(int rowNumber, int colNumber) {
        return cells.get(rowNumber).get(colNumber);
    }

    public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player) {
        cells.get(rowNumber).set(colNumber, player);
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

    public void increaseWinThreshold() {
        int curWinThresh = this.winThreshold;
        curWinThresh += 1;
        this.winThreshold = curWinThresh;
    }

    public void decreaseWinThreshold() {
        int curWinThresh = this.winThreshold;
        curWinThresh -= 1;
        this.winThreshold = curWinThresh;
    }

    public boolean isGameDrawn() {
        return gameDrawn;
    }

    public  void reset() {
        setWinner(null);
        setGameDrawn(false);

        for (int i = 0; i < cells.size(); i++) {
            List<OXOPlayer> row = cells.get(i);
            for (int j = 0; j < row.size(); j++) {
                row.set(j, null); // 或者 row.set(j, null)
            }
        }
    }
}
