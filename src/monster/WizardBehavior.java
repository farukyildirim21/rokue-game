package monster;

import player.Player;
import game.GameManager;
// we use strategy pattern
public interface WizardBehavior {
    void executeBehavior(WizardMonster monster, GameManager gameManager);
}
