package com.alpay.wesapiens.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alpay.wesapiens.R;
import com.alpay.wesapiens.helper.ItemTouchHelperAdapter;
import com.alpay.wesapiens.helper.ItemTouchHelperViewHolder;
import com.alpay.wesapiens.helper.OnStartDragListener;
import com.alpay.wesapiens.models.Frame;
import com.alpay.wesapiens.models.FrameHelper;
import com.alpay.wesapiens.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FrameListAdapter extends RecyclerView.Adapter<FrameListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private List<Frame> mItems = new ArrayList<>();
    private AppCompatActivity mAppCompatActivity;
    private String fName;
    private String fStartImage;
    private String fEndImage;
    private String fContext;
    private String fTime;
    private String fPlace;
    private String fQuestion;
    private String fAnswer;

    private final OnStartDragListener mDragStartListener;

    public FrameListAdapter(AppCompatActivity appCompatActivity, OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
        mAppCompatActivity = appCompatActivity;
        mItems.addAll(FrameHelper.listAll(appCompatActivity));
        mItems.add(new Frame());
    }

    public void addNewFrame(Frame frame){
        mItems.add(frame);
        notifyDataSetChanged();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_layout, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        if(mItems.get(position).getFrameName() == null){
            fillCreateFrameCard(holder, position);
        }else{
            fillSavedFrameCard(holder, position);
        }

        holder.frameDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItems.remove(position);
                notifyItemRemoved(position);
            }
        });
        holder.frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getActionMasked();
                if (action == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });


    }

    private void fillCreateFrameCard(final ItemViewHolder holder, int position){
        holder.frameCardView.setVisibility(View.VISIBLE);
        holder.frameSavedCardView.setVisibility(View.GONE);

        holder.frameAddStartImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fStartImage = Utils.pickImage(mAppCompatActivity);
            }
        });
        holder.frameAddEndImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fEndImage = Utils.pickImage(mAppCompatActivity);
            }
        });
        holder.frameSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fName = holder.frameName.getText().toString();
                fTime = holder.frameTime.getText().toString();
                fPlace = holder.framePlace.getText().toString();
                fContext = holder.frameContext.getText().toString();
                fQuestion = holder.frameQuestion.getText().toString();
                fAnswer = holder.frameAnswer.getText().toString();
                Frame frame = new Frame(1, fName, fTime, fPlace, fStartImage, fEndImage, fContext, fQuestion, fAnswer);
                addNewFrame(frame);
            }
        });
    }

    private void fillSavedFrameCard(final ItemViewHolder holder, int position){
        holder.frameCardView.setVisibility(View.GONE);
        holder.frameSavedCardView.setVisibility(View.VISIBLE);
        holder.frameSavedName.setText(mItems.get(position).getFrameName());
        holder.frameSavedTime.setText(mItems.get(position).getFrameTime());
        holder.frameSavedPlace.setText(mItems.get(position).getFramePlace());
        holder.frameSavedContext.setText(Utils.fromHtml(mItems.get(position).getFrameContext()));
        holder.frameSavedQuestion.setText(Utils.fromHtml(mItems.get(position).getFrameQuestion()));
        holder.frameSavedAnswer.setText(mItems.get(position).getFrameAnswer());
        holder.frameSavedStartImage.setImageDrawable(Utils.getDrawableWithName(mAppCompatActivity, mItems.get(position).getFrameStartImage()));
        holder.frameSavedEndImage.setImageDrawable(Utils.getDrawableWithName(mAppCompatActivity, mItems.get(position).getFrameEndImage()));
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {


        public final RelativeLayout frameLayout;
        public final CardView frameCardView;
        public final FloatingActionButton frameDeleteButton;
        public final ImageView frameAddStartImageButton;
        public final ImageView frameAddEndImageButton;
        public final EditText frameName;
        public final EditText frameTime;
        public final EditText framePlace;
        public final EditText frameContext;
        public final EditText frameQuestion;
        public final EditText frameAnswer;
        public final Button frameSaveButton;

        public final CardView frameSavedCardView;
        public final ImageView frameSavedStartImage;
        public final ImageView frameSavedEndImage;
        public final TextView frameSavedName;
        public final TextView frameSavedTime;
        public final TextView frameSavedPlace;
        public final TextView frameSavedContext;
        public final TextView frameSavedQuestion;
        public final TextView frameSavedAnswer;


        public ItemViewHolder(View itemView) {
            super(itemView);
            frameLayout = itemView.findViewById(R.id.frame_layout);
            frameCardView = itemView.findViewById(R.id.frame_card);
            frameDeleteButton = itemView.findViewById(R.id.frame_delete);
            frameAddEndImageButton = itemView.findViewById(R.id.frame_add_start_image);
            frameAddStartImageButton = itemView.findViewById(R.id.frame_add_start_image);
            frameName = itemView.findViewById(R.id.frame_name);
            frameTime = itemView.findViewById(R.id.frame_time);
            framePlace = itemView.findViewById(R.id.frame_place);
            frameContext = itemView.findViewById(R.id.frame_context);
            frameQuestion = itemView.findViewById(R.id.frame_question);
            frameAnswer = itemView.findViewById(R.id.frame_answer);
            frameSaveButton = itemView.findViewById(R.id.frame_save);

            frameSavedCardView = itemView.findViewById(R.id.frame_saved_card);
            frameSavedEndImage = itemView.findViewById(R.id.frame_saved_end_image);
            frameSavedStartImage = itemView.findViewById(R.id.frame_saved_start_image);
            frameSavedName = itemView.findViewById(R.id.frame_saved_name);
            frameSavedTime = itemView.findViewById(R.id.frame_saved_time);
            frameSavedPlace = itemView.findViewById(R.id.frame_saved_place);
            frameSavedContext = itemView.findViewById(R.id.frame_saved_context);
            frameSavedQuestion = itemView.findViewById(R.id.frame_saved_question);
            frameSavedAnswer = itemView.findViewById(R.id.frame_saved_answer);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
