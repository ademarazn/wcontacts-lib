package com.ademarazn.wcontacts.adapters.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ademarazn.wcontacts.R;
import com.ademarazn.wcontacts.adapters.WAdapter;

/**
 * @author ademarazn
 * @since 02/02/2019
 */
public class WContactViewHolder extends WAdapter.ViewHolder {
    public TextView tvName;
    public ImageView ivPhoto;
    public TextView tvNumber;

    public WContactViewHolder(View view) {
        super(view);
        tvName = view.findViewById(R.id.name);
        ivPhoto = view.findViewById(R.id.photo);
        tvNumber = view.findViewById(R.id.number);
    }
}
