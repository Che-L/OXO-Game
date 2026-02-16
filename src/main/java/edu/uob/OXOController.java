package edu.uob;

import java.io.Serial;
import java.io.Serializable;

public class OXOController implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;
    private OXOModel gameModel;

    public OXOController(OXOModel model) {
        gameModel = model;
    }

    public void handleIncomingCommand(String command) throws OXOMoveException {
        if (command == null) {
            throw new OXOMoveException.InvalidIdentifierLengthException(0);
        }

        command = command.toLowerCase().trim();
        if (command.length() != 2) {
            throw new OXOMoveException.InvalidIdentifierLengthException(command.length());
        }

        // 解析 row 字母
        char rowChar = command.charAt(0);
        if (rowChar < 'a' || rowChar > 'z') {
            throw new OXOMoveException.InvalidIdentifierCharacterException(
                    OXOMoveException.RowOrColumn.ROW, rowChar);
        }

        int row = rowChar - 'a';
        if (row < 0 || row >= gameModel.getNumberOfRows()) {
            throw new OXOMoveException.OutsideCellRangeException(
                    OXOMoveException.RowOrColumn.ROW, row);
        }

        // 解析 column 数字
        String colStr = command.substring(1);
        int col;

        for (char c : colStr.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new OXOMoveException.InvalidIdentifierCharacterException(
                        OXOMoveException.RowOrColumn.COLUMN, c);
            }
        }

        try {
            col = Integer.parseInt(colStr) - 1;
        } catch (NumberFormatException e) {
            throw new OXOMoveException.InvalidIdentifierCharacterException(
                    OXOMoveException.RowOrColumn.COLUMN, colStr.charAt(0));
        }

        if (col < 0 || col >= gameModel.getNumberOfColumns()) {
            throw new OXOMoveException.OutsideCellRangeException(
                    OXOMoveException.RowOrColumn.COLUMN, col);
        }

        // 检查格子是否被占用
        if (gameModel.getCellOwner(row, col) != null) {
            throw new OXOMoveException.CellAlreadyTakenException(row, col);
        }

        // 正确落子
        OXOPlayer current = gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber());
        gameModel.setCellOwner(row, col, current);

        // 检查游戏是否结束
        boolean ended = checkIfGameEnded(row, col);
        if (!ended) {
            int next = (gameModel.getCurrentPlayerNumber() + 1) % gameModel.getNumberOfPlayers();
            gameModel.setCurrentPlayerNumber(next);
        }
    }

    private boolean checkIfGameEnded(int row, int col) {
        OXOPlayer player = gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber());
        int needed = gameModel.getWinThreshold();

        if (checkLine(player, row, col, needed)) {
            gameModel.setWinner(player);
            return true;  // 游戏结束
        }

        // 平局：全盘占满 且 没有 winner
        if (isBoardFull() && gameModel.getWinner() == null) {
            gameModel.setGameDrawn(true);
        }

        return false; // 游戏继续
    }

    private boolean checkIfGameStarted() {
        // 遍历棋盘
        for (int r = 0; r < gameModel.getNumberOfRows(); r++) {
            for (int c = 0; c < gameModel.getNumberOfColumns(); c++) {
                if (gameModel.getCellOwner(r, c) != null) {
                    return true; // 棋盘已有落子，游戏已经开始
                }
            }
        }
        return false; // 棋盘全空，游戏尚未开始
    }

    private boolean checkLine(OXOPlayer player, int row, int col, int needed) {
        int rows = gameModel.getNumberOfRows();
        int cols = gameModel.getNumberOfColumns();

        // --------- 横向检查 ---------
        int count = 0;
        for (int c = 0; c < cols; c++) {
            if (gameModel.getCellOwner(row, c) == player) count++;
            else count = 0;

            if (count >= needed) return true;
        }

        // --------- 纵向检查 ---------
        count = 0;
        for (int r = 0; r < rows; r++) {
            if (gameModel.getCellOwner(r, col) == player) count++;
            else count = 0;

            if (count >= needed) return true;
        }

        // --------- 主对角线检查 (\) ---------
        count = 0;
        int r = row, c = col;
        // 回溯到对角线起点
        while (r > 0 && c > 0) { r--; c--; }
        while (r < rows && c < cols) {
            if (gameModel.getCellOwner(r, c) == player) count++;
            else count = 0;
            if (count >= needed) return true;
            r++; c++;
        }

        // --------- 副对角线检查 (/) ---------
        count = 0;
        r = row; c = col;
        // 回溯到对角线起点
        while (r > 0 && c < cols - 1) { r--; c++; }
        while (r < rows && c >= 0) {
            if (gameModel.getCellOwner(r, c) == player) count++;
            else count = 0;
            if (count >= needed) return true;
            r++; c--;
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int r = 0; r < gameModel.getNumberOfRows(); r++) {
            for (int c = 0; c < gameModel.getNumberOfColumns(); c++) {
                if (gameModel.getCellOwner(r, c) == null) return false;
            }
        }
        return true;
    }

    public void increaseWinThreshold() {
        if (gameModel.getWinner() != null) return;
        int currentWin = gameModel.getWinThreshold();
        int max = Math.min(gameModel.getNumberOfRows(), gameModel.getNumberOfColumns());
        if (currentWin < max) {
            gameModel.setWinThreshold(currentWin + 1);
        }
    }
    public void decreaseWinThreshold() {
        if (gameModel.getWinner() != null) return; // 游戏已结束
        if (checkIfGameStarted()) return;              // 游戏已开始不能减
        int current = gameModel.getWinThreshold();
        if (current > 3) {
            gameModel.setWinThreshold(current - 1);
        }
    }

    public void addRow() { gameModel.addRow(); }
    public void removeRow() { gameModel.removeRow(); }
    public void addColumn() { gameModel.addColumn(); }
    public void removeColumn() { gameModel.removeColumn(); }
    public void reset() { gameModel.reset(); }
}