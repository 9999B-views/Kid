package vn.devpro.devprokidorigin.activities.game.latbai.even.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.os.Handler;

/**
 * Created by admin on 3/30/2018.
 */

// các sự kiện từ UI đến engine và chạy ngược lại


public class EventBus {
    private Handler mHandler;
    private static EventBus mInstance = null;
    private final Map<String, List<EvenObsever>> events = Collections.synchronizedMap( new HashMap<String,
            List<EvenObsever>>() );
    private Object obj = new Object();

    private EventBus() {
        mHandler = new Handler();
    }


    public static EventBus getInstance() {
        if (mInstance == null) {
            mInstance = new EventBus();
        }
        return mInstance;
    }

    synchronized public void listen(String eventType, EvenObsever eventObserver) {
        List<EvenObsever> observers = events.get( eventType );
        if (observers == null) {
            observers = Collections.synchronizedList( new ArrayList<EvenObsever>() );
        }
        observers.add( eventObserver );
        events.put( eventType, observers );
    }

    synchronized public void unlisten(String eventType, EvenObsever eventObserver) {
        List<EvenObsever> observers = events.get( eventType );
        if (observers != null) {
            observers.remove( eventObserver );
        }
    }

    public void notify(Event event) {
        synchronized (obj) {
            List<EvenObsever> observers = events.get( event.getType() );
            if (observers != null) {
                for (EvenObsever observer : observers) {
                    Abstracts abstractEvent = (Abstracts) event;
                    abstractEvent.fire( observer );
                }
            }
        }
    }


    public void notifyy(final Event event, long delay) {

        mHandler.postDelayed( new Runnable() {
                                  @Override
                                  public void run() {
                                      EventBus.this.notify( event );
                                  }
                              },
                delay );

    }
}
