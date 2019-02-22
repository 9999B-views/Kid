package vn.devpro.devprokidorigin.activities.game.dapbongbay.game;

class TranferAnimGame implements AnimGameInitializers {
    private final double mSpeedMin;
    private final double mSpeedMax;

    public TranferAnimGame(double speedMin, double speedMax) {
        mSpeedMin = speedMin;
        mSpeedMax = speedMax;
    }


    @Override
    public void initAnimGame(AnimGame a) {
        double speed = 100;
        a.mSpeedY = speed / 1000d;
    }
}
