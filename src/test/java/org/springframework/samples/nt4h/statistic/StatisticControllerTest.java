package org.springframework.samples.nt4h.statistic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;

import org.javatuples.Quartet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.samples.nt4h.card.ability.Ability;
import org.springframework.samples.nt4h.card.ability.AbilityCardType;
import org.springframework.samples.nt4h.card.ability.AbilityInGame;
import org.springframework.samples.nt4h.card.ability.AbilityInGameRepository;
import org.springframework.samples.nt4h.card.ability.AbilityRepository;
import org.springframework.samples.nt4h.card.ability.AbilityService;
import org.springframework.samples.nt4h.card.ability.Deck;
import org.springframework.samples.nt4h.card.ability.DeckRepository;
import org.springframework.samples.nt4h.card.ability.DeckService;
import org.springframework.samples.nt4h.card.enemy.Enemy;
import org.springframework.samples.nt4h.card.enemy.EnemyInGame;
import org.springframework.samples.nt4h.card.hero.Role;
import org.springframework.samples.nt4h.card.product.Product;
import org.springframework.samples.nt4h.card.product.ProductInGame;
import org.springframework.samples.nt4h.card.product.StateProduct;
import org.springframework.samples.nt4h.game.Accessibility;
import org.springframework.samples.nt4h.game.Game;
import org.springframework.samples.nt4h.game.Mode;
import org.springframework.samples.nt4h.message.Advise;
import org.springframework.samples.nt4h.message.MessageRepository;
import org.springframework.samples.nt4h.message.MessageService;
import org.springframework.samples.nt4h.player.Player;
import org.springframework.samples.nt4h.player.PlayerRepository;
import org.springframework.samples.nt4h.player.PlayerService;
import org.springframework.samples.nt4h.player.Tier;
import org.springframework.samples.nt4h.turn.Phase;
import org.springframework.samples.nt4h.turn.Turn;
import org.springframework.samples.nt4h.turn.TurnRepository;
import org.springframework.samples.nt4h.turn.TurnService;
import org.springframework.samples.nt4h.user.User;
import org.springframework.samples.nt4h.user.UserRepository;
import org.springframework.samples.nt4h.user.UserService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;

@ContextConfiguration(classes = {StatisticController.class})
@ExtendWith(SpringExtension.class)
class StatisticControllerTest {
    @MockBean
    private Advise advise;

    @MockBean
    private PlayerService playerService;

    @Autowired
    private StatisticController statisticController;

    @MockBean
    private StatisticService statisticService;

    @MockBean
    private UserService userService;

    private Player player;
    private Game game;
    private User user;
    private Turn turn;
    private AbilityInGame abilityInGame;
    private EnemyInGame enemyInGame;

