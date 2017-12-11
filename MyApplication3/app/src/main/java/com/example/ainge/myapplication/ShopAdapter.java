package com.example.ainge.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ainge.myapplication.R;

import java.util.List;


public class ShopAdapter extends ArrayAdapter<Product>
{
    private int SA_ResourceId;
    public ShopAdapter(Context context, int textViewResourceId,
                       List<Product> objects)
    {
        super(context,textViewResourceId,objects);
        SA_ResourceId = textViewResourceId;
    }
    class ViewHolder
    {
        TextView VH_FirstLetter;
        TextView VH_Name;
        TextView VH_Price;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product products = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null)
        {
            view = LayoutInflater.from(getContext()).inflate(SA_ResourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.VH_FirstLetter = (TextView) view.findViewById(R.id.firstletter);
            viewHolder.VH_Name = (TextView) view.findViewById(R.id.productname);
            viewHolder.VH_Price = (TextView) view.findViewById(R.id.price);
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.VH_FirstLetter.setText(products.getFirstLetter());
        viewHolder.VH_Name.setText(products.getName());
        return view;
    }

}
