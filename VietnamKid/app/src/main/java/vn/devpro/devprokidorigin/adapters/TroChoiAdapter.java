package vn.devpro.devprokidorigin.adapters;

/**
 * Created by hienc on 4/13/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.interfaces.ChooseGameClickListener;
import vn.devpro.devprokidorigin.models.ItemProperty;
import vn.devpro.devprokidorigin.models.TroChoiModel;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.Utils;

/**
 * Created by hienc on 4/11/2018.
 */

public class TroChoiAdapter extends RecyclerView.Adapter<TroChoiAdapter.ViewHolder> {
    private static int ITEM_MARGIN_TOP_BOTTOM = 0;
    private ItemProperty itemProperty;
    public Context mContext;
    private ArrayList<TroChoiModel> mTroChoiModels;
    private ChooseGameClickListener mGameClickListener;


    public TroChoiAdapter(Context mContext, ArrayList<TroChoiModel> mTroChoiModels, ItemProperty itemProperty) {
        this.itemProperty = itemProperty;
        this.mContext = mContext;
        this.mTroChoiModels = mTroChoiModels;

    }

    public void setItemClickListener(ChooseGameClickListener pGameClickListener) {
        this.mGameClickListener = pGameClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View convertView = inflater.inflate(R.layout.item_trochoi, parent, false);
        AbsListView.LayoutParams param = new AbsListView.LayoutParams(itemProperty.width, itemProperty.height);
        convertView.setLayoutParams(param);
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(convertView.getLayoutParams());
        layoutParams.setMargins(itemProperty.left_right_margin, itemProperty.top_bottom_margin, itemProperty.left_right_margin, itemProperty.top_bottom_margin);
        convertView.setLayoutParams(layoutParams);
        convertView.setPadding((int) (itemProperty.width / 10.5f), 0, (int) (itemProperty.width / 10.5f), 0);
        ViewHolder viewHolder = new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TroChoiModel game = mTroChoiModels.get(position);
        if (Global.getLanguage() == Global.VN) {
            holder.tvTroChoi.setText(game.getGameNameVN());
        } else {
            holder.tvTroChoi.setText(game.getGameNameEN());
        }
        if (Global.isDemoApp()) {
            if (position != 0 && position != 1) {
                holder.itemView.setAlpha(0.4f);
                holder.itemView.setClickable(false);
            }
        }
        switch (position) {
            case 0:
                holder.rivItem.setBackgroundResource(R.drawable.retangle2);
                Glide.with(mContext).load(R.drawable.game_mn_pop_balloon).into(holder.rivItem);
                break;
            case 1:
                holder.rivItem.setBackgroundResource(R.drawable.retangle4);
                Glide.with(mContext).load(R.drawable.game_mn_puzzle).into(holder.rivItem);
                break;
            case 2:
                holder.rivItem.setBackgroundResource(R.drawable.retangle);
                Glide.with(mContext).load(R.drawable.game_mn_match_obj).into(holder.rivItem);
                break;
            case 3:
                holder.rivItem.setBackgroundResource(R.drawable.retangle1);
                Glide.with(mContext).load(R.drawable.game_mn_find_shadow).into(holder.rivItem);
                break;
            case 4:
                holder.rivItem.setBackgroundResource(R.drawable.retangle3);
                Glide.with(mContext).load(R.drawable.game_mn_make_pair).into(holder.rivItem);
                break;
            case 5:
                holder.rivItem.setBackgroundResource(R.drawable.retangle5);
                Glide.with(mContext).load(R.drawable.game_mn_write_letter).into(holder.rivItem);
                break;
            default:
                break;
        }
        holder.rivItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                Utils.didTapButtom(pView);
                pView.getAnimation().setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mGameClickListener.onGameMenuItemClick(game.getId());
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTroChoiModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView rivItem;
        TextView tvTroChoi;
        RelativeLayout rltSelect;


        public ViewHolder(View itemView) {
            super(itemView);
            tvTroChoi = itemView.findViewById(R.id.tvTTTro);
            tvTroChoi.setTypeface(Global.Font.Quicksand_Bold);
            rivItem = itemView.findViewById(R.id.rivItem);
        }
    }
}

