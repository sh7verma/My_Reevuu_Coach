package com.myreevuuCoach.interfaces;

/**
 * Created by dev on 8/3/19.
 */

public interface VideoAdapterItemClick {
    void onItemClick(int position);

    void onItemLikeClick(int position);

    void onItemCommentClick(int position);

    void onItemShareClick(int position);
}
