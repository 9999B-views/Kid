package vn.devpro.devprokidorigin.activities.game.latbai.even;

import vn.devpro.devprokidorigin.activities.game.latbai.even.ui.Abstracts;
import vn.devpro.devprokidorigin.activities.game.latbai.even.ui.EvenObsever;

public class DataMissing extends Abstracts {
    public static final String TYPE = DataMissing.class.getName();
    @Override
    protected void fire(EvenObsever evenObsever) {
        evenObsever.onEvent(this);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
