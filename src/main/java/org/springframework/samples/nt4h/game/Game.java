package org.springframework.samples.nt4h.game;

import com.google.common.collect.Lists;
import lombok.*;
import org.springframework.samples.nt4h.action.Phase;
import org.springframework.samples.nt4h.card.enemy.EnemyInGame;
import org.springframework.samples.nt4h.card.hero.Hero;
import org.springframework.samples.nt4h.card.hero.HeroInGame;
import org.springframework.samples.nt4h.game.exceptions.FullGameException;
import org.springframework.samples.nt4h.game.exceptions.HeroAlreadyChosenException;
import org.springframework.samples.nt4h.model.NamedEntity;
import org.springframework.samples.nt4h.player.Player;
import org.springframework.samples.nt4h.player.exceptions.RoleAlreadyChosenException;
import org.springframework.samples.nt4h.stage.Stage;
import org.springframework.samples.nt4h.turn.Turn;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "games")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Game extends NamedEntity {

    private LocalDateTime startDate;

    private LocalDateTime finishDate;

    private Integer maxPlayers;



    @NotNull
    @Enumerated(EnumType.STRING)
    private Mode mode;

    public int isUniClass() {
        return mode == Mode.UNI_CLASS ? 1 : 2;
    }


    @Enumerated(EnumType.STRING)
    private Phase phase;

    private String password;

    @Enumerated(EnumType.STRING)
    private Accessibility accessibility;

    private boolean hasStages;

    // Relaciones
    @OneToMany(cascade = CascadeType.ALL)
    private List<Player> alivePlayersInTurnOrder;

    @OneToOne(cascade = CascadeType.ALL)
    private Turn currentTurn;

    @OneToMany(cascade = CascadeType.ALL)
    @Size(max = 3)
    private List<EnemyInGame> actualOrcs;

    @OneToMany
    private List<EnemyInGame> allOrcsInGame;

    @OneToMany(cascade = CascadeType.ALL)
    private List<EnemyInGame> passiveOrcs;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Player> players;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Stage> stage;

    public void addPlayer(Player player) throws FullGameException {
        if (this.players == null)
            players = Lists.newArrayList(player);
        else if (this.players.size() >= this.maxPlayers)
            throw new FullGameException();
        else
            this.players.add(player);

    }

    public void addPlayerWithNewHero(Player player, HeroInGame hero) throws FullGameException, HeroAlreadyChosenException, RoleAlreadyChosenException {
        if (isHeroAlreadyChosen(hero.getHero()))
            throw new HeroAlreadyChosenException();
         else {
            player.addHero(hero);
            addPlayer(player);
        }

    }


    public boolean isHeroAlreadyChosen(Hero hero) {
        if(this.players==null)return false;
        return this.players.stream().anyMatch(player -> player.getHeroes().stream().anyMatch(h -> h.getHero().equals(hero)));

    }

}
