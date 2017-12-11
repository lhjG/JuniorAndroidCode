package com.example.ainge.myapplication;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ainge.myapplication.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;
public class MainActivity extends AppCompatActivity {

    private List<Product> Products_List = new ArrayList<>();
    private RecyclerView Recycler_View;//获取布局实例
    private ProductAdapter Product_Adapter;

    private List<Product> Shop_List = new ArrayList<>();
    private ShopAdapter Shop_Adapter;
    private ListView List_View;

    private FloatingActionButton Shopping_Car;
    private int Flag = 0;
    private String Name = "name";
    private String Tag="test";

    private String [] Keywords = {"*","E","A","D","K","W","M","F","M","L","B"};
    private String [] Names = {"购物车","Enchated Forest","Arla Milk","Devondale Milk",
            "Kindle Oasis","waitrose 早餐麦片","Mcvitie's 饼干","Ferreo Rocher",
            "Maltesers","Lindt","Borggreve"};
    private int Images [] ={R.drawable.enchatedforest,R.drawable.arla,R.drawable.devondale,
            R.drawable.kindle,R.drawable.waitrose, R.drawable.mcvitie,R.drawable.ferrero,R.drawable.maltesers,
            R.drawable.lindt,R.drawable.borggreve};
    private String Prices [] = {"¥5.00","¥59.00","¥79.00","¥2399.00","¥179.00", "¥14.90",
            "¥132.59","¥141.43","¥139.43","¥28.90"};
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //产生通知
        setContentView(R.layout.activity_main);


        //Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
        Random random=new Random();
        int random_result = random.nextInt(10)%10+1;
        Intent intent = new Intent();
        intent.setAction("MyBroadcast");
        intent.putExtra("StartApp",random_result);
        sendBroadcast(intent);
        EventBus.getDefault().register(this);

        //通知widget
        Bundle bundle = new Bundle();
        bundle.putInt("widget_image",Images[random_result-1]);
        bundle.putString("widget_name",Names[random_result]);
        bundle.putString("widget_price",Prices[random_result]);
        Intent towidget_intent=new Intent();
        towidget_intent.setAction("WidgetBroadcast");
        towidget_intent.putExtras(bundle);
        sendBroadcast(towidget_intent);

        initProducts();//初始化产品
        initShopcar();//初始化购物车

        //RecyclerView（商品列表）
        Recycler_View = (RecyclerView) findViewById(R.id.ProductList);
        Product_Adapter = new ProductAdapter(MainActivity.this,Products_List);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        Recycler_View.setLayoutManager(layoutManager); //通过设置布局管理器来设置垂直布局
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(Product_Adapter); //开场动画
        animationAdapter.setDuration(1000);
        Recycler_View.setAdapter(animationAdapter);
        Recycler_View.setItemAnimator(new OvershootInLeftAnimator());

        //ListView（购物车列表）
        Shop_Adapter = new ShopAdapter(MainActivity.this, R.layout.product,Shop_List);
        List_View = (ListView) findViewById(R.id.ShoppingList);
        List_View.setAdapter(Shop_Adapter);
        List_View.setVisibility(View.INVISIBLE);

        //floatingactinbutton（购物车图标）
        Shopping_Car = (FloatingActionButton) findViewById(R.id.ShoppingCar);

