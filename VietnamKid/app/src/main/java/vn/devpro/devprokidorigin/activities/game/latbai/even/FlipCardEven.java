package vn.devpro.devprokidorigin.activities.game.latbai.even;



import vn.devpro.devprokidorigin.activities.game.latbai.even.ui.Abstracts;
import vn.devpro.devprokidorigin.activities.game.latbai.even.ui.EvenObsever;

/**
 * Created by admin on 3/30/2018.
 */

public class FlipCardEven extends Abstracts {

    public static final String TYPE = FlipCardEven.class.getName();
    public final int id;

    public FlipCardEven(int id) {
        this.id = id;
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
