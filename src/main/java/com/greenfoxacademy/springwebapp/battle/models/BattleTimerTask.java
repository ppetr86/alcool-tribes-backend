package com.greenfoxacademy.springwebapp.battle.models;

import com.greenfoxacademy.springwebapp.battle.services.BattleServiceImpl;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import java.util.List;
import java.util.TimerTask;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BattleTimerTask extends TimerTask {

    private KingdomEntity attackingKingdom;
    private List<TroopEntity> attackingTroops;
    private KingdomEntity defendingKingdom;
    private int distance;
    private BattleServiceImpl battleService;

    @Override
    public void run() {
        battleService.runBattle(this.attackingKingdom, this.attackingTroops, this.defendingKingdom, this.distance);
    }
}
