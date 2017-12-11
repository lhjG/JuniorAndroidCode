package com.example.ainge.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ainge.myapplication.R;

import org.greenrobot.eventbus.EventBus;

import static java.security.AccessController.getContext;


public class ShoppingActivity extends AppCompatActivity {

    private int SAc_Images [] ={R.drawable.enchatedforest,R.drawable.arla,R.drawable.devondale,
            R.drawable.kindle,R.drawable.waitrose, R.drawable.mcvitie,R.drawable.ferrero,R.drawable.maltesers,
            R.drawable.lindt,R.drawable.borggreve};
    private String SAc_Names [] = {"购物车","Enchated Forest","Arla Milk","Devondale Milk",
            "Kindle Oasis","waitrose 早餐麦片","Mcvitie's 饼干","Ferreo Rocher",
            "Maltesers","Lindt","Borggreve"};
    private String SAc_Prices [] = {"¥5.00","¥59.00","¥79.00","¥2399.00","¥179.00", "¥14.90",
            "¥132.59","¥141.43","¥139.43","¥28.90"};
    private String SAc_MoreInformation [] = {"作者    Johanna Basford","产地    德国","产地    澳大利亚","版本    8GB"
            ,    "重量    2Kg","产地    英国","重量    300g","重量    118g","重量    249g","重量    640g"};


    private String SAc_Intention [] = {"一键下单","分享商品","不感兴趣","查看更多商品促销信息"};

    private ImageView SAc_ImageView;
    private TextView SAc_NameView;
    private TextView SAc_PriceView;
    private TextView SAc_InformationView;
    private ImageView BackView;
    private ImageView StarView;
    private ImageView CarView;

    private int Flag = 0;
    private static final String DYNAMICACTION = "AddShoppingList";
    private static final String WIDGET_DYNAMICACTION = "addshoppinglist_forwidget";
    private IntentFilter DynamicFilter;
    private IntentFilter widget_dynamicfilter;
    private NotifyAdded notifyadded;
    private NewAppWidget newappwidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DynamicFilter = new IntentFilter();
        DynamicFilter.addAction(DYNAMICACTION);
        notifyadded = new NotifyAdded();
        registerReceiver(notifyadded,DynamicFilter);
        setContentView(R.layout.activity_shopping);

        widget_dynamicfilter = new IntentFilter();
        widget_dynamicfilter.addAction(WIDGET_DYNAMICACTION);
        newappwidget = new NewAppWidget();
        registerReceiver(newappwidget,widget_dynamicfilter);


        SAc_ImageView = (ImageView) findViewById(R.id.productImage);
        SAc_NameView = (TextView) findViewById(R.id.productName);
        SAc_PriceView = (TextView) findViewById(R.id.price);
        SAc_InformationView = (TextView) findViewById(R.id.version);
        BackView = (ImageView) findViewById(R.id.back);
        StarView = (ImageView) findViewById(R.id.star);
        CarView = (ImageView) findViewById(R.id.shopcar);

        if(Flag==0) StarView.setImageResource(R.drawable.empty_star);
        else StarView.setImageResource(R.drawable.full_star);

        final Intent intent1 = getIntent();
        final String IntentName = intent1.getStringExtra("name");
        int IntentNumber = ConvertNumber(IntentName); //通过商品的名字返回数组的索引
        SAc_ImageView.setImageResource(SAc_Images[IntentNumber-1]);
        SAc_NameView.setText(SAc_Names[IntentNumber]);
        SAc_PriceView.setText(SAc_Prices[IntentNumber-1]);
        SAc_InformationView.setText(SAc_MoreInformation[IntentNumber-1]);
        //返回
        BackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();   //销毁当前的活动
            }
        });
        //收藏
        StarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Flag == 1) {
                    StarView.setImageResource(R.drawable.empty_star);
                    Flag=0;
                }
                else {
                    StarView.setImageResource(R.drawable.full_star);
                    Flag=1;
                }
            }
        });
        //加入购物车
        CarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShoppingActivity.this,"商品已添加到购物车",Toast.LENGTH_SHORT).show();


                Intent intent2 = new Intent();
                int SelectedNumber = ConvertNumber(SAc_NameView.getText().toString());
                intent2.setAction(DYNAMICACTION);
                intent2.putExtra("SelectedNumber",SelectedNumber);
                sendBroadcast(intent2);

                Bundle bundle = new Bundle();
                bundle.putInt("widget_image",SAc_Images[SelectedNumber-1]);
                bundle.putString("widget_name",SAc_Names[SelectedNumber]);
                Intent towidget_intent=new Intent();
                towidget_intent.setAction(WIDGET_DYNAMICACTION);
                towidget_intent.putExtras(bundle);
                sendBroadcast(towidget_intent);

//                Intent intent2 = new Intent();


//                setResult(RESULT_OK,intent2);
                EventBus.getDefault().post(new AddToShoppingList(SelectedNumber));
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                ShoppingActivity.this,android.R.layout.simple_list_item_1,SAc_Intention);
        ListView listView = (ListView) findViewById(R.id.moreinformation);
        listView.setAdapter(arrayAdapter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notifyadded);
        unregisterReceiver(newappwidget);
    }
    private int ConvertNumber(String S) {
            int result=0;              //要声明一个返回变量才可以，不能直接返回,字符串不能直接比较要用equals
            if(S.equals("Enchated Forest")){result=1;}
            if(S.equals("Arla Milk")){result=2;}
            if(S.equals("Devondale Milk")){result=3;}
            if(S.equals("Kindle Oasis")){result=4;}
            if(S.equals("waitrose 早餐麦片")){result=5;}
            if(S.equals("Mcvitie's 饼干")){result=6;}
            if(S.equals("Ferreo Rocher")){result=7;}
            if(S.equals("Maltesers")){result=8;}
            if(S.equals("Lindt")){result=9;}
            if(S.equals("Borggreve")){result=10;}
            return result;
    }
}
