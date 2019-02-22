package vn.devpro.devprokidorigin.utils.AdMob;

import android.util.Log;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.utils.Global;

public class AdsObserver {
    public static long lastShow = 0;
    private static String TAG = AdsObserver.class.getSimpleName();

   public static boolean shouldShow(long milis){
       if (Global.isPremium)return false;
       long wait = milis - lastShow;
       Log.i( TAG,"Time"+milis+"....ls:"+lastShow+".....hh:"+wait);
       if(wait >= Global.limit_ads ) // 60000
       {
           Log.i(TAG,"ads can show now-->"+wait);
           return true ;
       }
       else
       {
           Log.e(TAG,"not enough time to show next ads-->"+wait);
           return  false ;
       }
   }

}
