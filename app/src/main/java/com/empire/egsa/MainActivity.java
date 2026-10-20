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
        stage = 0;
        moved = false;
        base();

        VideoView video = new VideoView(this);
        video.setVideoURI(
            Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro)
        );
        root.addView(video, new FrameLayout.LayoutParams(-1, -1));

        TextView skipHint = text("TAP ANYWHERE TO SKIP", 12, WHITE, true);
        skipHint.setBackground(shape(0xaa171920, 0, 24));
        skipHint.setPadding(22, 12, 22, 12);

        FrameLayout.LayoutParams hintParams = new FrameLayout.LayoutParams(
            -2, -2, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL
        );
        hintParams.bottomMargin = 36;
        root.addView(skipHint, hintParams);

        Runnable continueToNext = () -> {
            if (moved) return;
            moved = true;
            video.stopPlayback();
            download();
        };

        video.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                continueToNext.run();
            }
            return true;
        });

        skipHint.setOnClickListener(v -> continueToNext.run());
        video.setOnCompletionListener(mp -> continueToNext.run());

        video.setOnErrorListener((mp, what, extra) -> {
            continueToNext.run();
            return true;
        });

        video.setOnPreparedListener(mp -> {
            mp.setLooping(false);
            video.start();
        });
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
        if(stage != 0)return;
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
        stage = 2;
        base();

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(28, 18, 28, 18);
        page.setBackgroundColor(BG);
        root.addView(page, new FrameLayout.LayoutParams(-1, -1));

        // Top bar: server status on the left, player count on the right.
        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setGravity(Gravity.CENTER_VERTICAL);
        top.setBackground(shape(CARD, 0, 16));
        top.setPadding(20, 8, 20, 8);
        page.addView(top, new LinearLayout.LayoutParams(-1, 58));

        TextView server = text("SERVER: OFF", 15, 0xffff5968, true);
        server.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        top.addView(server, new LinearLayout.LayoutParams(0, -1, 1));

        TextView players = text("PLAYERS: 0/50", 15, WHITE, true);
        players.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        top.addView(players, new LinearLayout.LayoutParams(0, -1, 1));

        // Main content, centered and without Nickname or PLAY controls.
        FrameLayout center = new FrameLayout(this);
        LinearLayout.LayoutParams centerParams =
            new LinearLayout.LayoutParams(-1, 0, 1);
        centerParams.topMargin = 14;
        centerParams.bottomMargin = 8;
        page.addView(center, centerParams);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER);
        center.addView(content, new FrameLayout.LayoutParams(-1, -1));

        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.empire_logo);
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        content.addView(logo, new LinearLayout.LayoutParams(220, 130));

        TextView title = text("EMPIRE GAME", 30, WHITE, true);
        LinearLayout.LayoutParams titleParams =
            new LinearLayout.LayoutParams(-1, -2);
        titleParams.topMargin = 8;
        content.addView(title, titleParams);

        TextView subtitle = text("EG:SA  •  MOBILE", 13, MUTED, true);
        content.addView(subtitle);

        TextView footer = text("SERVER CONNECTION NOT AVAILABLE YET",
            12, MUTED, true);
        page.addView(footer, new LinearLayout.LayoutParams(-1, 28));
    }

    @Override public void onBackPressed() {
        if(stage==2) super.onBackPressed();
    }
}
