package org.springframework.samples.nt4h.game;



import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.nt4h.card.hero.Hero;
import org.springframework.samples.nt4h.card.hero.HeroInGame;
import org.springframework.samples.nt4h.card.hero.HeroService;
import org.springframework.samples.nt4h.game.exceptions.*;
import org.springframework.samples.nt4h.message.Advise;
import org.springframework.samples.nt4h.message.Message;
import org.springframework.samples.nt4h.player.Player;
import org.springframework.samples.nt4h.player.PlayerService;
import org.springframework.samples.nt4h.player.exceptions.RoleAlreadyChosenException;
import org.springframework.samples.nt4h.statistic.Statistic;
import org.springframework.samples.nt4h.user.User;
import org.springframework.samples.nt4h.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

// TODO: Cambiar nombre y enlaces.
@Controller
@RequestMapping("/games")
public class GameController {

    // Constantes.
    private static final String VIEW_GAME_CREATE = "games/createGame";
    private static final String VIEW_GAME_LIST = "games/gamesList";
    private static final String VIEW_GAME_LOBBY = "games/gameLobby";
    private static final String PAGE_GAME_LOBBY = "redirect:/games/{gameId}";
    private static final String VIEW_GAME_HERO_SELECT = "games/heroSelect";
    private static final String PAGE_GAMES = "redirect:/games";
    private static final String VIEW_GAME_ORDER = "games/selectOrder";
    private static final String VIEW_GAME_PREORDER = "games/preSelectOrder";
    private static final String PAGE_CURRENT_GAME = "redirect:/games/current";
    // Servicios
    private final GameService gameService;

    private final HeroService heroService;
    private final UserService userService;
    private final PlayerService playerService;
    private final Advise advise;


