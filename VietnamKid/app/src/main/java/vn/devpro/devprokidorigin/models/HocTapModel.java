package vn.devpro.devprokidorigin.models;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import vn.devpro.devprokidorigin.databases.DBHelper;
import vn.devpro.devprokidorigin.databases.SQLiteDataController;
import vn.devpro.devprokidorigin.interfaces.LoadHocTapDataListenner;
import vn.devpro.devprokidorigin.models.entity.TopicName;

/**
 * Created by admin on 3/26/2018.
 */

public class HocTapModel {
    private LoadHocTapDataListenner listenner ;
    private Context context ;
    private DBHelper dbHelper ;
    private int[] arrIndex = {1,10,2,11,4,12,3,13,14,5,8,6,9,17,18,15,16,7};

    public HocTapModel(LoadHocTapDataListenner listenner,Context context){
        this.listenner = listenner;
        this.context = context;
    }

    public  void loadData()
    {
        if(context == null){
            Log.e( "HocTapModel","context null" );
            return;
        }

        if(dbHelper==null)
        {
            dbHelper = DBHelper.getInstance( context ) ;
        }
        ArrayList<TopicName> listData = dbHelper.listTopic();
        if (listData.size() == 0){
            return ;
        }
        else if(listData.size() > 0) {
            Log.i( "DataModel:", "listData size: " + listData.size() );
            reoderList(listData,arrIndex);
            //showList(listData);
            listenner.onLoadTopicNameSuccess(listData);
        }
        else {
            listenner.onLoadTopicNameFailure( "Hoctapactivity: Error listData has no item" );
        }
    }

    private void reoderList(ArrayList<TopicName> pList, int[] pIndice){
        int listS = pList.size();
        int pInS = pIndice.length;
        if(pInS < listS){
            Log.e("REORDER","reorder error: list size:"+listS+" bigger index size:"+pInS);
            return;
        }
        for (int i = 0; i < listS; i++) {
            pList.get(i).setIndex(pIndice[i]);
        }
        // sort
        Collections.sort(pList,new TopicNameComparator());
        Log.i("REORDER", "reorder list done!");
        showList(pList);
    }

    private class TopicNameComparator implements Comparator<TopicName>{
        @Override
        public int compare(TopicName pT1, TopicName pT2) {
            if(pT1.getIndex() == pT2.getIndex()){
                return 0;
            }else if(pT1.getIndex() > pT2.getIndex()) {
                return 1;
            }else {
                return -1;
            }
        }
    }

    private void showList(ArrayList<TopicName> pList){
        for (TopicName t : pList) {
            Log.i("LIST","index:"+t.getIndex()+"--id:"+t.getId());
        }
    }
}
