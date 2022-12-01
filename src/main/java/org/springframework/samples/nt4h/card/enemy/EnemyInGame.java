package org.springframework.samples.nt4h.card.enemy;

import lombok.Getter;
import lombok.Setter;
import org.springframework.samples.nt4h.card.ability.AbilityEffectEnum;
import org.springframework.samples.nt4h.model.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
public class EnemyInGame extends BaseEntity {
    @NotNull
    @Max(value = 10)
    private Integer actualHealth;

    // TODO: Decidir como pasarle las habilidades.
    // @Enumerated(EnumType.STRING)
    // private Set<AbilityEffectEnum> permanentEffectCardsUsed = new HashSet<>();

    @NotNull
    private boolean isNightLord;
    private boolean isDead;
    private Boolean noAttackThisTurn;

    @ManyToOne
    private Enemy enemy;

}
