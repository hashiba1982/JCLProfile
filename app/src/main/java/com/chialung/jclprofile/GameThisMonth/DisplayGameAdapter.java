package com.chialung.jclprofile.GameThisMonth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chialung.jclprofile.R;
import com.chialung.jclprofile.jclModule.LoadBmp;

import java.util.ArrayList;



/**
 * Created by chialung on 2015/5/19.
 */
public class DisplayGameAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<GameItem> gamelist;
    public LayoutInflater myInflater;
    public View view;


    public DisplayGameAdapter(Context context, ArrayList<GameItem> gamelist){
        this.context = context;
        this.gamelist = gamelist;
        this.myInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return gamelist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = myInflater.inflate(R.layout.display_game_item, null);
        view = convertView;

        TextView tv_gameTitle = (TextView)view.findViewById(R.id.tv_gameTitle);
        tv_gameTitle.setText(gamelist.get(position).gameTitle);

        ImageView imageView = (ImageView)view.findViewById(R.id.iv_gameImage);
        imageView.setTag(gamelist.get(position).gameImage);
        LoadBmp.getBitmap(gamelist.get(position).gameImage, imageView, null);

/*        convertView.setTag(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tagNo = (Integer)v.getTag();
                RareFunction.debug("�̭����y:"+gamelist.get(tagNo).gameTitle, 3);
                Fragment displayGameProfile = new DisplayGameProfile();
                FragmentManager fm = context.getFragmentManager();
                fm.beginTransaction().replace(R.id.fl_displayGameContiner, displayGameProfile).commit();
                FrameLayout fl_gameProfile = (FrameLayout)getView().findViewById(R.id.fl_displayGameContiner);
                fl_gameProfile.setVisibility(View.VISIBLE);
            }
        });*/

        return convertView;
    }


    /*public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            RareFunction.debug("what�O����:"+msg.what, 3);
*//*            switch (msg.what){
                case LoadBmp.IMAGE_LOAD_COMPLETE:

                    Map<String, Object> getBmpset = (Map)msg.obj;
                    ImageView completeImageView = (ImageView)getBmpset.get("imageView");
                    Bitmap bmp = (Bitmap)getBmpset.get("image");
                    completeImageView.setImageBitmap(bmp);

                    RareFunction.debug("���J���\�badapter��", 3);
                    break;

                case LoadBmp.IMAGE_LOAD_FAIL:
                    RareFunction.debug("���J���Ѧbadapter��", 3);
                    break;
            }*//*
        }
    };*/
}
