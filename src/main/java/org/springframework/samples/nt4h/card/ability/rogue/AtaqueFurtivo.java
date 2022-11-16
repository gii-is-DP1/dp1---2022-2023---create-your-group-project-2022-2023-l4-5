package org.springframework.samples.nt4h.card.ability.rogue;

import org.springframework.samples.nt4h.card.ability.AbilityEffectEnum;
import org.springframework.samples.nt4h.card.enemy.EnemyInGame;
import org.springframework.samples.nt4h.player.Player;
import org.springframework.stereotype.Component;

@Component
public class AtaqueFurtivo {
    public void execute(Player player, EnemyInGame enemy) {
        new Attack(2, player, enemy).execute();

        if(!enemy.getPermanentEffectCardsUsed().contains(AbilityEffectEnum.ATAQUE_FURTIVO)){
            new GoldOnKill(1, enemy, player).execute();
        }

        player.getGame().getOrcs()
            .forEach(x -> x.getPermanentEffectCardsUsed().add(AbilityEffectEnum.ATAQUE_FURTIVO));
    }
}
