package vn.devpro.devprokidorigin.activities.game.latbai.fragments;




import  android.support.v4.app.Fragment;

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
import vn.devpro.devprokidorigin.activities.game.latbai.even.ui.EvenObsever;

/**
 * Created by admin on 3/31/2018.
 */

public class BaseFragment extends Fragment implements EvenObsever {
    @Override
    public void onEvent(FlipCardEven event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEvent(Sukien_chondokho event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEvent(HidePairCardEven event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEvent(FlipDownCardEven event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEvent(StarNewGame event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEvent(Sukien_chon_chude event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEvent(GameWonPairEven event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEvent(BackGame event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEvent(NextGame event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEvent(ChangeBackground event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEvent(DataMissing event) {
        throw new UnsupportedOperationException();
    }
}
