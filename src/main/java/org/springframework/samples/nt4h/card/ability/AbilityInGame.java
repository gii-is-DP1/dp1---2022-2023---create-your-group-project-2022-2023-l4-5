package org.springframework.samples.nt4h.card.ability;

import lombok.*;
import org.springframework.samples.nt4h.card.product.ProductInGame;
import org.springframework.samples.nt4h.model.BaseEntity;
import org.springframework.samples.nt4h.player.Player;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "abilities_in_game")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AbilityInGame extends BaseEntity {
    @Min(0)
    @Column(columnDefinition = "int default 0")
    private Integer timesUsed;

    @Min(0)
    // @NotNull
    private Integer attack;

    @NotNull
    private boolean isProduct;

    @ManyToOne
    private Ability ability;

    @ManyToOne
    private Player player;

    @ManyToOne
    private ProductInGame productInGame;

    @Enumerated(EnumType.STRING)
    private AbilityCardType abilityCardType;
}
