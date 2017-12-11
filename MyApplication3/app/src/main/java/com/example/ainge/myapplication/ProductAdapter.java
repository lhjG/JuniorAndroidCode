package com.example.ainge.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ainge.myapplication.R;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>
{
    private List<Product> A_ProductList;
    private Context A_Context;
    private LayoutInflater A_Inflater;
    private OnItemClickListener A_ItemClickListener = null;

    public ProductAdapter(Context context,List<Product> productsList)
    {
        this.A_Context = context;
        this.A_ProductList = productsList;
        A_Inflater = LayoutInflater.from(A_Context);  //获取布局实例
    }


    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView VH_FirtLetter;
        TextView VH_Name;
        public ViewHolder(View view)
        {
            super(view);//  继承view中的图标等其他显示内容
            VH_FirtLetter = (TextView) view.findViewById(R.id.firstletter);///这两个text是必须显示的
            VH_Name = (TextView) view.findViewById(R.id.productname);
        }
    }
//    public class ViewHolder extends RecyclerView.ViewHolder
//    {
//        private SparseArray<View> mViews;
//        private View mConvertView;
//
//        public ViewHolder(Context context, View itemView, ViewGroup parent)
//        {
//            super(itemView)
//            mConvertView = itemView;
//            mViews = new SparseArray<View>0;
//        }
//        public static ViewHolder get(Context context, ViewGroup parent, int layoutId)
//        {
//            View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
//            ViewHolder holder = new ViewHolder(context, itemView, parent);
//            return holder;
//        }
//        public<T extends View> T getView(int viewId)
//        {
//            View view = mViews.get(viewId);
//            if (view==null)
//            {
//                view = mConvertView.findViewById(viewId);
//                mViews.put(viewId,view);
//            }
//            return (T) view;
//        }
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = A_Inflater.inflate(R.layout.product,parent,false);  //填充
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewholder, final int position)
    {
        Product product = A_ProductList.get(position);
        viewholder.VH_FirtLetter.setText(product.getFirstLetter());
        viewholder.VH_Name.setText(product.getName());

        if(A_ItemClickListener != null)
        {
            viewholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    A_ItemClickListener.onItemClick(position);
                }
            });
            viewholder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    A_ItemClickListener.onItemLongClick(position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return A_ProductList.size();
    }


    public interface OnItemClickListener
    {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onitemclicklistener){
        this.A_ItemClickListener = onitemclicklistener;
    }

}

