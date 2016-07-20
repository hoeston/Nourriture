package com.example.asuspc.test2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

	private LinearLayout home;
	private LinearLayout friend;
	private RelativeLayout myinfo;

	private ImageButton home_png;
	private ImageButton friend_png;
	private ImageButton myinfo_png;

	private Fragment homeFragment;
	private Fragment friendFragment;
	private Fragment myinfoFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initEvent();
		initFragment(0);
	}

	private void initFragment(int index) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		hideFragment(transaction);
		switch(index) {
			case 0:
				if (homeFragment == null) {
					homeFragment = new HomeFragment();
					transaction.add(R.id.container, homeFragment);
				} else {
					transaction.show(homeFragment);
				}
				break;
			case 1:
				if (friendFragment == null) {
					friendFragment = new FriendFragment();
					transaction.add(R.id.container, friendFragment);
				} else {
					transaction.show(friendFragment);
				}
				break;
			case 2:
				if (myinfoFragment == null) {
					myinfoFragment = new MyinfoFragment();
					transaction.add(R.id.container, myinfoFragment);
				} else {
					transaction.show(myinfoFragment);
				}
				break;
			default:
				break;
		}
		transaction.commit();
	}

	private void hideFragment(FragmentTransaction transaction) {
		if (homeFragment != null) {
			transaction.hide(homeFragment);
		}
		if (friendFragment != null) {
			transaction.hide(friendFragment);
		}
		if (myinfoFragment != null) {
			transaction.hide(myinfoFragment);
		}
	}

	private void  initEvent() {
		home.setOnClickListener(this);
		friend.setOnClickListener(this);
		myinfo.setOnClickListener(this);
	}

	private void initView() {
		this.home = (LinearLayout) getLayoutInflater().inflate(R.layout.home, null, false);
		this.friend = (LinearLayout)getLayoutInflater().inflate(R.layout.page2, null, false);
		this.myinfo = (RelativeLayout)getLayoutInflater().inflate(R.layout.me, null, false);

		this.home_png = (ImageButton) findViewById(R.id.id_tab_home_img);
		this.friend_png = (ImageButton) findViewById(R.id.id_tab_friend_img);
		this.myinfo_png = (ImageButton) findViewById(R.id.id_tab_myinfo_img);
	}



	public void onClick(View view) {
		restartButton();
		switch (view.getId()) {
			case R.id.home:
				home_png.setImageResource(R.mipmap.ic_logo);
				initFragment(0);
				break;
			case R.id.friend:
				friend_png.setImageResource(R.mipmap.ic_l1);
				initFragment(1);
				break;
			case R.id.myinfo:
				myinfo_png.setImageResource(R.mipmap.ic_l2);
				initFragment(2);
				break;
			default:
				break;
		}
	}

	private void restartButton() {
		home_png.setImageResource(R.mipmap.ic_logo);
		friend_png.setImageResource(R.mipmap.ic_l1);
		myinfo_png.setImageResource(R.mipmap.ic_l2);
	}

}
