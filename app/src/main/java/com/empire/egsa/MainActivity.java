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
        fullscreen();
        intro();
    }

    void fullscreen() {
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    class FullscreenVideoView extends VideoView {
        public FullscreenVideoView(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            setMeasuredDimension(width, height);
        }
    }

    void intro() {
        stage = 0;
        moved = false;
        base();

        FullscreenVideoView video = new FullscreenVideoView(this);
        video.setVideoURI(
            Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro)
        );
        FrameLayout.LayoutParams videoParams =
            new FrameLayout.LayoutParams(-1, -1);
        root.addView(video, videoParams);

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
        if (stage != 0) return;
        moved = true;
        stage = 1;
        base();

        LinearLayout page = column();
        page.setGravity(Gravity.CENTER);
        page.setPadding(0, 24, 0, 24);
        root.addView(page, new FrameLayout.LayoutParams(-1, -1));

        TextView logoTitle = text("EG:SA", 34, Color.WHITE, true);
        logoTitle.setGravity(Gravity.CENTER);
        page.addView(logoTitle,
            new LinearLayout.LayoutParams(-1, -2));

        TextView heading = text("LAUNCHER SETUP", 22, RED, true);
        heading.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams headingParams =
            new LinearLayout.LayoutParams(-1, -2);
        headingParams.topMargin = 18;
        page.addView(heading, headingParams);

        TextView info = text(
            "Preparing EMPIRE GAME",
            16, WHITE, false);
        info.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams infoParams =
            new LinearLayout.LayoutParams(-1, -2);
        infoParams.topMargin = 12;
        page.addView(info, infoParams);

        ProgressBar progress = new ProgressBar(this, null,
            android.R.attr.progressBarStyleHorizontal);
        progress.setMax(100);
        progress.setProgress(100);
        progress.setProgressTintList(
            android.content.res.ColorStateList.valueOf(Color.WHITE));
        progress.setProgressBackgroundTintList(
            android.content.res.ColorStateList.valueOf(0xff555555));

        LinearLayout.LayoutParams progressParams =
            new LinearLayout.LayoutParams(-1, 50);
        progressParams.gravity = Gravity.CENTER;
        progressParams.leftMargin = 0;
        progressParams.rightMargin = 0;
        progressParams.topMargin = 22;
        page.addView(progress, progressParams);

        TextView note = text(
            "Launcher ready",
            14, WHITE, false);
        note.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams noteParams =
            new LinearLayout.LayoutParams(-1, -2);
        noteParams.topMargin = 12;
        page.addView(note, noteParams);

        handler.postDelayed(this::home, 1600);
    }

    void home() {
        stage = 2;
        fullscreen();
        base();

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setGravity(Gravity.CENTER_HORIZONTAL);
        page.setPadding(28, 20, 28, 20);
        page.setBackgroundColor(BG);
        root.addView(page, new FrameLayout.LayoutParams(-1, -1));

        LinearLayout statusBar = new LinearLayout(this);
        statusBar.setOrientation(LinearLayout.HORIZONTAL);
        statusBar.setGravity(Gravity.CENTER_VERTICAL);
        statusBar.setPadding(18, 0, 18, 0);
        statusBar.setBackground(shape(CARD, 0, 14));
        page.addView(statusBar, new LinearLayout.LayoutParams(-1, 54));

        TextView server = text("SERVER: OFF", 14, 0xffff5968, true);
        server.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        statusBar.addView(server, new LinearLayout.LayoutParams(0, -1, 1));

        TextView players = text("PLAYERS: 0/50", 14, WHITE, true);
        players.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        statusBar.addView(players, new LinearLayout.LayoutParams(0, -1, 1));

        FrameLayout middle = new FrameLayout(this);
        LinearLayout.LayoutParams middleParams =
            new LinearLayout.LayoutParams(-1, 0, 1);
        middleParams.topMargin = 12;
        middleParams.bottomMargin = 12;
        page.addView(middle, middleParams);

        LinearLayout center = new LinearLayout(this);
        center.setOrientation(LinearLayout.VERTICAL);
        center.setGravity(Gravity.CENTER);
        center.setPadding(0, 24, 0, 0);
        middle.addView(center, new FrameLayout.LayoutParams(-1, -1));

        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.empire_logo);
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        center.addView(logo, new LinearLayout.LayoutParams(170, 100));

        TextView title = text("EMPIRE GAME", 27, WHITE, true);
        LinearLayout.LayoutParams titleParams =
            new LinearLayout.LayoutParams(-1, -2);
        titleParams.topMargin = 5;
        center.addView(title, titleParams);

        TextView subtitle = text("EG:SA  •  MOBILE", 12, MUTED, true);
        center.addView(subtitle);

        LinearLayout bottom = new LinearLayout(this);
        bottom.setOrientation(LinearLayout.VERTICAL);
        bottom.setPadding(0, 0, 0, 0);
        LinearLayout.LayoutParams bottomParams =
            new LinearLayout.LayoutParams(-1, -2);
        bottomParams.topMargin = 18;
        center.addView(bottom, bottomParams);

        nickname = new EditText(this);
        nickname.setSingleLine(true);
        nickname.setTextSize(18);
        nickname.setHint("(Nickname)");
        nickname.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_DONE);
        nickname.setFocusable(true);
        nickname.setFocusableInTouchMode(true);
        nickname.setInputType(
            android.text.InputType.TYPE_CLASS_TEXT
            | android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        );
        nickname.setTextColor(Color.WHITE);
        nickname.setGravity(Gravity.CENTER);
        nickname.setHintTextColor(MUTED);
        nickname.setTextColor(WHITE);
        nickname.setPadding(16, 0, 16, 0);
        nickname.setInputType(
            android.text.InputType.TYPE_CLASS_TEXT
            | android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        );
        nickname.setBackground(shape(CARD, 0xff454854, 12));
        LinearLayout.LayoutParams nickParams =
            new LinearLayout.LayoutParams(320, 60);
        nickParams.gravity = Gravity.CENTER;
        bottom.addView(nickname, nickParams);

        Button play = button("PLAY", RED);
        play.setText("PLAY");
        play.setAllCaps(true);
        play.setTextColor(Color.WHITE);
        play.setTextSize(22);
        play.setGravity(Gravity.CENTER);
        play.setVisibility(android.view.View.VISIBLE);
        play.setEnabled(true);
        play.setMinWidth(0);
        play.setMinHeight(0);
        play.setPadding(8, 0, 8, 0);
        play.setTextColor(Color.WHITE);
        play.setTextSize(16);
        play.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams playParams =
            new LinearLayout.LayoutParams(440, 92);
        playParams.gravity = Gravity.CENTER;
        playParams.topMargin = 10;
        bottom.addView(play, playParams);

        play.setOnClickListener(v -> {
            String name = nickname.getText().toString().trim();
            if (name.isEmpty()) {
                nickname.setError("Enter your nickname");
                nickname.requestFocus();
                return;
            }

            new AlertDialog.Builder(this)
                .setTitle("EG:SA")
                .setMessage("Welcome, " + name
                    + "!\\nServer: OFFLINE\\nGame connection is not available yet.")
                .setPositiveButton("OK", null)
                .show();
        });
    }

    @Override public void onBackPressed() {
        if(stage==2) super.onBackPressed();
    }
}
