package com.chialung.jclprofile.GameThisMonth;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chialung.jclprofile.R;
import com.chialung.jclprofile.jclModule.LoadBmp;
import com.chialung.jclprofile.jclModule.Tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by chialung on 2015/5/25.
 */
public class DisplayGameProfile extends Fragment {

    public Context m_context;
    public View my_view;
    public String url;
    public String gameTitle;
    public String gameId;
    private int sampleImageCount = 1;
    private String gameBrandUrl = "";
    private LinearLayout ll_softImageLayout;


    private Document doc;
    private Elements softImage, softTable, softStory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.debug("DisplayGameProfile -- onCreateView", 3);
        return inflater.inflate(R.layout.display_game_profile, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        m_context = getActivity().getApplicationContext();
        my_view = getView();

        Tools.debug("DisplayGameProfile -- onActivityCreated", 3);
        super.onActivityCreated(savedInstanceState);

        //讀出來的網址
        //RareFunction.debug("load url:" + getArguments().getString("gameUrl"), 3);
        url = getArguments().getString("gameUrl");
        gameTitle = getArguments().getString("gameTitle");
        gameId =getArguments().getString("gameId");
        getHtmlRaw();
    }

    private void getHtmlRaw(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Map mycookie = new Hashtable();
                    mycookie.put("__utma", "28147114.1493862857.1387864810.1422936753.1423806069.13");
                    mycookie.put("__utmz", "28147114.1387864810.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); getchu_adalt_flag=getchu.com");
                    mycookie.put("_ga", "GA1.2.1493862857.1387864810");

                    doc = Jsoup.connect(url)
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36")
                            .cookies(mycookie)
                            .get();

                    softImage = doc.select("img[width=280]");
                    softTable = doc.select("table#soft_table table td");
                    softStory = doc.select("div.tablebody");
                    //softImages = doc.select("div[align=center]");

                    handler.sendEmptyMessage(12001);

                }catch (IOException e){
                    e.getMessage();
                }

            }
        }).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 12001:
                    setupSoftdata();
                    break;

                case LoadBmp.IMAGE_LOAD_COMPLETE:
                    Tools.debug("圖片讀取成功", 3);
                    sampleImageLoadingSuccess();
                    break;

                case LoadBmp.IMAGE_LOAD_FAIL:
                    Tools.debug("圖片讀取失敗", 3);
                    sampleImageLoadingFail();
                    break;
            }
        }
    };

    private void setupSoftdata(){

        ImageView iv_softImage = (ImageView)my_view.findViewById(R.id.iv_gameProfileImage);
        for (Element imgObj : softImage){
            Tools.debug("url連結:"+imgObj.select("img[width=280]").attr("abs:src").toString(), 3);
            String imageLink = imgObj.select("img[width=280]").attr("abs:src").toString();
            LoadBmp.getBitmap(imageLink, iv_softImage, null);
        }

        TextView tv_softTitle = (TextView)my_view.findViewById(R.id.tv_gameTitle);
        tv_softTitle.setText(gameTitle);

        ArrayList<String> softDetail = new ArrayList<String>();
        for (Element softDetailObj :softTable){
            //產品各類資訊
            if (!softDetailObj.select("td[align=top]").text().equals("")) {
                softDetail.add(softDetailObj.select("td[align=top]").text());
            }

            //製造廠商的連結網址
            if (!softDetailObj.select("a.glance").attr("abs:href").equals("")){
                //RareFunction.debug("td d search:" + softDetailObj.select("a.glance").attr("abs:href"), 3);
                gameBrandUrl = softDetailObj.select("a.glance").attr("abs:href");
            }

        }

        //製作商
        TextView tv_softBrand = (TextView)my_view.findViewById(R.id.tv_brand);
        String[] brand = softDetail.get(0).split(" ");
        tv_softBrand.setText(brand[0]);

        tv_softBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(gameBrandUrl);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });

        TextView tv_softPrice = (TextView)my_view.findViewById(R.id.tv_price);
        tv_softPrice.setText(softDetail.get(1));
        TextView tv_softReleaseDate = (TextView)my_view.findViewById(R.id.tv_release);
        tv_softReleaseDate.setText(softDetail.get(2));

        //Story , 系統介紹,
        ArrayList<String> story = new ArrayList<String>();
        for (Element storyObj : softStory){
            if (storyObj.text().indexOf(R.string.galgame_string_001) == -1){
                story.add(storyObj.text());
            }
        }

        TextView tv_softStory = (TextView)my_view.findViewById(R.id.tv_gameIntro);
        for (int i=0; i < story.size(); i++){
            if(!story.get(i).equals("")){
                tv_softStory.append(story.get(i));
                tv_softStory.append("\n");
                tv_softStory.append("\n");
            }
        }
        ///brandnew/854302/c854302sample7.jpg

        ll_softImageLayout = (LinearLayout)my_view.findViewById(R.id.ll_softImageLayout);

        //圖片搜尋
        LoadSampleImage();

    }

    private void LoadSampleImage(){

        String bmpurl = "http://www.getchu.com/brandnew/"+gameId+"/c"+gameId+"sample"+sampleImageCount+".jpg";

        View view = View.inflate(m_context, R.layout.display_game_sample_image_item, null);
        ImageView iv_sampleImage = (ImageView)view.findViewById(R.id.iv_sampleImage);

        LoadBmp.getBitmap(bmpurl, iv_sampleImage, handler);


        ll_softImageLayout.addView(view);
    }

    private void sampleImageLoadingSuccess(){
        sampleImageCount++;
        LoadSampleImage();
    }

    private void sampleImageLoadingFail(){
        ll_softImageLayout.removeViewAt(sampleImageCount-1);
        sampleImageCount = 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Tools.debug("DisplayGameProfile -- onDestroyView", 3);
    }

    @Override
    public void onPause() {
        super.onPause();
        Tools.debug("DisplayGameProfile -- onPause", 3);
    }
}
