package vn.devpro.devprokidorigin.activities.game.latbai.even.ui;


import vn.devpro.devprokidorigin.activities.game.latbai.even.BackGame;
import vn.devpro.devprokidorigin.activities.game.latbai.even.ChangeBackground;
import vn.devpro.devprokidorigin.activities.game.latbai.even.DataMissing;
import vn.devpro.devprokidorigin.activities.game.latbai.even.FlipCardEven;
import vn.devpro.devprokidorigin.activities.game.latbai.even.NextGame;
import vn.devpro.devprokidorigin.activities.game.latbai.even.StarNewGame;
import vn.devpro.devprokidorigin.activities.game.latbai.even.Sukien_chon_chude;
import vn.devpro.devprokidorigin.activities.game.latbai.even.Sukien_chondokho;
import vn.devpro.devprokidorigin.activities.game.latbai.even.engine.FlipDownCardEven;
import vn.devpro.devprokidorigin.activities.game.latbai.even.engine.GameWonPairEven;
import vn.devpro.devprokidorigin.activities.game.latbai.even.engine.HidePairCardEven;

/**
 * Created by admin on 3/30/2018.
 */

public interface EvenObsever {
    void onEvent (FlipCardEven event);

    void onEvent(Sukien_chondokho event);

    void onEvent(HidePairCardEven event);

    void onEvent(FlipDownCardEven event);

    void onEvent(StarNewGame event);

    void onEvent(Sukien_chon_chude event);

    void onEvent(GameWonPairEven event);

    void onEvent(BackGame event);

     //void onEvent(Sukien_chondokho event);
    void onEvent(NextGame event);

    void onEvent(ChangeBackground event);

    void onEvent(DataMissing event);

}