    @Autowired
    public GameController(GameService gameService, HeroService heroService, UserService userService, PlayerService playerService, Advise advise) {
        this.gameService = gameService;
        this.heroService = heroService;
        this.userService = userService;
        this.playerService = playerService;
        this.advise = advise;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @ModelAttribute("mode")
    public List<Mode> getMode() {
        return Lists.newArrayList(Mode.UNI_CLASS, Mode.MULTI_CLASS);
    }

    @ModelAttribute("heroes")
    public List<Hero> getHeroes() {
        return heroService.getAllHeros();
    }

    @ModelAttribute("accessibility")
    public List<Accessibility> getAccessibility() {
        return Lists.newArrayList(Accessibility.PUBLIC, Accessibility.PRIVATE);
    }

    @ModelAttribute("newHero")
    public HeroInGame getHero() {
        return new HeroInGame();
    }


    @ModelAttribute("loggedPlayer")
    public Player getPlayer() {
        User loggedUser = getUser();
        return loggedUser.getPlayer() != null ? loggedUser.getPlayer() : Player.builder().statistic(Statistic.createStatistic()).build();
    }

    @ModelAttribute("players")
    public List<Player> getPlayers() {
        Game game = getGame();
        return game != null ? game.getPlayers() : null;
    }

    @ModelAttribute("game")
    public Game getGame() {
        Game loggedGame = getUser().getGame();
        return loggedGame != null ? loggedGame : new Game();
    }

    @ModelAttribute("games")
    public List<Game> getGames() {
        return gameService.getAllGames();
    }

    @ModelAttribute("loggedUser")
    public User getUser() {
        return userService.getLoggedUser();
    }

    @ModelAttribute("chat")
    public Message getChat() {
        return new Message();
    }

    // Obtener todas las partidas.
    @GetMapping
    public String showGames(@RequestParam(defaultValue = "0") int page, ModelMap model, HttpSession session) {
        page = page < 0 ? 0 : page;
        Pageable pageable = PageRequest.of(page, 5);
        List<Game> games = gameService.getAllGames();
        Page<Game> gamePage = gameService.getAllGames(pageable);
        if (!games.isEmpty() && gamePage.isEmpty()) {
            page = games.size() / 5;
            pageable = PageRequest.of(page, 5);
            gamePage = gameService.getAllGames(pageable);
        }
        advise.getMessage(session, model);
        model.put("isNext", gamePage.hasNext());
        model.put("games", gamePage.getContent());
        model.put("page", page);
        return VIEW_GAME_LIST;
    }

    // Parida actual
    @GetMapping("/current")
    public String showCurrentGame(HttpSession session, HttpServletRequest request) {
        Game game = getGame();
        advise.keapUrl(session, request);
        return game == null ? PAGE_GAMES : VIEW_GAME_LOBBY;
    }

    // Visualizar una partida.
    @GetMapping("/view/{gameId}")
    public String showGame(@PathVariable("gameId") int gameId, ModelMap model, HttpSession session, HttpServletRequest request) throws UserInAGameException {
        Game game = gameService.getGameById(gameId);
        // advise.keapUrl(session, request);
        model.put("game", game);
        gameService.addSpectatorToGame(game, getUser());
        return VIEW_GAME_LOBBY;
    }

    /// Unirse a una partida.
    @GetMapping("/{gameId}")
    public String joinGame(@PathVariable("gameId") int gameId, @RequestParam(defaultValue = "null") String password, ModelMap model, HttpSession session, HttpServletRequest request) throws UserInAGameException, IncorrectPasswordException, UserHasAlreadyAPlayerException, FullGameException {
        Game newGame = gameService.getGameById(gameId);
        User loggedUser = getUser();
        userService.addUserToGame(loggedUser, newGame, password);
        gameService.addPlayerToGame(newGame, loggedUser); // Esto estaba antes en un post.
        advise.keapUrl(session, request);
        advise.getMessage(session, model);
        model.put("numHeroes", newGame.isUniClass()); // El jugador todavía no se ha unido, CUIODADO.
        return PAGE_CURRENT_GAME;
    }

    //Elegir here
    @GetMapping(value = "/heroSelect")
    public String initHeroSelectForm(HttpSession session, ModelMap model, HttpServletRequest request) {
        // Los datos para el formulario.
        advise.getMessage(session, model);
        advise.keapUrl(session, request);
        return VIEW_GAME_HERO_SELECT;
    }

    // Analizamos la elección del héroe.
    @PostMapping(value = "/heroSelect")
    public String processHeroSelectForm(HeroInGame heroInGame) throws RoleAlreadyChosenException, HeroAlreadyChosenException, FullGameException, PlayerIsReadyException {
        gameService.addHeroToPlayer(getPlayer(), heroInGame, getGame());
        return PAGE_CURRENT_GAME;

    }

    // Llamamos al formulario para crear la partida.
    @GetMapping(value = "/new")
    public String initCreationForm() throws UserInAGameException {
        if (!getGame().isNew())
            throw new UserInAGameException();
        return VIEW_GAME_CREATE;
    }

    // Comprobamos si la partida es correcta y la almacenamos.
    @PostMapping(value = "/new")
    public String processCreationForm(@Valid Game game, BindingResult result) throws FullGameException {
        if (result.hasErrors()) return VIEW_GAME_CREATE;
        gameService.createGame(userService.getLoggedUser(), game);
        return PAGE_CURRENT_GAME;
    }

    @GetMapping("/selectOrder")
    public String orderPlayers(HttpSession session, HttpServletRequest request) {
        advise.keapUrl(session, request);
        return VIEW_GAME_PREORDER;
    }


    // Tiene que recibir las cartas de habilidad que desea utilizar el jugador, por tanto, se va a modificar entero.
    @PostMapping("/selectOrder")
    public String processOrderPlayers(HttpSession session, HttpServletRequest request) {
        List<Player> players = getPlayers();
        Game game = getGame();
        if (players.stream().anyMatch(player -> player.getSequence() == -1))
            gameService.orderPlayer(players, game);
        advise.keapUrl(session, request);
        return VIEW_GAME_ORDER;
    }

    @GetMapping("deletePlayer/{playerId}")
    public String deletePlayer(@PathVariable("playerId") int playerId) {
        Game game = getGame();
        playerService.deletePlayerById(playerId);
        return PAGE_GAME_LOBBY.replace("{gameId}", game.getId().toString());
    }
}
