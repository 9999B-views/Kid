package vn.devpro.devprokidorigin.activities.game.latbai.even;



import vn.devpro.devprokidorigin.activities.game.latbai.even.ui.Abstracts;
import vn.devpro.devprokidorigin.activities.game.latbai.even.ui.EvenObsever;
import vn.devpro.devprokidorigin.activities.game.latbai.themes.Theme;


/**
 * Created by admin on 3/30/2018.
 */

public class Sukien_chon_chude extends Abstracts {
    public static final String TYPE = Sukien_chondokho.class.getName();
    public final Theme theme ;

    public Sukien_chon_chude(Theme theme) {
        this.theme = theme;
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
