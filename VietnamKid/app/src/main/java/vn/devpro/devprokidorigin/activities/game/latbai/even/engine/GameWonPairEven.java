package vn.devpro.devprokidorigin.activities.game.latbai.even.engine;


import vn.devpro.devprokidorigin.activities.game.latbai.even.ui.Abstracts;
import vn.devpro.devprokidorigin.activities.game.latbai.even.ui.EvenObsever;
import vn.devpro.devprokidorigin.models.entity.game.latbai.GameState;

/**
 * Created by admin on 3/30/2018.
 */
// chiến thắng 1 bảng
public  class GameWonPairEven extends Abstracts {

    public static final String TYPE = GameWonPairEven.class.getName();
    public GameState gameState;

    public GameWonPairEven(GameState gameState) {
        this.gameState = gameState;
    }
        @Override
    public String getType() {
        return TYPE;
    }

    @Override
    protected void fire(EvenObsever evenObsever) {
evenObsever.onEvent( this );
    }
}
