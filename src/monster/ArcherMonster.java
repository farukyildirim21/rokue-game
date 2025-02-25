package monster;

import java.io.Serializable;

import player.Player;
import game.GameManager;


public class ArcherMonster extends Monster implements Serializable {
	private static final long serialVersionUID = 1L;
    public ArcherMonster(int row, int col, GameManager gameManager) {
        super(row, col, gameManager);
    }

    @Override
    protected void performAction(Player player) {
        int distance = Math.abs(player.getRow() - row) + Math.abs(player.getCol() - col);
        if (distance < 4 && !player.getWearingCloak()) {
            player.loseLife();
        }
    }
}