    @BeforeEach
    void setUp() {
        Deck deck = new Deck();
        deck.setId(1);
        deck.setInDeck(new ArrayList<>());
        deck.setInDiscard(new ArrayList<>());
        deck.setInHand(new ArrayList<>());
        player = new Player();
        player.setAlive(true);
        player.setBirthDate(null);
        player.setDamageProtect(1);
        player.setDeck(new Deck());
        player.setGame(new Game());
        player.setHasEvasion(true);
        player.setHeroes(new ArrayList<>());
        player.setHost(true);
        player.setId(1);
        player.setName("Name");
        player.setNextPhase(Phase.START);
        player.setReady(true);
        player.setSequence(1);
        player.setStatistic(new Statistic());
        player.setTurns(new ArrayList<>());
        player.setWounds(1);
        turn = new Turn();
        turn.setCurrentEnemy(new EnemyInGame());
        turn.setCurrentProduct(new ProductInGame());
        turn.setGame(new Game());
        turn.setId(1);
        turn.setPhase(Phase.START);
        turn.setPlayer(new Player());
        turn.setUsedAbilities(new ArrayList<>());
        turn.setUsedEnemies(new ArrayList<>());
        turn.setUsedProducts(new ArrayList<>());

        Statistic statistic = new Statistic();
        statistic.setDamageDealt(1);
        statistic.setGlory(1);
        statistic.setGold(1);
        statistic.setId(1);
        statistic.setNumOrcsKilled(10);
        statistic.setNumPlayedGames(10);
        statistic.setNumPlayers(10);
        statistic.setNumWarLordKilled(10);
        statistic.setNumWonGames(10);
        statistic.setTimePlayed(1);
        game = new Game();
        game.setAccessibility(Accessibility.PUBLIC);
        game.setActualOrcs(new ArrayList<>());
        game.setAlivePlayersInTurnOrder(new ArrayList<>());
        game.setAllOrcsInGame(new ArrayList<>());
        game.setCurrentPlayer(player);
        game.setCurrentTurn(turn);
        game.setFinishDate(LocalDateTime.of(1, 1, 1, 1, 1));
        game.setId(1);
        game.setMaxPlayers(3);
        game.setMode(Mode.UNI_CLASS);
        game.setName("Juego Test");
        game.setPassiveOrcs(new ArrayList<>());
        game.setPassword("malo");
        game.setPlayers(new ArrayList<>());
        game.setSpectators(new ArrayList<>());
        game.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        game.setStatistic(statistic);

        Ability ability = new Ability();
        ability.setAttack(1);
        ability.setBackImage("Back Image");
        ability.setFrontImage("Front Image");
        ability.setId(1);
        ability.setMaxUses(3);
        ability.setName("Abilidad");
        ability.setPathName("Path Name");
        ability.setQuantity(1);
        ability.setRole(Role.WIZARD);


        ProductInGame productInGame = new ProductInGame();
        productInGame.setGame(new Game());
        productInGame.setId(1);
        productInGame.setName("Daga");
        productInGame.setPlayer(new Player());
        productInGame.setProduct(new Product());
        productInGame.setStateProduct(StateProduct.IN_SALE);
        productInGame.setTimesUsed(1);

        abilityInGame = new AbilityInGame();
        abilityInGame.setAbility(ability);
        abilityInGame.setAbilityCardType(AbilityCardType.WARRIOR);
        abilityInGame.setAttack(1);
        abilityInGame.setId(1);
        abilityInGame.setPlayer(player);
        abilityInGame.setProduct(true);
        abilityInGame.setProductInGame(productInGame);
        abilityInGame.setTimesUsed(1);


        Enemy enemy = new Enemy();
        enemy.setBackImage("Back Image");
        enemy.setFrontImage("Front Image");
        enemy.setHasCure(true);
        enemy.setHealth(1);
        enemy.setHiddenGlory(1);
        enemy.setHiddenGold(1);
        enemy.setId(1);
        enemy.setIsNightLord(true);
        enemy.setLessDamageWizard(true);
        enemy.setMaxUses(3);
        enemy.setName("Orco");
        enemy.setNotHiddenGlory(1);
        enemy.setNotHiddenGold(1);

        enemyInGame = new EnemyInGame();
        enemyInGame.setActualHealth(1);
        enemyInGame.setEnemy(enemy);
        enemyInGame.setId(1);
        enemyInGame.setNightLord(true);

        Product product = new Product();
        product.setAttack(1);
        product.setBackImage("Back Image");
        product.setCapacity(new ArrayList<>());
        product.setFrontImage("Front Image");
        product.setId(1);
        product.setMaxUses(3);
        product.setName("Name");
        product.setPathName("Path Name");
        product.setPrice(1);
        product.setQuantity(1);

        deck.setInDiscard(List.of(abilityInGame));
        deck.setInHand(List.of(abilityInGame));
        deck.setInDeck(List.of(abilityInGame));
        player.setStatistic(statistic);
        player.setDeck(deck);
        game.setCurrentPlayer(player);
        turn.setCurrentAbility(abilityInGame);
        game.setCurrentTurn(turn);

        user = new User();
        user.setAuthority("DOKTOL");
        user.setAvatar("Avatar");
        user.setBirthDate(LocalDate.ofEpochDay(1L));
        user.setDescription("Wink");
        user.setEnable("Enable");
        user.setFriends(new ArrayList<>());
        user.setGame(game);
        user.setId(1);
        user.setIsConnected(true);
        user.setPassword("rocker");
        user.setPlayer(player);
        user.setReceivedMessages(new ArrayList<>());
        user.setSentMessages(new ArrayList<>());
        user.setStatistic(statistic);
        user.setTier(Tier.IRON);
        user.setUsername("The Rock");
    }


    @Test
    void testShowStatistics() throws Exception {
        when(statisticService.getStatistics()).thenReturn(new ArrayList<>());
        when(userService.getLoggedUser()).thenReturn(user);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/statistics/allStatistics");
        MockMvcBuilders.standaloneSetup(statisticController)
            .build()
            .perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.model().size(3))
            .andExpect(MockMvcResultMatchers.model().attributeExists("allStatistic", "loggedUser", "statistic"))
            .andExpect(MockMvcResultMatchers.view().name("statistics/allStatistics"))
            .andExpect(MockMvcResultMatchers.forwardedUrl("statistics/allStatistics"));
    }

    @Test
    void testSpecificStatistics() {
        when(userService.getLoggedUser()).thenReturn(user);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        when(statisticService.getStatistics()).thenReturn(List.of(Quartet.with("Hola",2.0,2,2)));
        ModelMap model = new ModelMap();
        assertEquals("statistics/detailedStatistics",
            statisticController.specificStatistics(0, model, new MockHttpSession()));
    }

}

