package org.springframework.samples.nt4h.card.hero;

import org.springframework.samples.nt4h.card.ability.Ability;
import org.springframework.samples.nt4h.card.ability.AbilityService;
import org.springframework.samples.nt4h.card.ability.DeckService;
import org.springframework.samples.nt4h.card.enemy.EnemyInGame;
import org.springframework.samples.nt4h.card.enemy.EnemyService;
import org.springframework.samples.nt4h.game.Game;
import org.springframework.samples.nt4h.game.GameService;
import org.springframework.samples.nt4h.message.CacheManager;
import org.springframework.samples.nt4h.player.Player;
import org.springframework.samples.nt4h.player.PlayerService;
import org.springframework.samples.nt4h.statistic.Statistic;
import org.springframework.samples.nt4h.statistic.StatisticService;
import org.springframework.samples.nt4h.turn.TurnService;
import org.springframework.samples.nt4h.user.User;
import org.springframework.samples.nt4h.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.Optional;

/**
 * Las habilidades del ladrón son:
 * - Al corazón.
 * - Ataque furtivo.
 * - Ballesta precisa.
 * - En las sombras.
 * - Engañar.
 * - Robar bolsillos.
 * - Saqueo1.
 * - Saque2.
 * - Trampa.
 */
@Controller
@RequestMapping("/abilities")
public class AbilityThiefController {

    private final static String PAGE_MAKE_DAMAGE = "redirect:/heroAttack/makeDamage";
    private final UserService userService;
    private final AbilityService abilityService;
    private final CacheManager cacheManager;
    private final StatisticService statisticService;
    private final DeckService deckService;

