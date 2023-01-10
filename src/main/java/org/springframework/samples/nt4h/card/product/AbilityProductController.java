package org.springframework.samples.nt4h.card.product;

import org.springframework.samples.nt4h.card.ability.AbilityInGame;
import org.springframework.samples.nt4h.card.ability.AbilityService;
import org.springframework.samples.nt4h.card.ability.Deck;
import org.springframework.samples.nt4h.card.enemy.EnemyInGame;
import org.springframework.samples.nt4h.card.enemy.EnemyService;
import org.springframework.samples.nt4h.game.Game;
import org.springframework.samples.nt4h.game.GameService;
import org.springframework.samples.nt4h.player.Player;
import org.springframework.samples.nt4h.player.PlayerService;
import org.springframework.samples.nt4h.turn.TurnService;
import org.springframework.samples.nt4h.user.User;
import org.springframework.samples.nt4h.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * Las habilidades de los productos son:
 * - Daga élfica.
 * - Poción curativa.
 * - Piedra amolar.
 * - Vial de conjuración.
 * - Elixir de concentración.
 * - Capa élfica.
 * - Armadura de placas.
 * - Alabarda orca.
 * - Arco compuesto.
 */
@Controller
@RequestMapping("/product")
public class AbilityProductController {

    private final String PAGE_ENEMY_ATTACK = "redirect:/enemyAttack";
    private final String PAGE_HERO_ATTACK = "redirect:/heroAttack";
    private final String PAGE_LOSE_CARD = "redirect:/loseCard";
    private final String VIEW_LOSE_CARD = "abilities/loseCard";
    private final String VIEW_CHOSE_ENEMY = "abilities/choseEnemy";
    private final String VIEW_FIND_IN_DISCARD = "abilities/findInDiscard";
    private final UserService userService;
    private final AbilityService abilityService;
    private final PlayerService playerService;
    private final TurnService turnService;
    private final GameService gameService;
    private final EnemyService enemyService;

    public AbilityProductController(UserService userService, AbilityService abilityService, PlayerService playerService, TurnService turnService, GameService gameService, EnemyService enemyService) {
        this.userService = userService;
        this.abilityService = abilityService;
        this.playerService = playerService;
        this.turnService = turnService;
        this.gameService = gameService;
        this.enemyService = enemyService;
    }

    @ModelAttribute("loggedUser")
    public User getLoggedUser() {
        return userService.getLoggedUser();
    }

    @ModelAttribute("game")
    public Game getGame() {
        return getLoggedUser().getGame();
    }

    @ModelAttribute("currentPlayer")
    public Player getCurrentPlayer() {
        return getGame().getCurrentPlayer();
    }

    @ModelAttribute("logggedPlayer")
    public Player getLoggedPlayer() {
        return getLoggedUser().getPlayer();
    }

    //  Daga élfica
    @GetMapping("/elfDagger/{cardId}")
    private String elfDagger(@PathVariable("cardId") int cardId, HttpSession session) {
        Player currentPlayer = getCurrentPlayer();
        Player loggedPlayer = getLoggedPlayer();
        if (currentPlayer != loggedPlayer)
            return PAGE_HERO_ATTACK;
        // Pierde la carta si no tiene el héreo pericia.
        session.setAttribute("deleteCard", true);
        return PAGE_HERO_ATTACK;
    }

    // Poción curativa
    @GetMapping("/healingPotion/{cardId}")
    private String healingPotion(@PathVariable("cardId") int cardId, HttpSession session) {
        Player currentPlayer = getCurrentPlayer();
        Player loggedPlayer = getLoggedPlayer();
        if (currentPlayer != loggedPlayer)
            return PAGE_HERO_ATTACK;
        // Elimina una herida.
        currentPlayer.setWounds(currentPlayer.getWounds() - 1);
        // Eliminamos la carta.
        session.setAttribute("deleteCard", true);
        return PAGE_HERO_ATTACK;
    }

    // Piedra amolar
    @GetMapping("/sharpeningStone/{cardId}")
    private String sharpeningStone(@PathVariable("cardId") int cardId, HttpSession session) {
        Player currentPlayer = getCurrentPlayer();
        Player loggedPlayer = getLoggedPlayer();
        if (currentPlayer != loggedPlayer)
            return PAGE_HERO_ATTACK;
        // Añadimos el daño por piedra amolar.
        session.setAttribute("sharpeningStone", 1);
        return PAGE_HERO_ATTACK;
    }

    // Vial de conjuración
    @GetMapping("/conjureVial/{cardId}")
    private String conjureVial(@PathVariable("cardid") int cardId, HttpSession session, ModelMap model) {
        Player currentPlayer = getCurrentPlayer();
        Player loggedPlayer = getLoggedPlayer();
        if (currentPlayer != loggedPlayer)
            return PAGE_HERO_ATTACK;
        model.put("discard", currentPlayer.getDeck().getInDiscard());
        return VIEW_FIND_IN_DISCARD;
    }

    // Elixir de concentración.
    @GetMapping("/concentrationElixir/{cardId}")
    private String concentrationVial(@PathVariable("cardId") int cardId, HttpSession session) {
        Player currentPlayer = getCurrentPlayer();
        Player loggedPlayer = getLoggedPlayer();
        if (currentPlayer != loggedPlayer)
            return PAGE_HERO_ATTACK;
        // Roba 3 cartas.
        Deck deck = currentPlayer.getDeck();
        for (var i = 0; i < 3; i++) {
            AbilityInGame abilityInGame = deck.getInDeck().get(0);
            deck.getInDeck().remove(abilityInGame);
            deck.getInHand().add(abilityInGame);
        }
        playerService.savePlayer(currentPlayer);
        return PAGE_HERO_ATTACK;
    }

    // Capa élfica
    @GetMapping("/elfCloak/{cardId}")
    private String elfCloak(@PathVariable("cardId") int cardId, HttpSession session) {
        Player currentPlayer = getCurrentPlayer();
        Player loggedPlayer = getLoggedPlayer();
        if (currentPlayer != loggedPlayer)
            return PAGE_HERO_ATTACK;
        EnemyInGame enemyInGame = (EnemyInGame) session.getAttribute("attackedEnemy");
        enemyInGame.setNoAttackThisTurn(true);
        enemyService.saveEnemyInGame(enemyInGame);
        // El enemigo selecionado no causa daño este turno.
        return PAGE_HERO_ATTACK;
    }

    // Armadura de placas.
    @GetMapping("/plateArmor/{cardId}")
    private String plateArmor(@PathVariable("cardId") int cardId, HttpSession session) {
        Player currentPlayer = getCurrentPlayer();
        Player loggedPlayer = getLoggedPlayer();
        if (currentPlayer != loggedPlayer)
            return PAGE_HERO_ATTACK;
        // Recupera 4 cartas.
        Deck deck = currentPlayer.getDeck();
        for (var i = 0; i < 4; i++) {
            AbilityInGame abilityInGame = deck.getInDiscard().get(0);
            deck.getInDiscard().remove(abilityInGame);
            deck.getInHand().add(abilityInGame);
        }
        playerService.savePlayer(currentPlayer);
        return PAGE_HERO_ATTACK;
    }

    // Alabarda orca
    @GetMapping("/orcaLance/{cardId}")
    private String orcLance(@PathVariable("cardId") int cardId, HttpSession session) {
        return PAGE_HERO_ATTACK;
    }

    // Arco compuesto.
    @GetMapping("/compoundBow/{cardId}")
    private String compoundBound() {
        return PAGE_HERO_ATTACK;
    }
}
