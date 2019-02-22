package vn.devpro.devprokidorigin.activities.game.latbai.even;



import vn.devpro.devprokidorigin.activities.game.latbai.even.ui.Abstracts;
import vn.devpro.devprokidorigin.activities.game.latbai.even.ui.EvenObsever;

/**
 * Created by admin on 3/30/2018.
 */
//  khi trowrnlaji màn hình chính được nhấn

public class Sukien_chondokho extends Abstracts {
    public static final String TYPE = Sukien_chondokho.class.getName();

    public final int difficulty;

    public Sukien_chondokho(int difficulty) {
        this.difficulty = difficulty;
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
