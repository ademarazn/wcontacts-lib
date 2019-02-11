package com.ademarazn.wcontacts.adapters;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @author ademarazn
 * @since 02/02/2019
 */
public abstract class WAdapter<T> extends BaseAdapter {

    private List<T> items;

    protected WAdapter(@NonNull List<T> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).hashCode();
    }

    @Override
    public abstract View getView(int i, View view, ViewGroup parent);

    public static class ViewHolder {
        protected View view;

        protected ViewHolder(View view) {
            this.view = view;
        }
    }
}
