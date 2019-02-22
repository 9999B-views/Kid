package vn.devpro.devprokidorigin.activities.game.latbai.even;



import vn.devpro.devprokidorigin.activities.game.latbai.even.ui.Abstracts;
import vn.devpro.devprokidorigin.activities.game.latbai.even.ui.EvenObsever;

/**
 * Created by admin on 3/30/2018.
 */

public class StarNewGame extends Abstracts {
    public static final String TYPE = StarNewGame.class.getName();

    public final int difficulty,topicId ;

    public StarNewGame(int difficulty) {
        this.difficulty = difficulty;
        this.topicId = 3 ;
    }
    public StarNewGame(int difficulty, int topicId) {
        this.difficulty = difficulty;
        this.topicId = topicId ;
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
