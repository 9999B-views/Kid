package vn.devpro.devprokidorigin.activities.game.latbai.even.engine;


import vn.devpro.devprokidorigin.activities.game.latbai.even.ui.Abstracts;
import vn.devpro.devprokidorigin.activities.game.latbai.even.ui.EvenObsever;

/**
 * Created by admin on 3/30/2018.
 */
// khi nút trở lại được nhấn
public class HidePairCardEven extends Abstracts {
     public static final String TYPE = HidePairCardEven.class.getName();
    public int id1;
    public int id2;

    public HidePairCardEven(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
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
