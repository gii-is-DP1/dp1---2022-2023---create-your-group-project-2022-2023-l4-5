package org.springframework.samples.nt4h.game;


import com.github.cliftonlabs.json_simple.JsonObject;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.nt4h.card.hero.Hero;
import org.springframework.samples.nt4h.card.hero.HeroInGame;
import org.springframework.samples.nt4h.card.hero.HeroService;
import org.springframework.samples.nt4h.game.exceptions.FullGameException;
import org.springframework.samples.nt4h.game.exceptions.HeroAlreadyChosenException;
import org.springframework.samples.nt4h.game.exceptions.PlayerInOtherGameException;
import org.springframework.samples.nt4h.player.Player;
import org.springframework.samples.nt4h.player.PlayerService;
import org.springframework.samples.nt4h.player.exceptions.RoleAlreadyChosenException;
import org.springframework.samples.nt4h.user.User;
import org.springframework.samples.nt4h.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/games")
public class GameController {

    // Constantes.
    private static final String VIEW_GAME_CREATE = "games/createGame";
    private static final String VIEW_GAME_LIST = "games/gamesList";
    private static final String VIEW_GAME_LOBBY = "games/gameLobby";
    private static final String PAGE_GAME_LOBBY = "redirect:/games/{gameId}";
    private static final String VIEW_GAME_HERO_SELECT = "games/heroSelect";
    private static final String PAGE_GAME_HERO_SELECT = "redirect:/games/{gameId}/{playerId}";
    private static final String PAGE_GAMES = "redirect:/games";

    // Servicios
    private final GameService gameService;

    private final HeroService heroService;

    private final PlayerService playerService;
    private final UserService userService;

