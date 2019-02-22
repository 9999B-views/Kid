package vn.devpro.devprokidorigin.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.interfaces.ItemClick;

/**
 * Created by hoang-ubuntu on 30/05/2018.
 */

public class LearnWriteAdapter extends BaseAdapter {
    private String[] dsChuCai;
    private LayoutInflater inflater;
    private float itemHeight;

    public LearnWriteAdapter(String[] dsChuCai, LayoutInflater inflater, float itemHeight) {
        this.dsChuCai = dsChuCai;
        this.inflater = inflater;
        this.itemHeight = itemHeight;
    }

    public void setList(String[] list) {
        this.dsChuCai = list;
    }

    @Override
    public int getCount() {
        return dsChuCai.length;
    }

    @Override
    public String getItem(int i) {
        return dsChuCai[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_game_lw, null);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) itemHeight);
            view.setLayoutParams(params);

            holder = new Holder();
            holder.roundedImageView = view.findViewById(R.id.imgItemView);

            holder.textView = view.findViewById(R.id.textGrid);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        String chuCai = getItem(position);
        holder.textView.setText(chuCai);
        holder.textView.setBackgroundResource(R.drawable.bodor_item_write_selector);
        return view;
    }

    static class Holder {
        TextView textView;
        RoundedImageView roundedImageView;
    }
}