        //商品列表点击事件
        Product_Adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener()
        {
            @Override
            //短按进入更多信息界面查看商品详细信息
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this,ShoppingActivity.class);//属于class的ShoppingActivity
                final Product product = Products_List.get(position);
                intent.putExtra(Name,product.getName());
                startActivityForResult(intent,1);
            }

            @Override
            //长按删除商品
            public void onItemLongClick(int position) {
                final Product product = Products_List.get(position);
                Toast.makeText(MainActivity.this,"移除第"+(position+1)+"个商品",Toast.LENGTH_SHORT).show();
                Products_List.remove(product);
                Product_Adapter.notifyDataSetChanged();
            }
        });

        //购物车图标点击事件
        Shopping_Car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    if(Flag == 0)//在商品列表中点击购物车
                    {
                        Flag = 1;
//                        ListView shopping_list = new ListView();
//                        listView.setAdapter(shopAdapter);
                        //进入购物车列表界面
                        List_View.setVisibility(View.VISIBLE);
                        Recycler_View.setVisibility(View.INVISIBLE);
                        Shopping_Car.setImageResource(R.drawable.mainpage);//更换图标

                        //短按商品跳转至商品详情界面
                        List_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Product product = Shop_List.get(position);
                                if(position>=1)//点击的是商品而不是标题
                                {
                                    final Product products = Shop_List.get(position);
                                    Intent intent = new Intent(MainActivity.this,ShoppingActivity.class);
                                    intent.putExtra("name",products.getName());
                                    startActivityForResult(intent,1);//开始跳转activity
                                }
                            }
                        });

                        //长按弹出对话框选择是否删除商品
                        List_View.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                final Product product = Shop_List.get(position);
                                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(MainActivity.this);
                                deleteDialog.setTitle("移除商品");
                                deleteDialog.setMessage("是否从购物车移除"+product.getName());
                                if(position>=1)
                                {
                                    deleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(MainActivity.this,"成功移除商品"+product.getName(),Toast.LENGTH_SHORT).show();
                                            Shop_List.remove(product);
                                            Shop_Adapter.notifyDataSetChanged();
                                        }
                                    });

                                    deleteDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(MainActivity.this,"点击了取消",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    deleteDialog.show();
                                }
                                return true;
                            }
                        });
                    }
                    else//在购物车界面点击购物车图标
                    {
                        Flag = 0;
                        //回到商品列表界面
                        List_View.setVisibility(View.INVISIBLE);
                        Recycler_View.setVisibility(View.VISIBLE);
                        Shopping_Car.setImageResource(R.drawable.shoplist);
                        List_View.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                final Product product = Shop_List.get(position);
                                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(MainActivity.this);
                                deleteDialog.setTitle("移除商品");
                                deleteDialog.setMessage("是否从购物车移除"+product.getName());
                                if(position>=1)
                                {
                                    deleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(MainActivity.this,"成功移除商品"+product.getName(),Toast.LENGTH_SHORT).show();
                                            Shop_List.remove(product);
                                            Shop_Adapter.notifyDataSetChanged();
                                        }
                                    });

                                    deleteDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(MainActivity.this,"点击了取消",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    deleteDialog.show();
                                }
                                return true;
                            }
                        });
                    }
                }
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReceiveEventBusMessage(AddToShoppingList event) {
        int MessageInt = event.getIndexDelivered();
        Product product = new Product(Keywords[MessageInt],Names[MessageInt]);
        Shop_List.add(product);
        Shop_Adapter.notifyDataSetChanged();
        List_View.setVisibility(View.VISIBLE);
        Recycler_View.setVisibility(View.INVISIBLE);
        Shopping_Car.setImageResource(R.drawable.mainpage);
        List_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = Shop_List.get(position);
                if(position>=1)//点击的是商品而不是标题
                {
                    final Product products = Shop_List.get(position);
                    Intent intent = new Intent(MainActivity.this,ShoppingActivity.class);
                    intent.putExtra("name",products.getName());
                    startActivityForResult(intent,1);//开始跳转activity
                }
            }
        });
        List_View.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                final Product product = Shop_List.get(position);
                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(MainActivity.this);
                deleteDialog.setTitle("移除商品");
                deleteDialog.setMessage("是否从购物车移除"+product.getName());
                if(position>=1)
                {
                    deleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this,"成功移除商品"+product.getName(),Toast.LENGTH_SHORT).show();
                            Shop_List.remove(product);
                            Shop_Adapter.notifyDataSetChanged();
                        }
                    });

                    deleteDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this,"点击了取消",Toast.LENGTH_SHORT).show();
                        }
                    });
                    deleteDialog.show();
                }
                return true;
            }
        });
        Shopping_Car.setOnClickListener(new View.OnClickListener() {
            int Flag1 = 1;
            @Override
            public void onClick(View view) {
                {
                    if(Flag1 == 0)//在商品列表中点击购物车
                    {
                        Flag1 = 1;
//                        ListView shopping_list = new ListView();
//                        listView.setAdapter(shopAdapter);
                        //进入购物车列表界面
                        List_View.setVisibility(View.VISIBLE);
                        Recycler_View.setVisibility(View.INVISIBLE);
                        Shopping_Car.setImageResource(R.drawable.mainpage);//更换图标

                        //短按商品跳转至商品详情界面
                        List_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Product product = Shop_List.get(position);
                                if(position>=1)//点击的是商品而不是标题
                                {
                                    final Product products = Shop_List.get(position);
                                    Intent intent = new Intent(MainActivity.this,ShoppingActivity.class);
                                    intent.putExtra("name",products.getName());
                                    startActivityForResult(intent,1);//开始跳转activity
                                }
                            }
                        });
                        //长按弹出对话框选择是否删除商品
                        List_View.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                final Product product = Shop_List.get(position);
                                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(MainActivity.this);
                                deleteDialog.setTitle("移除商品");
                                deleteDialog.setMessage("是否从购物车移除"+product.getName());
                                if(position>=1)
                                {
                                    deleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(MainActivity.this,"成功移除商品"+product.getName(),Toast.LENGTH_SHORT).show();
                                            Shop_List.remove(product);
                                            Shop_Adapter.notifyDataSetChanged();
                                        }
                                    });

                                    deleteDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(MainActivity.this,"点击了取消",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    deleteDialog.show();
                                }
                                return true;
                            }
                        });
                    }
                    else//在购物车界面点击购物车图标
                    {
                        Flag1 = 0;
                        //回到商品列表界面
                        List_View.setVisibility(View.INVISIBLE);
                        Recycler_View.setVisibility(View.VISIBLE);
                        Shopping_Car.setImageResource(R.drawable.shoplist);
                        List_View.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                final Product product = Shop_List.get(position);
                                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(MainActivity.this);
                                deleteDialog.setTitle("移除商品");
                                deleteDialog.setMessage("是否从购物车移除"+product.getName());
                                if(position>=1)
                                {
                                    deleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(MainActivity.this,"成功移除商品"+product.getName(),Toast.LENGTH_SHORT).show();
                                            Shop_List.remove(product);
                                            Shop_Adapter.notifyDataSetChanged();
                                        }
                                    });

                                    deleteDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(MainActivity.this,"点击了取消",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    deleteDialog.show();
                                }
                                return true;
                            }
                        });
                    }
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case 1:
                if(resultCode == RESULT_OK)
                {
                    int returnedData = data.getIntExtra("SelectedNumber",0);
                    Log.d(Tag,"点击了"+returnedData);
                    Product product = new Product(Keywords[returnedData],Names[returnedData]);
                    Shop_List.add(product);
                    Shop_Adapter.notifyDataSetChanged();
                }
                break;
            default:
        }
    }

    private void initProducts() {
        for(int i=1;i<=10;i++) {
            Product product = new Product(Keywords[i],Names[i]);
            Products_List.add(product);
        }
    }

    private void initShopcar()
    {
        Product product = new Product(Keywords[0],Names[0]);
        Shop_List.add(product);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
