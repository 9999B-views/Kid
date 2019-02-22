package com.alpay.wesapiens.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alpay.wesapiens.R;
import com.alpay.wesapiens.adapter.FrameListAdapter;
import com.alpay.wesapiens.helper.OnStartDragListener;
import com.alpay.wesapiens.helper.SimpleItemTouchHelperCallback;
import com.alpay.wesapiens.models.Frame;
import com.alpay.wesapiens.models.FrameHelper;
import com.alpay.wesapiens.utils.Utils;

import java.io.FileNotFoundException;
import java.io.InputStream;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CreateGameFragment extends Fragment implements OnStartDragListener {

    public View view;
    private Unbinder unbinder;
    private ItemTouchHelper mItemTouchHelper;
    private RecyclerView recyclerView;
    private FrameListAdapter frameListAdapter;

    public static CreateGameFragment newInstance(){
        return new CreateGameFragment();
    }

    public CreateGameFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.add_new_frame_button)
    public void addNewFrame(){
        frameListAdapter.addNewFrame(new Frame());
        recyclerView.scrollToPosition(FrameHelper.getFrameListSize() - 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_storyboard, container, false);
        unbinder = ButterKnife.bind(this, view);
        try {
            FrameHelper.readFrameList(getContext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Drawable drawable;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Utils.showErrorToast((AppCompatActivity)getActivity(), R.string.error, Toast.LENGTH_SHORT);
                return;
            }
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                drawable = Drawable.createFromStream(inputStream, null);
                Utils.saveImageDrawable(drawable);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        frameListAdapter = new FrameListAdapter((AppCompatActivity) getActivity(), this);
        recyclerView = (RecyclerView) view.findViewById(R.id.create_game_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(frameListAdapter);

        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(frameListAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.scrollToPosition(FrameHelper.getFrameListSize() - 1);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FrameHelper.saveFrameList(getContext());
        unbinder.unbind();
    }
}
