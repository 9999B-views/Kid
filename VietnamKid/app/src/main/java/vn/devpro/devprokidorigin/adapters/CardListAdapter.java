package vn.devpro.devprokidorigin.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.models.Sound;
import vn.devpro.devprokidorigin.models.entity.TopicItem;
import vn.devpro.devprokidorigin.models.interfaces.IonImgClick;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.Utils;

/**
 * Created by Administrator on 3/6/2018.
 */

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    private ArrayList<TopicItem> mTopicItem;
    private Context mContext;
    private IonImgClick ionImgClick;
    private int itemW;

    public CardListAdapter(Activity activity, ArrayList<TopicItem> mTopicItem, int itW, IonImgClick imgClick) {
        this.mTopicItem = mTopicItem;
        this.ionImgClick = imgClick;
        mContext = activity.getApplicationContext();
        this.itemW = itW;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        LinearLayout ln;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgItemView);
            ln = itemView.findViewById(R.id.recyler_item);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemW, LinearLayout.LayoutParams.MATCH_PARENT);
            ln.setLayoutParams(params);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem, null);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int i) {

        final String soundVN = mTopicItem.get(i).getSound_vn();
        final String soundEn = mTopicItem.get(i).getSound_en();
        Glide.with(mContext).load(mTopicItem.get(i).getBitImg())
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .error(R.drawable.no_img_available))
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ionImgClick.onClick(i);
                Utils.didTapButtom(holder.imageView);
                if (Global.getLanguage() == Global.VN) {
                    Sound.getInstance(mContext).playEncrytedFile(soundVN);
                } else {
                    Sound.getInstance(mContext).playEncrytedFile(soundEn);
                }
                int showRatio = mTopicItem.get(i).getShow_ratio();
                showRatio++;
                if (showRatio < 4) {
                    mTopicItem.get(i).setShow_ratio(showRatio);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        int i = mTopicItem.size();
        if (i == 0) {
            return i;
        }
        return mTopicItem.size();
    }

    public void setSelectedItem(int position) {
        notifyDataSetChanged();
    }
}
