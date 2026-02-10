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

        if (command.length() < 2) {
            throw new OXOMoveException.InvalidIdentifierLengthException(command.length());
        }

        // -------------------------
        // 解析 row 字母
        // -------------------------
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

        // -------------------------
        // 解析 column 数字
        // -------------------------
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

        // -------------------------
        // 检查格子是否被占用
        // -------------------------
        if (gameModel.getCellOwner(row, col) != null) {
            throw new OXOMoveException.CellAlreadyTakenException(row, col);
        }

        // -------------------------
        // 正确落子
        // -------------------------
        OXOPlayer current = gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber());
        gameModel.setCellOwner(row, col, current);

        // -------------------------
        // 检查游戏是否结束
        // （我可以帮你写完整 win check）
        // -------------------------
        checkIfGameEnded(row, col);

        // -------------------------
        // 切换玩家
        // -------------------------
        int next = (gameModel.getCurrentPlayerNumber() + 1) % gameModel.getNumberOfPlayers();
        gameModel.setCurrentPlayerNumber(next);
    }

    private void checkIfGameEnded(int row, int col) {
        OXOPlayer player = gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber());
        int needed = gameModel.getWinThreshold();

        if (checkLine(player, row, 0, 0, 1, needed)) {  // 横向
            gameModel.setWinner(player);
            return;
        }
        if (checkLine(player, 0, col, 1, 0, needed)) {  // 纵向
            gameModel.setWinner(player);
            return;
        }
        if (checkDiag1(player, row, col, needed)) {      // 主对角线
            gameModel.setWinner(player);
            return;
        }
        if (checkDiag2(player, row, col, needed)) {      // 副对角线
            gameModel.setWinner(player);
            return;
        }

        // 平局：全盘占满 且 没有 winner
        if (isBoardFull() && gameModel.getWinner() == null) {
            gameModel.setGameDrawn(true);
        }
    }

    private boolean checkLine(OXOPlayer p, int startRow, int startCol, int dRow, int dCol, int needed) {
        int count = 0;

        int rows = gameModel.getNumberOfRows();
        int cols = gameModel.getNumberOfColumns();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                int r = startRow + i * dRow;
                int c = startCol + j * dCol;

                if (r >= rows || c >= cols) continue;

                if (gameModel.getCellOwner(r, c) == p) {
                    count++;
                    if (count >= needed) return true;
                } else count = 0;
            }
        }
        return false;
    }

    private boolean checkDiag1(OXOPlayer p, int row, int col, int needed) {
        int rows = gameModel.getNumberOfRows();
        int cols = gameModel.getNumberOfColumns();

        int r = row;
        int c = col;

        while (r > 0 && c > 0) {
            r--;
            c--;
        }

        int count = 0;

        while (r < rows && c < cols) {
            if (gameModel.getCellOwner(r, c) == p) {
                count++;
                if (count >= needed) return true;
            } else {
                count = 0;
            }
            r++;
            c++;
        }
        return false;
    }

    private boolean checkDiag2(OXOPlayer p, int row, int col, int needed) {
        int rows = gameModel.getNumberOfRows();
        int cols = gameModel.getNumberOfColumns();

        int r = row;
        int c = col;

        while (r > 0 && c < cols - 1) {
            r--;
            c++;
        }

        int count = 0;

        while (r < rows && c >= 0) {
            if (gameModel.getCellOwner(r, c) == p) {
                count++;
                if (count >= needed) return true;
            } else {
                count = 0;
            }
            r++;
            c--;
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

    public void addRow() { gameModel.addRow(); }
    public void removeRow() { gameModel.removeRow(); }
    public void addColumn() { gameModel.addColumn(); }
    public void removeColumn() { gameModel.removeColumn(); }
    public void increaseWinThreshold() { gameModel.increaseWinThreshold(); }
    public void decreaseWinThreshold() { gameModel.decreaseWinThreshold(); }
    public void reset() { gameModel.reset(); }
}