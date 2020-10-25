package com.nicholas.dotamarketplace;

import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int mode;
    // Int mode:
    // 0: All, use recyclerview
    // 1: Bottom only

    public SpacesItemDecoration(int space, int mode) {
        this.space = dpToPx(space);
        this.mode = mode;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        boolean isLast;
        if (mode == 0) {
            isLast = (position == state.getItemCount()-1) || (position == state.getItemCount()-2);
            if (position % 2 == 0) {
                outRect.top = space;
                outRect.left = space;
                outRect.right = space / 2;
            } else {
                outRect.top = space;
                outRect.right = space;
                outRect.left = space / 2;
            }
            if (isLast) {
                outRect.bottom = space;
            }
        } else if (mode == 1) {
            isLast = position == state.getItemCount()-1;
            if (isLast) {
                outRect.bottom = space;
            }
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
