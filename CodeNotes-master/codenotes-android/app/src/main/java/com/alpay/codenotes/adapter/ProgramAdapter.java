package com.alpay.codenotes.adapter;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.base.BaseActivity;
import com.alpay.codenotes.models.Program;
import com.alpay.codenotes.models.ProgramHelper;
import com.alpay.codenotes.utils.NavigationManager;

import java.util.ArrayList;
import java.util.List;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ItemViewHolder> {

    private List<Program> mItems = new ArrayList<>();
    private AppCompatActivity mAppCompatActivity;

    public ProgramAdapter(AppCompatActivity appCompatActivity) {
        mAppCompatActivity = appCompatActivity;
        mItems.addAll(ProgramHelper.listAll(appCompatActivity));
        mItems.add(new Program());
    }

    public void addNewProgram(Program program){
        mItems.add(program);
        ProgramHelper.addNewProgram(program);
        notifyDataSetChanged();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.program_layout, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        holder.programName.setText(mItems.get(position).getProgramName());
        holder.programImage.setImageBitmap(
                mItems.get(position).getThumbnailImage(mAppCompatActivity,
                mItems.get(position).getProgramImages()[0]));
        holder.programDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItems.remove(position);
                notifyItemRemoved(position);
            }
        });
        holder.programDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.setProgramText(mItems.get(position).getProgramText());
                NavigationManager.openFragment(mAppCompatActivity, NavigationManager.PROGRAM_DETAILS);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public final RelativeLayout programLayout;
        public final ImageView programImage;
        public final TextView programName;
        public final Button programDetails;
        public final FloatingActionButton programDeleteButton;

        public ItemViewHolder(View itemView) {
            super(itemView);
            programLayout = itemView.findViewById(R.id.program_layout);
            programName = itemView.findViewById(R.id.program_name);
            programImage = itemView.findViewById(R.id.program_image);
            programDetails = itemView.findViewById(R.id.program_detail);
            programDeleteButton = itemView.findViewById(R.id.program_delete);

        }
    }
}