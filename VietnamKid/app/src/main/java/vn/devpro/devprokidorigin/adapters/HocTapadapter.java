package vn.devpro.devprokidorigin.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Random;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.databases.DBHelper;
import vn.devpro.devprokidorigin.dialogs.CustomAlertDialog;
import vn.devpro.devprokidorigin.interfaces.ITopicClick;
import vn.devpro.devprokidorigin.models.entity.TopicName;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.Utils;

/**
 * Created by admin on 3/7/2018.
 */

public class HocTapadapter extends BaseAdapter {
    private final int GRIDVIEW_PADDING_ITEM = 20;
    private ArrayList<TopicName> listData;
    private LayoutInflater inflater;
    private ITopicClick itemClick;
    private ImageView btnApp;
    private TextView txtname;
    private int itemWidth, itemHeight;
    private Context mContext;
    private float[] listpercent;
    public boolean database_missing = false;


    public HocTapadapter(ArrayList<TopicName> listData, LayoutInflater inflater, ITopicClick itemClick, int itemW, int itemH, Context pContext) {
        this.itemWidth = itemW;
        this.itemHeight = itemH;
        this.itemClick = itemClick;
        this.listData = listData;
        this.inflater = inflater;
        this.mContext = pContext;
    }


    public int getCount() {
        return listData.size();
    }

    @Override
    public TopicName getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.topic_name_item, null);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(itemWidth, itemHeight);
            convertView.setLayoutParams(params);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        processEvent(holder, position);
        bindData(holder, position);

        return convertView;
    }


    private void bindData(ViewHolder pHolder, int pPosition) {
        if (Global.getLanguage() == Global.VN) {
            pHolder.txtTitle.setText(listData.get(pPosition).getTitle_vn());
        } else {
            pHolder.txtTitle.setText(listData.get(pPosition).getTitle_en());
        }
        if (listData.get(pPosition).getImageData() == null) {

        } else {
            Bitmap icon = Utils.displayImageForHocTapActivity(listData.get(pPosition).getImageData(), itemWidth, itemHeight, true);
            pHolder.icon.setImageBitmap(icon);
        }
        if (listData.get(pPosition).getLock() == 0) {
            pHolder.imgLock.setVisibility(View.INVISIBLE);
        } else {
            pHolder.imgLock.setVisibility(View.VISIBLE);
        }


        float number = 0;
        if (!database_missing) {
            number = listpercent[listData.get(pPosition).getId()-1];
        }
        pHolder.mProgressBar.setProgress(number);
    }

    // FIXME: 3/31/2018 
    private void processEvent(final ViewHolder pHolder, final int pPosition) {
        pHolder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                itemClick.onTopicClick(listData.get(pPosition).getId(), listData.get(pPosition).getLock(),database_missing);
            }
        });
    }


    public void setListData(ArrayList<TopicName> pListData) {
        this.listData = pListData;
    }

    public void setListpercent(float[] listpercent) {
        this.listpercent = listpercent;
    }


    private class ViewHolder {
        RoundedImageView icon;
        TextView txtTitle;
        RoundCornerProgressBar mProgressBar;
        float textSize = itemWidth / 8.4f;
        int pad = (int) (itemWidth * 0.09f);
        ImageView imgLock;

        public ViewHolder(View convertView) {
            imgLock = convertView.findViewById(R.id.imgLock);
            icon = convertView.findViewById(R.id.btnApp);
            icon.setCornerRadius(itemWidth * 0.18f); // radius = 18% width
            txtTitle = convertView.findViewById(R.id.txtname);
            txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            txtTitle.setTypeface(Global.Font.Boosternextfy_Bold);
            mProgressBar = convertView.findViewById(R.id.progressView);
            mProgressBar.setPadding(pad, 0, pad, 0);
        }
    }

    float getProgressValue() {
        float ran = new Random().nextFloat();
        //Log.d("RANDPROGRESS", "--->" + ran);
        return ran;
    }

}