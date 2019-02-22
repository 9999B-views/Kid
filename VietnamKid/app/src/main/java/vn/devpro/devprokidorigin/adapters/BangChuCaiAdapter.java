package vn.devpro.devprokidorigin.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.interfaces.ItemClick;
import vn.devpro.devprokidorigin.models.entity.hocchucai.LetterModelSmall;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.Utils;

/**
 * Created by HOANG on 3/6/2018.
 */

public class BangChuCaiAdapter extends BaseAdapter {
    private final int GRIDVIEW_PADDING_ITEM = 20;
    private List<LetterModelSmall> chuCaiList;
    private LayoutInflater inflater;
    private ItemClick itemClick;
    private int itemWidth;
    private int itemHeight;
    private Context mContext;

    public BangChuCaiAdapter(List<LetterModelSmall> chuCaiList
            , ItemClick itemClick, int itemWidth, int itemHeight, Context mContext) {
        this.chuCaiList = chuCaiList;
        inflater = LayoutInflater.from(mContext);
        this.itemClick = itemClick;
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return chuCaiList.size();
    }

    @Override
    public LetterModelSmall getItem(int position) {
        return chuCaiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.chu_cai_item, null);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(itemWidth, itemHeight);
            view.setLayoutParams(params);

            holder = new ViewHolder();
            holder.icon = view.findViewById(R.id.chucai_item);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        LetterModelSmall letterModelSmall = getItem(position);

        byte[] data = Utils.decodeFileImage(Utils.getByteArrayFromSD(Global.pathImages,letterModelSmall.getImage_name(), ".png"));
        if (data != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            //holder.icon.setImageBitmap(bitmap);
            holder.icon.setImageBitmap(Bitmap.createScaledBitmap(bitmap, itemWidth, itemHeight, false));
        } else {
            holder.icon.setImageResource(R.drawable.no_img_available);
        }
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onClickItemGridView(chuCaiList.get(position).getId());
            }
        });
        return view;
    }

    static class ViewHolder {
        ImageView icon;
    }
}