    @Autowired
    public GameController(GameService gameService, HeroService heroService, PlayerService playerService, UserService userService) {
        this.gameService = gameService;
        this.heroService = heroService;
        this.playerService= playerService;
        this.userService = userService;
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

    @ModelAttribute("hero")
    public HeroInGame getHero() {
        return new HeroInGame();
    }

    // Obtener todas las partidas.
    @GetMapping
    public String getGames(ModelMap model) {
        model.put("selections", gameService.getAllGames());
        return VIEW_GAME_LIST;
    }

    /// Unirse a una partida.
    @GetMapping("/{gameId}")
    public String joinGame(@PathVariable("gameId") int gameId, ModelMap model) {
        Game game = gameService.getGameById(gameId);
        User user = userService.currentUser();
        System.out.println("User: " + user);
        if (gameService.getAllGames().stream().filter(g -> g.getId() != gameId)
            .anyMatch(g -> g.getPlayers().stream().map(Player::getName).anyMatch(n -> n.equals(user.getUsername())))) {
            Game currentGame = gameService.getAllGames().stream().filter(g -> g.getPlayers().stream().anyMatch(p -> p.getName().equals(user.getUsername()))).findFirst().get();
            model.put("message", "Ya estás en una partida y esa es  " + currentGame.getName() + ".");
            model.put("messageType", "danger");
            return getGames(model);
        }
        Optional<Player> player = game.getPlayers().stream().filter(p -> p.getName().equals(user.getUsername())).findFirst();
        model.put("player", player.orElseGet(Player::new));
        model.put("selections", game.getPlayers());
        model.put("numHeroes", game.getMode() == Mode.UNI_CLASS ? 1 : 2);
        return VIEW_GAME_LOBBY;
    }

    // Crear un jugador y vincularlo con la partida.
    @PostMapping(value = "/{gameId}")
    public String processCreationPlayerReady(@Valid Player player, @PathVariable Integer gameId, ModelMap model) throws RoleAlreadyChosenException, HeroAlreadyChosenException {
        // Obtenemos los datos.
        User user = userService.currentUser();
        Game game = gameService.getGameById(gameId);
        Optional<Player> oldPlayer = game.getPlayers().stream().filter(p -> p.getName().equals(user.getUsername())).findFirst();
        // Comprobamos si el usuario ya se había unido a la partida.
        if (oldPlayer.isPresent()) return PAGE_GAME_HERO_SELECT
            .replace("{gameId}", gameId.toString())
            .replace("{playerId}", oldPlayer.get().getId().toString());
        // Si la partida está llena se le
        if (game.getPlayers().size() + 1 > game.getMaxPlayers())
            return PAGE_GAMES;
        // Creamos el jugador si el usuario no se había unido a la partida.
        player.setName(user.getUsername());
        player.setHost(false);
        game.addPlayer(player);
        player.setGame(game);
        playerService.savePlayer(player);
        // Si la partida está llena, no se le permitirá entrar.
        try {
            gameService.saveGame(game);
        } catch (FullGameException e) {
            playerService.deletePlayer(player);
            model.put("message", "La partida está llena.");
            model.put("messageType", "danger");
            return PAGE_GAMES;
        } catch (PlayerInOtherGameException e) {
            Game currentGame = gameService.getAllGames().stream().filter(g -> g.getPlayers().stream().anyMatch(p -> p.getName().equals(user.getUsername()))).findFirst().get();
            model.put("message", "Has sido redirigido a otra partida.");
            model.put("messageType", "danger");
            return PAGE_GAMES;
        }
        return PAGE_GAME_HERO_SELECT.replace("{gameId}", gameId.toString()).replace("{playerId}", player.getId().toString());
    }

    //Elegir heroe
    @GetMapping(value = "/{gameId}/{playerId}")
    public String initHeroSelectForm(@PathVariable Integer gameId, @PathVariable Integer playerId, ModelMap model) {
        // Los datos para el formulario.
        model.put("game", gameService.getGameById(gameId));
        model.put("player", playerService.getPlayerById(playerId));
        model.put("hero", new HeroInGame());
        return VIEW_GAME_HERO_SELECT;
    }

    // Analizamos la elección del héroe.
    @PostMapping(value = "/{gameId}/{playerId}")
    public String processHeroSelectForm(HeroInGame heroInGame, ModelMap model, @PathVariable Integer gameId, @PathVariable Integer playerId, BindingResult result) throws FullGameException, PlayerInOtherGameException {
        // Obtenemos los datos.
        Hero hero = heroService.getHeroById(heroInGame.getHero().getId());
        Game game = gameService.getGameById(gameId);
        Player player = playerService.getPlayerById(playerId);
        // Vinculamos el héroe al jugador.
        heroInGame.setActualHealth(hero.getHealth());
        heroInGame.setPlayer(player);
        player.addHero(heroInGame);
        if (player.getHeroes().size() == game.getMode().getNumHeroes()) player.setReady(true);
        // Si el héroe ya ha sido elegido o ya tenía uno de ese rol, se le impedirá elegirlo.
        try {
            playerService.savePlayer(player);
        } catch (RoleAlreadyChosenException e) {
            model.put("message", "Role already chosen.");
            model.put("messageType", "danger");
            return initHeroSelectForm(gameId, playerId, model);
        }
        try {
            gameService.saveGame(game);
        } catch (HeroAlreadyChosenException e) {
            model.put("message", "Hero already chosen");
            model.put("messageType", "danger");
            return initHeroSelectForm(gameId, playerId, model);
        }
        return PAGE_GAME_LOBBY.replace("{gameId}", gameId.toString());

    }

    // Llamamos al formulario para crear la partida.
    @GetMapping(value = "/new")
    public String initCreationForm(ModelMap model) {
        // Comprobamos si está en otras partidas.
        User user = userService.currentUser();
        if (gameService.getAllGames().stream().anyMatch(g -> g.getPlayers().stream().map(Player::getName).anyMatch(n -> n.equals(user.getUsername())))) {
            Game currentGame = gameService.getAllGames().stream().filter(g -> g.getPlayers().stream().anyMatch(p -> p.getName().equals(user.getUsername()))).findFirst().get();
            model.put("message", "Ya estás en una partida y esa es  " + currentGame.getName() + ".");
            model.put("messageType", "danger");
            return getGames(model);
        }
        model.put("game", new Game());
        return VIEW_GAME_CREATE;
    }


    // Comprobamos si la partida es correcta y la almacenamos.
    @PostMapping(value = "/new")
    public String processCreationForm(@Valid Game game, BindingResult result, ModelMap model) throws HeroAlreadyChosenException, FullGameException, PlayerInOtherGameException {
        User user = userService.currentUser();
        if (result.hasErrors()) return VIEW_GAME_CREATE;
        // Añadir anfitrión.
        Player player = new Player();
        player.setHost(true);
        player.setName(user.getUsername());
        game.addPlayer(player);
        gameService.saveGame(game);
        return PAGE_GAME_LOBBY.replace("{gameId}", game.getId().toString());
    }

    // Acrualizar los jugadores en el lobby.
    @GetMapping("/update/{gameId}")
    public ResponseEntity<String> updateMessages(@PathVariable Integer gameId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("messages",
            gameService.getGameById(gameId)
                .getPlayers().stream().map(player -> player.getName() + " { " +
                    player.getHeroes().stream().map(hero -> hero.getHero().getName()).reduce((s, s2) -> s + ", " + s2)
                        .orElse("No hero selected") + " }" + " " + (player.getReady() ? "Ready": "Not ready"))
                .collect(Collectors.toList()));
        return new ResponseEntity<>(jsonObject.toJson(), HttpStatus.OK);
    }
}
