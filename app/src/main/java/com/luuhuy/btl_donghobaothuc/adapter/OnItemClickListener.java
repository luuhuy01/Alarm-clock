package com.luuhuy.btl_donghobaothuc.adapter;

public interface OnItemClickListener {
    void onItemClick(ItemTime itemTime);
    void onItemLongClick(ItemTime itemTime, int position);
    void switchItem(ItemTime itemTime);
}
