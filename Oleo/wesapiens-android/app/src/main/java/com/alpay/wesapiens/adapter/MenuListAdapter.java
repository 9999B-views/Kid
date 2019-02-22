package com.alpay.wesapiens.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alpay.wesapiens.GameActivity;
import com.alpay.wesapiens.R;
import com.alpay.wesapiens.models.MenuItem;
import com.alpay.wesapiens.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuViewHolder> {
    private List<MenuItem> mDataset;
    private AppCompatActivity mAppCompatActivity;
    public static class MenuViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextView;
        public LinearLayout mLinearLayout;
        public MenuViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.menu_text);
            mImageView = itemView.findViewById(R.id.menu_icon);
            mLinearLayout = itemView.findViewById(R.id.menu_item_layout);
        }
    }

    public MenuListAdapter(AppCompatActivity appCompatActivity, ArrayList<MenuItem> menuItems) {
        mDataset = menuItems;
        mAppCompatActivity = appCompatActivity;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_holder, parent, false);
        MenuViewHolder menuViewHolder = new MenuViewHolder(view);
        return menuViewHolder;
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position).getTitle());
        holder.mImageView.setImageResource(mDataset.get(position).getImage());
        if(mDataset.get(position).isActive()){
            holder.mTextView.setTextColor(Color.BLACK);
            holder.mImageView.setColorFilter(Color.argb(255, 0, 0, 0));
            holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mAppCompatActivity, GameActivity.class);
                    mAppCompatActivity.startActivity(intent);
                }
            });
        }else{
            holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.showWarningToast(mAppCompatActivity, R.string.ch_select_warning, Toast.LENGTH_SHORT);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
