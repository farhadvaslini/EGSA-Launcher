package com.empire.egsa;

import android.app.*;
import android.os.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.net.Uri;
import android.widget.*;

public class MainActivity extends Activity {
    final int BG=0xff090A0E, RED=0xffF1263D, WHITE=Color.WHITE,
        MUTED=0xffA7AAB5, CARD=0xff171920;
    FrameLayout root;
    Handler handler=new Handler(Looper.getMainLooper());
    int stage=0;
    boolean moved=false;
    EditText nickname;

    GradientDrawable shape(int color,int stroke,int radius) {
        GradientDrawable d=new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        if(stroke!=0) d.setStroke(2,stroke);
        return d;
    }

    TextView text(String value,int size,int color,boolean bold) {
        TextView t=new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER);
        if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        return t;
    }

    LinearLayout column() {
        LinearLayout l=new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setGravity(Gravity.CENTER);
        l.setPadding(25,22,25,22);
        return l;
    }

    void base() {
        root=new FrameLayout(this);
        root.setBackground(new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[]{0xff310810,BG,BG}));
        setContentView(root);
    }

    void addSpace(LinearLayout l,int height) {
        View v=new View(this);
        l.addView(v,new LinearLayout.LayoutParams(1,height));
    }

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);
        intro();
    }

    void intro() {
        stage=0;
        moved=false;
        base();

        LinearLayout l=column();
        root.addView(l,new FrameLayout.LayoutParams(-1,-1));

        TextView logo=text("EG",76,RED,true);
        logo.setBackground(shape(0xff180B10,RED,32));
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(150,150);
        l.addView(logo,lp);

        addSpace(l,24);
        l.addView(text("EMPIRE GAME",30,WHITE,true));
        l.addView(text("EG:SA  •  MOBILE LAUNCHER",13,MUTED,true));
        addSpace(l,40);

        Button enter=button("ENTER EMPIRE",RED);
        l.addView(enter,new LinearLayout.LayoutParams(-1,56));
        l.addView(text("TAP TO CONTINUE",12,MUTED,true));
        enter.setOnClickListener(v->download());

        // Intro video can be added here once video playback is verified.
    }

    Button button(String title,int color) {
        Button b=new Button(this);
        b.setText(title);
        b.setTextSize(16);
        b.setTextColor(WHITE);
        b.setTypeface(null,Typeface.BOLD);
        b.setAllCaps(false);
        b.setBackground(shape(color,0,18));
        return b;
    }

    void download() {
        if(moved)return;
        moved=true;
        stage=1;
        base();
        LinearLayout l=column();
        root.addView(l,new FrameLayout.LayoutParams(-1,-1));
        l.addView(text("EG:SA",24,RED,true));
        l.addView(text("PREPARING LAUNCHER",18,WHITE,true));
        addSpace(l,20);
        ProgressBar p=new ProgressBar(this,null,
            android.R.attr.progressBarStyleHorizontal);
        p.setMax(100);
        p.setProgressTintList(
            android.content.res.ColorStateList.valueOf(RED));
        l.addView(p,new LinearLayout.LayoutParams(-1,12));
        TextView label=text("Launcher UI ready",14,MUTED,false);
        l.addView(label);
        handler.postDelayed(this::home,900);
    }

    void home() {
        stage=2;
        base();
        ScrollView scroll=new ScrollView(this);
        root.addView(scroll,new FrameLayout.LayoutParams(-1,-1));

        LinearLayout page=new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(22,28,22,28);
        scroll.addView(page);

        LinearLayout top=new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        TextView brand=text("EG:SA",27,WHITE,true);
        top.addView(brand,new LinearLayout.LayoutParams(0,60,1));
        TextView status=text("● LAUNCHER",12,0xff57D68D,true);
        top.addView(status);
        page.addView(top);

        TextView hero=text("EMPIRE\nGAME",39,WHITE,true);
        hero.setGravity(Gravity.CENTER_VERTICAL);
        hero.setPadding(20,20,20,20);
        hero.setBackground(shape(0xff230D13,RED,22));
        page.addView(hero,new LinearLayout.LayoutParams(-1,190));

        addSpace(page,18);
        page.addView(text("LOS SANTOS • ROLEPLAY",14,MUTED,true));
        addSpace(page,20);

        TextView card=text("SERVER STATUS\nNot connected yet",17,WHITE,true);
        card.setBackground(shape(CARD,0,18));
        page.addView(card,new LinearLayout.LayoutParams(-1,90));

        addSpace(page,16);
        TextView players=text("PLAYERS     -- / 50",14,MUTED,true);
        players.setBackground(shape(CARD,0,18));
        page.addView(players,new LinearLayout.LayoutParams(-1,58));

        addSpace(page,22);
        nickname=new EditText(this);
        nickname.setSingleLine(true);
        nickname.setHint("Enter your nickname");
        nickname.setHintTextColor(MUTED);
        nickname.setTextColor(WHITE);
        nickname.setPadding(18,8,18,8);
        nickname.setBackground(shape(CARD,RED,16));
        page.addView(nickname,new LinearLayout.LayoutParams(-1,58));

        addSpace(page,14);
        Button play=button("PLAY",RED);
        page.addView(play,new LinearLayout.LayoutParams(-1,60));
        play.setOnClickListener(v->{
            String name=nickname.getText().toString().trim();
            if(name.isEmpty()) {
                nickname.setError("Enter your nickname");
                nickname.requestFocus();
                ((InputMethodManager)getSystemService(
                    Context.INPUT_METHOD_SERVICE)).showSoftInput(
                    nickname,InputMethodManager.SHOW_IMPLICIT);
                return;
            }
            new AlertDialog.Builder(this)
                .setTitle("EG:SA")
                .setMessage("Welcome, "+name+"!\nServer connection will be added later.")
                .setPositiveButton("OK",null).show();
        });

        addSpace(page,18);
        page.addView(text("NEWS & UPDATES",17,WHITE,true));
        TextView news=text(
            "Welcome to EMPIRE GAME.\nLauncher preview version 1.0.0",
            14,MUTED,false);
        news.setBackground(shape(CARD,0,16));
        news.setPadding(12,18,12,18);
        page.addView(news,new LinearLayout.LayoutParams(-1,95));

        addSpace(page,18);
        page.addView(text("SETTINGS   •   SUPPORT   •   ABOUT",12,MUTED,true));
    }

    @Override public void onBackPressed() {
        if(stage!=2)home();
        else super.onBackPressed();
    }
}
