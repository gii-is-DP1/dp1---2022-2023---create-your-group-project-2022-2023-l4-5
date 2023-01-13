package org.springframework.samples.nt4h.card.ability;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.nt4h.card.enemy.EnemyInGame;
import org.springframework.samples.nt4h.card.product.ProductInGame;
import org.springframework.samples.nt4h.card.product.ProductService;
import org.springframework.samples.nt4h.game.Game;
import org.springframework.samples.nt4h.game.GameService;
import org.springframework.samples.nt4h.message.CacheManager;
import org.springframework.samples.nt4h.player.Player;
import org.springframework.samples.nt4h.player.PlayerService;
import org.springframework.samples.nt4h.turn.Phase;
import org.springframework.samples.nt4h.turn.Turn;
import org.springframework.samples.nt4h.turn.TurnService;
import org.springframework.samples.nt4h.user.User;
import org.springframework.samples.nt4h.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
@RequestMapping("/abilities")
public class AbilityController {

    private final static String PAGE_MAKE_DAMAGE = "redirect:/heroAttack/makeDamage";
    private final static String PAGE_ABILITY = "redirect:/abilities/";

    private final UserService userService;
    private final GameService gameService;
    private final PlayerService playerService;
    private final AbilityService abilityService;
    private final ProductService productService;
    private final TurnService turnService;
    private final CacheManager cacheManager;

    @Autowired
    public AbilityController(UserService userService, GameService gameService, PlayerService playerService, AbilityService abilityService, ProductService productService, TurnService turnService, CacheManager cacheManager) {
        this.userService = userService;
        this.gameService = gameService;
        this.playerService = playerService;
        this.abilityService = abilityService;
        this.productService = productService;
        this.turnService = turnService;
        this.cacheManager = cacheManager;
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

    @ModelAttribute("loggedPlayer")
    public Player getLoggedPlayer() {
        return getLoggedUser().getPlayer();
    }

    @PostMapping("/loseCard")
    public String loseCard(Turn turn, HttpSession session) {
        Player currentPlayer = getCurrentPlayer();
        AbilityInGame abilityInGame = turn.getCurrentAbility();
        Deck deck = currentPlayer.getDeck();
        deck.discardCardOnHand(abilityInGame); // Esto debe de ser un efecto
        return cacheManager.getNextUrl(session).orElse(PAGE_MAKE_DAMAGE);
    }

    @PostMapping("/chooseEnemy")
    public String chooseEnemy(Turn turn, @RequestParam("name") String name, HttpSession session) {
        EnemyInGame enemyInGame = turn.getCurrentEnemy();
        if (name != null) {

            if (Objects.equals(name, "getOutEnemy")) {
                Game game = getGame();
                // Colocar el enemigo por el último.
                EnemyInGame lastOne = game.getAllOrcsInGame().get(game.getAllOrcsInGame().size() - 1);
                game.getAllOrcsInGame().set(game.getAllOrcsInGame().size() - 1, enemyInGame);
                int index = game.getActualOrcs().indexOf(enemyInGame);
                game.getActualOrcs().set(index, lastOne);
                gameService.saveGame(game);
            }
            else {
                Object enemies = session.getAttribute(name);
                if (enemies == null)
                    session.setAttribute(name, enemyInGame.getId());
                else {
                    session.setAttribute(name, enemies + "," + enemyInGame.getId());
                }

            }

        }
        return cacheManager.getNextUrl(session).orElse(PAGE_MAKE_DAMAGE);
    }

    @GetMapping("/findInDiscard")
    public String findInDiscard(Turn turn) {
        // Cogemos la carta elegida de la pila de descarte.
        AbilityInGame abilityInGame = turn.getCurrentAbility();
        // La colocamos en la mano.
        Player currentPlayer = getCurrentPlayer();
        Deck deck = currentPlayer.getDeck();
        deck.getInDiscard().remove(abilityInGame);
        deck.getInHand().add(abilityInGame);
        playerService.savePlayer(currentPlayer);
        return PAGE_MAKE_DAMAGE;
    }

    @PostMapping("/chooseAbilityFromDeck")
    public String chooseAbilityFromDeck(Turn turn, HttpSession session) {
        AbilityInGame abilityInGame = turn.getCurrentAbility();
        session.setAttribute("inDeck", abilityInGame.getId());
        return cacheManager.getNextUrl(session).orElse(PAGE_MAKE_DAMAGE);
    }

    @PostMapping("/exchangeCards")
    public String exchangeCards(Turn turn, HttpSession session) {
        AbilityInGame inDeck = abilityService.getAbilityInGameById(((Integer)session.getAttribute("inDeck")));
        AbilityInGame inHand = turn.getCurrentAbility();
        Deck deck = getCurrentPlayer().getDeck();
        deck.getInHand().remove(inHand);
        deck.getInHand().add(inDeck);
        deck.getInDeck().remove(inDeck);
        deck.getInDeck().add(inHand);
        playerService.savePlayer(getCurrentPlayer());
        return cacheManager.getNextUrl(session).orElse(PAGE_MAKE_DAMAGE);
    }

    @PostMapping("/chooseProductFromMarket")
    public String chooseProductFromMarket(Turn turn, HttpSession session) {
        AbilityInGame abilityInGame = turn.getCurrentAbility();
        session.setAttribute("inMarket", abilityInGame.getId());
        Object nextUrl = session.getAttribute("nextUrl");
        return nextUrl == null ? PAGE_MAKE_DAMAGE : nextUrl.toString();
    }

    @PostMapping("/exchangeProducts")
    public String exchangeProducts(Turn turn, HttpSession session) {
        ProductInGame inMarket = productService.getProductInGameById(((Integer) session.getAttribute("inMarket")));
        ProductInGame inSale = turn.getCurrentProduct();
        Integer id = inSale.getId();
        inSale.setId(inMarket.getId());
        inMarket.setId(id);
        productService.saveProductInGame(inSale);
        productService.saveProductInGame(inMarket);
        return cacheManager.getNextUrl(session).orElse(PAGE_MAKE_DAMAGE);
    }

    @GetMapping
    public String findEffect() {
        Player currentPlayer = getCurrentPlayer();
        Player loggedPlayer = getLoggedPlayer();
        if (currentPlayer != loggedPlayer)
            return PAGE_MAKE_DAMAGE;
        Turn turn = turnService.getTurnsByPhaseAndPlayerId(Phase.HERO_ATTACK, currentPlayer.getId());
        AbilityInGame abilityInGame = turn.getCurrentAbility();
        return PAGE_ABILITY + abilityInGame.getAbility().getPathName();
    }
}
