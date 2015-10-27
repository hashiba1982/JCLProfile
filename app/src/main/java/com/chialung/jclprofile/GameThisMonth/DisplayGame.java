package com.chialung.jclprofile.GameThisMonth;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;

import com.chialung.jclprofile.R;
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
 * Created by chialung on 2015/5/13.
 */
public class DisplayGame extends Fragment {

    public String url = "http://www.getchu.com/all/month_title.html?genre=pc_soft&gc=gc";

    public Document doc;
    public Elements productRaw, products, years, months;
    public ArrayList<GameItem> gamelist;
    public Button btn_otherMonth, btn_nextMonth, btn_lastMonth;
    public DisplayGameAdapter displayGameAdapter;
    public GridView gv_gameGridView;

    private Context m_context;
    private View m_view;

    //年月選擇項目
    private Spinner sp_year;
    private Spinner sp_month;

    public ArrayList<String> yearList = new ArrayList<String>();
    private String[] monthList = new String[]{"", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    public ArrayAdapter<String> yearAdapter;
    public ArrayAdapter<String> monthAdapter;
    public int yearNow;
    public int monthNow;
    public int yearSelected;
    public int monthSelected;

    public Message saveMsg; //保存msg資料可以不用重讀

    public DisplayGame(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.debug("DisplayGame -- onCreateView", 3);
        return inflater.inflate(R.layout.display_game, container, false);
    }

    @Override
    public void onDestroyView() {
        Tools.debug("DisplayGame -- onDestroyView", 3);
        super.onDestroyView();
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Tools.debug("DisplayGame -- onActivityCreated", 3);

        m_context = getActivity().getApplicationContext();
        m_view = getView();

        btn_lastMonth = (Button)getView().findViewById(R.id.btn_lastMonth);
        btn_lastMonth.setOnClickListener(switchOtherPage);
        btn_nextMonth = (Button)getView().findViewById(R.id.btn_nextMonth);
        btn_nextMonth.setOnClickListener(switchOtherPage);
        btn_otherMonth = (Button)getView().findViewById(R.id.btn_otherMonth);
        btn_otherMonth.setOnClickListener(switchOtherPage);

        //開始讀取資料
        if (saveMsg != null){
            setupGameData(saveMsg);
        }else {
            getHtmlRaw();
        }
    }

    private void getHtmlRaw() {
    //http://www.getchu.com/all/month_title.html?genre=pc_soft&gc=gc

        Tools.debug("getHtmlRaw():"+url, 3);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //Getchu的header資料
                    Map mycookie = new Hashtable();
                    mycookie.put("__utma", "28147114.1493862857.1387864810.1422936753.1423806069.13");
                    mycookie.put("__utmz", "28147114.1387864810.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); getchu_adalt_flag=getchu.com");
                    mycookie.put("_ga", "GA1.2.1493862857.1387864810");

                    doc = Jsoup.connect(url)
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36")
                            .cookies(mycookie)
                            .get();

                    //link = doc.select("a[href]");
                    Tools.debug("getHtmlRaw() -> thread", 3);
                    productRaw = doc.select("table.product");
                    products = productRaw.select("td.dd");

                    years = doc.select("select[name=year] OPTION");
                    months = doc.select("select[name=month] OPTION");
                    //RareFunction.debug("RAW:"+  doc.select("select[name=year] OPTION"), 3);
                    //gameTitle2 = gameTitle.select("a[href].black");
                    //media = gameTitle.select("img[width=120]");
                    //media = doc.select("img[width=120]");
                    //other = doc.select("link[href]");

                    handler.sendEmptyMessage(1001);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setupYearMonth(){

        int yearMax = yearNow;
        int yearMin = 1997;

        for (int i = yearMax; i >= yearMin; i--){
            yearList.add(String.valueOf(i));
        }

        //年的選擇
        yearAdapter = new ArrayAdapter<String>(m_context, android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

        sp_year = (Spinner)m_view.findViewById(R.id.sp_yearList);
        sp_year.setAdapter(yearAdapter);
        sp_year.setSelection(yearList.indexOf(String.valueOf(yearNow)));
        sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearSelected = Integer.parseInt(yearList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //月的選擇
        monthAdapter = new ArrayAdapter<String>(m_context,android.R.layout.simple_spinner_item, monthList);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

        sp_month = (Spinner)m_view.findViewById(R.id.sp_monthList);
        sp_month.setAdapter(monthAdapter);
        sp_month.setSelection(monthNow);
        sp_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthSelected = Integer.parseInt(monthList[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    Button.OnClickListener switchOtherPage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.btn_nextMonth:
                    monthNow++;
                    if (monthNow > 12){
                        yearNow++;
                        monthNow = 1;
                    }

                    url = "http://www.getchu.com/all/month_title.html?genre=pc_soft&gage=all&year="+yearNow+"&month="+monthNow;
                    break;

                case R.id.btn_lastMonth:
                    monthNow--;
                    if (monthNow < 1){
                        yearNow--;
                        monthNow = 12;

                        if (yearNow < 1997){
                            yearNow = 1997;
                            monthNow= 1;
                        }
                    }

                    url = "http://www.getchu.com/all/month_title.html?genre=pc_soft&gage=all&year="+yearNow+"&month="+monthNow;
                    break;

                case R.id.btn_otherMonth:
                    url = "http://www.getchu.com/all/month_title.html?genre=pc_soft&gage=all&year="+yearSelected+"&month="+monthSelected;
                    break;
            }

            displayGameAdapter = null;
            doc = null;
            productRaw = null;
            products = null;

            getHtmlRaw();

        }
    };


    //html解析跟資料填裝
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    setupGameData(msg);
                    break;
            }
        }
    };

    public void setupGameData(Message msg){

        saveMsg = msg;

        gamelist = new ArrayList<GameItem>();

        for (Element obj : products){
            //RareFunction.debug("handle products", 3);
            GameItem gameItem = new GameItem();
            gameItem.gameTitle = obj.select("a[href].black").text();
            gameItem.gameUrl = obj.select("a.black").attr("abs:href").toString();
            gameItem.gameImage = obj.select("img[width=120]").attr("abs:src").toString();
            gamelist.add(gameItem);
        }

        displayGameAdapter = new DisplayGameAdapter(m_context, gamelist);
        gv_gameGridView = (GridView)m_view.findViewById(R.id.gv_gameGridView);
        gv_gameGridView.setAdapter(displayGameAdapter);
        gv_gameGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showGameProfile(position);
            }
        });


        //因為頁面有兩組同樣的select所以會跳兩次
        for (Element yearObj : years){
            if (!yearObj.select("OPTION[selected]").text().equals("")){
                //RareFunction.debug("Year Option:"+yearObj.select("OPTION[selected]").text(), 3);
                yearNow = Integer.parseInt(yearObj.select("OPTION[selected]").text());
                //RareFunction.debug("yearNow:"+yearNow, 3);
            }
        }

        for (Element monthObj : months){
            if (!monthObj.select("OPTION[selected]").text().equals("")){
                //RareFunction.debug("Year Option:"+yearObj.select("OPTION[selected]").text(), 3);
                monthNow = Integer.parseInt(monthObj.select("OPTION[selected]").text());
                //RareFunction.debug("monthNow:"+monthNow, 3);
            }
        }

        yearSelected =yearNow;
        monthSelected = monthNow;

        setupYearMonth();
    }

    //開啟詳細資料
    public void showGameProfile(int position){
        Tools.debug("目前的遊戲:" + gamelist.get(position).gameTitle, 3);

        //查詢遊戲id
        int gameIdIndex = gamelist.get(position).gameUrl.indexOf("id=");
        String gameId = gamelist.get(position).gameUrl.substring(gameIdIndex+3);

        //RareFunction.debug("進入onItemSelected", 3);
        Fragment displayGameProfile = new DisplayGameProfile();

        Bundle bundle = new Bundle();
        bundle.putString("gameUrl", gamelist.get(position).gameUrl);
        bundle.putString("gameTitle", gamelist.get(position).gameTitle);
        bundle.putString("gameId", gameId);
        displayGameProfile.setArguments(bundle);

        FragmentManager fm = getFragmentManager();

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_container, displayGameProfile, null);

        //transaction.add(R.id.frame_container, displayGameProfile);
        transaction.addToBackStack(null);
        transaction.commit();

        //FrameLayout fl_gameProfile = (FrameLayout) getView().findViewById(R.id.fl_displayGameContiner);
        //fl_gameProfile.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        Tools.debug("DisplayGame -- onStart", 3);
    }

    @Override
    public void onResume() {
        super.onResume();
        Tools.debug("DisplayGame -- onResume", 3);
    }

    @Override
    public void onPause() {
        super.onPause();
        Tools.debug("DisplayGame -- onPause", 3);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Tools.debug("DisplayGame -- onSaveInstanceState", 3);
    }
}
