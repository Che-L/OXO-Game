package edu.uob;

import java.io.Serial;
import java.io.Serializable;

public class OXOController implements Serializable {
    @Serial private static final long serialVersionUID = 1;
    private OXOModel gameModel;

    public OXOController(OXOModel model) {
        gameModel = model;
    }

    public void handleIncomingCommand(String command) throws OXOMoveException {}
    public void addRow() {
        gameModel.addRow();
    }

    public void removeRow() {
        gameModel.removeRow();
    }

    public void addColumn() {
        gameModel.addColumn();
    }

    public void removeColumn() {
        gameModel.removeColumn();
    }

    public void increaseWinThreshold() {
        int rows = gameModel.getNumberOfRows();
        int curCols = gameModel.getNumberOfColumns();
        int curWinThresh = gameModel.getWinThreshold();

        curWinThresh += 1;
        this.gameModel = new OXOModel(rows, curCols, curWinThresh);
    }
    public void decreaseWinThreshold() {
        int rows = gameModel.getNumberOfRows();
        int curCols = gameModel.getNumberOfColumns();
        int curWinThresh = gameModel.getWinThreshold();

        curWinThresh -= 1;
        this.gameModel = new OXOModel(rows, curCols, curWinThresh);
    }

    public void reset() {
        gameModel.reset();
    }
}
