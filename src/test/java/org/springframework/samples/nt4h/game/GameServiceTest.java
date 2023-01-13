
package org.springframework.samples.nt4h.game;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.nt4h.card.hero.Hero;
import org.springframework.samples.nt4h.card.hero.HeroInGame;
import org.springframework.samples.nt4h.card.hero.HeroService;
import org.springframework.samples.nt4h.exceptions.NotFoundException;
import org.springframework.samples.nt4h.game.exceptions.FullGameException;
import org.springframework.samples.nt4h.message.Advise;
import org.springframework.samples.nt4h.player.Player;
import org.springframework.samples.nt4h.player.exceptions.RoleAlreadyChosenException;
import org.springframework.samples.nt4h.user.User;
import org.springframework.samples.nt4h.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class GameServiceTest {
    @Autowired
    protected GameService gameService;
    @Autowired
    protected UserService userService;
    @Autowired
    private HeroService heroService;
    private int idGame;
    private Game game;
    private int numGames;

    @MockBean
    private Advise advise;

    @BeforeEach
    void setUp() throws FullGameException, RoleAlreadyChosenException {
        User user = userService.getUserById(1);
        game = Game.createGame( "Prueba",   Mode.UNI_CLASS, 2, "");
        Player player = Player.createPlayer(user, game, true);
        game.setFinishDate(LocalDateTime.of(2020, 1, 2, 0, 0));
        Hero hero = heroService.getHeroById(1);
        HeroInGame heroInGame = HeroInGame.createHeroInGame(hero, user.getPlayer());
        player.addHero(heroInGame);
        gameService.saveGame(game);
        idGame = game.getId();
        numGames = gameService.getAllGames().size();
    }

    @Test
    public void testFindById() {
        assertEquals(game, gameService.getGameById(idGame));
    }
    @Test
    public void testFindByIncorrectId() {
        assertThrows(NotFoundException.class, () -> gameService.getGameById(-1));
    }
    @Test
    public void testFindAll() {
        assertEquals(numGames, gameService.getAllGames().size());
    }

}
