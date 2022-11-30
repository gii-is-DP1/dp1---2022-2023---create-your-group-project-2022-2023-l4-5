package org.springframework.samples.nt4h.action;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.nt4h.card.ability.AbilityInGame;
import org.springframework.samples.nt4h.card.product.ProductInGame;
import org.springframework.samples.nt4h.card.product.ProductService;
import org.springframework.samples.nt4h.card.product.StateProduct;
import org.springframework.samples.nt4h.player.Player;
import org.springframework.samples.nt4h.turn.exceptions.NoMoneyException;

public class BuyProduct implements Action {

    private Player player;
    private ProductInGame productInGame;

    @Autowired
    private ProductService productService;

    public BuyProduct(Player player, ProductInGame productInGame) {
        this.player = player;
        this.productInGame = productInGame;
    }


    @Override
    public void executeAction() throws NoMoneyException {
        if (player.getGold() < productInGame.getProduct().getPrice())
            throw new NoMoneyException();
        else if (productInGame.getStateProduct() == StateProduct.INSALE) {
            AbilityInGame abilityInGame = AbilityInGame.builder().timesUsed(0).attack(productInGame.getProduct().getAttack()).isProduct(true).build();
            player.addAbilityInDeck(abilityInGame);
            productInGame.setStateProduct(StateProduct.PLAYER);
            productInGame.setPlayer(player);
            productService.saveProductInGame(productInGame);
        }
    }
}