    public AbilityThiefController(UserService userService, AbilityService abilityService, CacheManager cacheManager, StatisticService statisticService, DeckService deckService) {
        this.userService = userService;
        this.abilityService = abilityService;
        this.cacheManager = cacheManager;
        this.statisticService = statisticService;
        this.deckService = deckService;
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

    // Al corazón. (fufa)
    @GetMapping("/toTheHeart")
    private String toTheHeart(HttpSession session) {
        Player currentPlayer = getCurrentPlayer();
        // Comprobamos si podemos cargarnos al enemigo.
        EnemyInGame attackedEnemy = cacheManager.getAttackedEnemy(session);
        Integer sharpeningStone = cacheManager.getSharpeningStone(session);
        Integer extraDamage = cacheManager.getEnemiesThatReceiveMoreDamageForEnemy(session, attackedEnemy);
        System.out.println("Sharpening stone: " + sharpeningStone);
        abilityService.getAllAbilities().forEach(ability -> {
            if (ability.getName().contains("Al")) {
               System.out.println("Al corazon: " + ability.getName());
            }
        });
        Ability ability = abilityService.getAbilityByName("Al corazon");
        System.out.println("Al corazon: " + ability.getId());
        int attack = cacheManager.getAttack(session) + ability.getAttack() + sharpeningStone + extraDamage;
        if (attack >= attackedEnemy.getActualHealth() && cacheManager.isFirstToTheHeart(session)) {
            cacheManager.setFirstToTheHeart(session);
            // Gana uno de oro.
            statisticService.gainGold(currentPlayer, 1);
            System.out.println("Actualización del oro?" + getLoggedUser().getStatistic().getGold());
        }
        // Pierde 1 carta.
        deckService.fromDeckToDiscard(currentPlayer, currentPlayer.getDeck());
        return PAGE_MAKE_DAMAGE;
    }

    // Ataque furtivo. (fufa)
    @GetMapping("/stealthAttack")
    private String stealthAttack(HttpSession session) {
        Player currentPlayer = getCurrentPlayer();
        // Comprobamos si podemos cargarnos al enemigo.
        EnemyInGame attackedEnemy = cacheManager.getAttackedEnemy(session);
        Integer sharpeningStone = cacheManager.getSharpeningStone(session);
        Integer extraDamage = cacheManager.getEnemiesThatReceiveMoreDamageForEnemy(session, attackedEnemy);
        Ability ability = abilityService.getAbilityByName("Ataque furtivo");
        int attack = cacheManager.getAttack(session) + ability.getAttack() + sharpeningStone + extraDamage;
        if (attack >= attackedEnemy.getActualHealth() && cacheManager.isFirstStealthAttack(session)) {
            cacheManager.setFirstStealthAttack(session);
            // Gana uno de oro.
            statisticService.gainGold(currentPlayer, 1);
        }
        return PAGE_MAKE_DAMAGE;
    }

    // Ballesta precisa. (fufa)
    @GetMapping("/preciseBow")
    private String preciseBow(HttpSession session) {
        // Si ya ha sido atacado con ballesta precisa, realiza más daño.
        if (cacheManager.hasAlreadyAttackedWithPreciseBow(session))
            cacheManager.addAttack(session,  1);
        else
            cacheManager.addAlreadyAttackedWithPreciseBow(session);
        return PAGE_MAKE_DAMAGE;
    }

    // En las sombras. (fufa)
    @GetMapping("/inTheShadows")
    private String inTheShadows(HttpSession session) {
        // Previene dos puntos de daño.
        cacheManager.setDefend(session, 2);
        return PAGE_MAKE_DAMAGE;
    }

    // Engañar. (fufa)
    @GetMapping("/deceive")
    private String deceive(HttpSession session) {
        Player currentPlayer = getCurrentPlayer();
        // Elegimos el enemigo que no va a realizar daño.
        Statistic statistic = currentPlayer.getStatistic();
        if (statistic.getGold() >= 2) {
            statisticService.loseGold(statistic, 2);
            cacheManager.addPreventDamageFromEnemies(session);
        }
        return PAGE_MAKE_DAMAGE;
    }

    // Robar bolsillos. (fufa)
    @GetMapping("/stealPockets")
    private String stealPockets() {
        Player currentPlayer = getCurrentPlayer();
        // Roba una moneda a cada héroe.
        for (Player player : getGame().getPlayers()) {
            Statistic statistic = player.getStatistic();
            if (statistic.getGold() > 0 && player != currentPlayer) {
                statisticService.loseGold(statistic, 1);
                statisticService.gainGold(currentPlayer, 1);
            }
        }
        return PAGE_MAKE_DAMAGE;
    }

    // Saqueo1 (fufa)
    @GetMapping("/loot1")
    private String loot1() {
        Player currentPlayer = getCurrentPlayer();
        // Ganas dos monedas por cada enemigo vivo.
        statisticService.gainGold(currentPlayer, 2 * getGame().getActualOrcs().size());
        return PAGE_MAKE_DAMAGE;
    }

    // Saqueo2 (fufa)
    @GetMapping("/loot2")
    public String loot2() {
        Player currentPlayer = getCurrentPlayer();
        // Ganas una moneda por cada enemigo vivo.
        statisticService.gainGold(currentPlayer, getGame().getActualOrcs().size());
        // Gana un punto de gloria.
        statisticService.gainGlory(currentPlayer, 1);
        return PAGE_MAKE_DAMAGE;
    }

    // Trampa
    @GetMapping("/trap")
    public String trap(HttpSession session) {
        // El enemigo seleccionado morirá al terminar la fase de ataque.
        EnemyInGame attackedEnemy = cacheManager.getAttackedEnemy(session);
        if (attackedEnemy == null) {
            Optional<EnemyInGame> enemy = getGame().getActualOrcs().stream().max(Comparator.comparing(EnemyInGame::getActualHealth));
            enemy.ifPresent(enemyInGame -> cacheManager.addCapturedEnemies(session, enemyInGame));
        }
        else
            cacheManager.addCapturedEnemies(session);
        return PAGE_MAKE_DAMAGE;
    }
}
