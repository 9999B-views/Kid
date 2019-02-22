package vn.devpro.devprokidorigin.activities.game.latbai.even.engine;


import vn.devpro.devprokidorigin.activities.game.latbai.even.ui.Abstracts;
import vn.devpro.devprokidorigin.activities.game.latbai.even.ui.EvenObsever;

/**
 * Created by admin on 3/30/2018.
 */

public class FlipDownCardEven extends Abstracts {

    public static final String TYPE = FlipDownCardEven.class.getName();
      public FlipDownCardEven()
      {

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
