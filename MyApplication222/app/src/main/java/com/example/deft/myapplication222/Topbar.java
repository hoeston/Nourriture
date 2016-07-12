package com.example.deft.myapplication222;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Topbar extends RelativeLayout {
    private Button leftButton,rightButton;
    private TextView tvTitle;

    private int leftTextColor;
    private Drawable leftBackground;
    private String leftText;

    private int rightTextColor;
    private Drawable rightBackground;
    private String rightText;

    private float titleTextSize;
    private int titleTextColor;
    private String title;

    private topbarClickListener listener;

    public interface topbarClickListener{
        public void leftClick();
        public void rightClick();
    }

    public void setOnTopbarClickListener(topbarClickListener listener){
        this.listener=listener;
    }

    private LayoutParams leftParams,rightParames,titleParames;

    public Topbar(final Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray ta= context.obtainStyledAttributes(attrs,R.styleable.Topbar);
        leftTextColor = ta.getColor(R.styleable.Topbar_leftTextColor,0);
        leftText=ta.getString(R.styleable.Topbar_leftText);
        leftBackground=ta.getDrawable(R.styleable.Topbar_leftBackground);

        rightTextColor = ta.getColor(R.styleable.Topbar_rightTextColor,0);
        rightText=ta.getString(R.styleable.Topbar_rightText);
        rightBackground=ta.getDrawable(R.styleable.Topbar_rightBackground);

        titleTextColor=ta.getColor(R.styleable.Topbar_titleTextColor,0);
        titleTextSize=ta.getDimension(R.styleable.Topbar_titleTextSize,0);
        title=ta.getString(R.styleable.Topbar_title);

        ta.recycle();
        leftButton=new Button(context);
        rightButton=new Button(context);
        tvTitle=new TextView(context);

        leftButton.setTextColor(leftTextColor);
        leftButton.setBackground(leftBackground);
        leftButton.setText(leftText);

        rightButton.setTextColor(rightTextColor);
        rightButton.setBackground(rightBackground);
        rightButton.setText(rightText);

        tvTitle.setTextColor(titleTextColor);
        tvTitle.setText(title);
        tvTitle.setTextSize(titleTextSize);
        tvTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        setBackgroundColor(0xFFF59563);

        leftParams=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
        addView(leftButton, leftParams);

        rightParames=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightParames.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
        addView(rightButton, rightParames);

        titleParames=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        titleParames.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
        addView(tvTitle,titleParames);

        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.leftClick();
            }
        });

        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.rightClick();
            }
        });

    }

}
