package vn.devpro.devprokidorigin.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.models.Sound;
import vn.devpro.devprokidorigin.models.entity.TopicItem;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.Utils;


/**
 * Created by Administrator on 3/6/2018.
 */

public class ItemAdapter extends PagerAdapter {

    private ArrayList<TopicItem> topicItems;
    private LayoutInflater inflater;
    private Context context;
    private int width, height, showRatio =0;

    public ItemAdapter(ArrayList<TopicItem> topicItems, int width, int height, int topic_ID, Context context) {
        this.topicItems = topicItems;
        this.context = context;
        this.width = width;
        this.height = height;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return topicItems.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        ImageView imageView = view.findViewById(R.id.fragment_anh);
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) bitmap.recycle();
        }
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View v = inflater.inflate(R.layout.frament_item, container, false);
        assert v != null;
        final ImageView imageView = v.findViewById(R.id.fragment_anh);
        final String soundVN = topicItems.get(position).getSound_vn();
        final String soundEn = topicItems.get(position).getSound_en();
        imageView.setImageBitmap(Utils.displayImageForHocTapActivity(topicItems.get(position).getBitImg(), width, height, false));
        container.addView(v, 0);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showRatio < 4) {
                    showRatio++;
                }
                if (Global.getLanguage() == Global.VN) {
                    Sound.getInstance(context).playEncrytedFile(soundVN);
                } else {
                    Sound.getInstance(context).playEncrytedFile(soundEn);
                }
                Utils.btn_click_animate(view);
                topicItems.get(position).setShow_ratio(showRatio);
            }
        });
        return v;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
