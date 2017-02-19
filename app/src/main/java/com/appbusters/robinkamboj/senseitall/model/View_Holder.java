package com.appbusters.robinkamboj.senseitall.model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appbusters.robinkamboj.senseitall.R;
import com.appbusters.robinkamboj.senseitall.controller.ItemClickListener;
import com.appbusters.robinkamboj.senseitall.view.BatteryActivity;
import com.appbusters.robinkamboj.senseitall.view.CPUActivity;
import com.appbusters.robinkamboj.senseitall.view.CameraActivity;
import com.appbusters.robinkamboj.senseitall.view.FlashActivity;
import com.appbusters.robinkamboj.senseitall.view.GSMActivity;
import com.appbusters.robinkamboj.senseitall.view.LightActivity;
import com.appbusters.robinkamboj.senseitall.view.ProximityActivity;
import com.appbusters.robinkamboj.senseitall.view.VibratorActivity;

public class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

    public TextView sensor_name;
    public ImageView sensor_imageview;
    public CardView cardView;
    ItemClickListener clickListener;
    Context context;


    public View_Holder(View itemView) {
        super(itemView);

        sensor_name = (TextView) itemView.findViewById(R.id.textView);
        sensor_imageview = (ImageView) itemView.findViewById(R.id.imageView);
        cardView = (CardView) itemView.findViewById(R.id.cardView);

        context = itemView.getContext();
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setClickListener(ItemClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View view) {
        clickListener.onCLick(itemView, getAdapterPosition(), false);
    }

    @Override
    public boolean onLongClick(View view) {
        clickListener.onCLick(itemView, getAdapterPosition(), true);
        return false;
    }

    public void intent(String sensorName, int position){
        position++;
        switch (position){
            case 1:{
                Intent i = new Intent(context, CameraActivity.class);
                i.putExtra("sensorName", sensorName);
                context.startActivity(i);
                break;
            }
            case 2:{
                Intent i = new Intent(context, CameraActivity.class);
                i.putExtra("sensorName", sensorName);
                context.startActivity(i);
                break;
            }
            case 3:{
                break;
            }
            case 4:{

                break;
            }
            case 5:{

                break;
            }
            case 6:{
                Intent i = new Intent(context, GSMActivity.class);
                i.putExtra("sensorName", sensorName);
                context.startActivity(i);
                break;
            }
            case 7:{

                break;
            }
            case 8:{

                break;
            }
            case 9:{

                break;
            }
            case 10:{

                break;
            }
            case 11:{
                Intent i = new Intent(context, BatteryActivity.class);
                i.putExtra("sensorName", sensorName);
                context.startActivity(i);
                break;
            }
            case 12:{
                Intent i = new Intent(context, CPUActivity.class);
                i.putExtra("sensorName", sensorName);
                context.startActivity(i);
                break;
            }
            case 13:{

                break;
            }
            case 14:{
                Intent i = new Intent(context, VibratorActivity.class);
                i.putExtra("sensorName", sensorName);
                context.startActivity(i);
                break;
            }
            case 15:{

                break;
            }
            case 16:{

                break;
            }
            case 17:{

                break;
            }
            case 18:{

                break;
            }
            case 19:{
                Intent i = new Intent(context, LightActivity.class);
                i.putExtra("sensorName", sensorName);
                context.startActivity(i);
                break;
            }
            case 20:{
                Intent i = new Intent(context, ProximityActivity.class);
                i.putExtra("sensorName", sensorName);
                context.startActivity(i);
                break;
            }
            case 21:{

                break;
            }
            case 22:{

                break;
            }
            case 23:{

                break;
            }
            case 24:{
                Intent i = new Intent(context, FlashActivity.class);
                i.putExtra("sensorName", sensorName);
                context.startActivity(i);
                break;
            }
        }
    }
}
