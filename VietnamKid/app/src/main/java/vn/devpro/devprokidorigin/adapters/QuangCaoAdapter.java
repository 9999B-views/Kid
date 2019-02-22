package vn.devpro.devprokidorigin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.interfaces.MainFuctionClick;
import vn.devpro.devprokidorigin.models.ItemProperty;
import vn.devpro.devprokidorigin.models.QuangCaoModel;

/**
 * Created by hienc on 5/26/2018.
 */

public class QuangCaoAdapter extends RecyclerView.Adapter<QuangCaoAdapter.ViewHolder> {
    private ItemProperty itemProperty;
    private Context mContext;
    private ArrayList<QuangCaoModel> models;

    public QuangCaoAdapter( Context mContext, ArrayList<QuangCaoModel> models, ItemProperty itemProperty) {
        this.mContext = mContext;
        this.models = models;
        this.itemProperty = itemProperty;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.item_game_quangcao, parent, false);
        /*AbsListView.LayoutParams params = new AbsListView.LayoutParams(itemProperty.width, itemProperty.height);
        view.setLayoutParams(params);
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        marginLayoutParams.setMargins(itemProperty.left_right_margin, itemProperty.top_bottom_margin, itemProperty.left_right_margin, itemProperty.top_bottom_margin);
        view.setLayoutParams(marginLayoutParams);
        view.setPadding((int) (itemProperty.width / 20.5f), 0, (int) (itemProperty.width / 20.5f), 0);*/
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        QuangCaoModel quangCaoModel = models.get(position);
        Glide.with(mContext).load(quangCaoModel.getLinkAnh()).into(holder.imgAnhQC);
        holder.tvTen.setText(quangCaoModel.getTenGame());
        holder.tvMota.setText(quangCaoModel.getMota());
        holder.tvSao.setText(quangCaoModel.getSoSao());
        holder.imgAnhQC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgAnhQC;
        private TextView tvTen;
        private TextView tvMota;
        private TextView tvSao;

        public ViewHolder(View itemView) {
            super(itemView);
            imgAnhQC = itemView.findViewById(R.id.imgAnhQC);
            tvTen = itemView.findViewById(R.id.tvTen);
            tvMota = itemView.findViewById(R.id.tvMota);
            tvSao = itemView.findViewById(R.id.tvSao);
        }
    }
}
