package org.springframework.samples.nt4h.turn;

import org.javatuples.Pair;
import org.springframework.samples.nt4h.game.Game;
import org.springframework.samples.nt4h.game.GameService;
import org.springframework.samples.nt4h.message.Message;
import org.springframework.samples.nt4h.player.Player;
import org.springframework.samples.nt4h.statistic.Statistic;
import org.springframework.samples.nt4h.user.User;
import org.springframework.samples.nt4h.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/end")
public class EndController {

    private final static String VIEW_END = "turns/endPhase";
    private final static String VIEW_WELCOME =  "redirect:/";

    private final UserService userService;
    private final GameService gameService;

    public EndController(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @ModelAttribute("loggedUser")
    public User getLoggedUser() {
        return userService.getLoggedUser();
    }

    @ModelAttribute("currentPlayer")
    public Player getPlayer() {
        return getLoggedUser().getGame().getCurrentPlayer();
    }

    @ModelAttribute("game")
    public Game getGame() {
        return getPlayer().getGame();
    }

    @ModelAttribute("newTurn")
    public Turn getNewTurn() {
        return new Turn();
    }

    @ModelAttribute("loggedPlayer")
    public Player getLoggedPlayer() {
        User loggedUser = getLoggedUser();
        return loggedUser.getPlayer() != null ? loggedUser.getPlayer() : Player.builder().statistic(Statistic.createStatistic()).build();
    }

    @ModelAttribute("chat")
    public Message getChat() {
        return new Message();
    }

    @GetMapping
    public String showEnd(ModelMap model) {
        List<Player> players = getGame().getPlayers();
        List<Pair<Player, Integer>> pairs = new ArrayList<>();
        for (Player player : players) {
            Integer gold = player.getStatistic().getGold();
            Integer glory = player.getStatistic().getGlory();
            Integer kills = player.getStatistic().getNumOrcsKilled() + player.getStatistic().getNumWarLordKilled();
            Integer total = gold + glory + kills;
            pairs.add(new Pair<>(player, total));
        }
        pairs.sort((o1, o2) -> o2.getValue1().compareTo(o1.getValue1()));
        User winner = userService.getUserByUsername(pairs.get(0).getValue0().getName());
        Integer won = winner.getStatistic().getNumWonGames();
        winner.getStatistic().setNumWonGames(won+1);
        userService.upRank(winner.getId());
        model.addAttribute("punctuations", pairs);
        return VIEW_END;
    }

    @GetMapping("/finish")
    public String finishGame() {
        Game game = getGame();
        gameService.deleteGameById(game.getId());
        return VIEW_WELCOME;
    }


}
