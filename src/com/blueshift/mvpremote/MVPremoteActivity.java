package com.blueshift.mvpremote;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class MVPremoteActivity extends Activity {
    /** Called when the activity is first created. */
	DatagramSocket s;
	InetAddress addr;
	String hostname;
	EditText HostNameBox;
	
	View pageOne;
	View pageTwo;
	
	private SharedPreferences mPrefs;

	private void setAddr() {
		try {
			addr = InetAddress.getByName(hostname);
		} catch (UnknownHostException e) {
			addr = null;
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        MyPagerAdapter adapter = new MyPagerAdapter();
        ViewPager myPager = (ViewPager) findViewById(R.id.mypanelpager);
        myPager.setAdapter(adapter);
        myPager.setCurrentItem(0);
        
        mPrefs = getPreferences(MODE_PRIVATE);

        hostname = mPrefs.getString("hostname", "192.168.1.255");

        
        try {
			s = new DatagramSocket();
			addr = InetAddress.getByName(hostname);
		} catch (SocketException e) {
			// TODO Auto-generated catch blck
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    protected void onPause() {
    	super.onPause();

    	SharedPreferences.Editor ed = mPrefs.edit();
    	ed.putString("hostname", HostNameBox.getText().toString());
    	ed.commit();
    }

    public void onButtonPressed(View v) {
    	byte[] b = new byte[3];
    	byte val=0;
    	
    	b[0] = 0x00;
    	
    	switch(v.getId()) {
    	case R.id.bPageUp:
    		val = 11;
    		break;
    	case R.id.bUp:
    		val = 32;
    		break;

    	case R.id.bGreen:
    		val=46;
    		break;
    	case R.id.bLeft:
    		val=17;
    		break;
    	case R.id.bOK:
    		val = 37;
    		break;
    	case R.id.bRight:
    		val = 16;
    		break;
    	case R.id.bPageDown:
    		val = 56;
    		break;
    	case R.id.bDown:
    		val = 33;
    		break;
    	case R.id.bBack:
    		val=41;
    		break;
    	case R.id.bMute:
    		val = 15;
    		break;
    	case R.id.bBlank:
    		val = 12;
    		break;
    	case R.id.bFull:
    		val = 60;
    		break;
    	case R.id.bRewind:
    		val = 50;
    		break;
    	case R.id.bPlay:
    		val = 53;
    		break;
    	case R.id.bFF:
    		val = 52;
    		break;
    	case R.id.bRec:
    		val = 55;
    		break;
    	case R.id.bStop:
    		val = 54;
    		break;
    	case R.id.bPause:
    		val = 48;
    		break;
    	case R.id.b1:
    		val = 1;
    		break;
    	case R.id.b2:
    		val = 2;
    		break;
    	case R.id.b3:
       		val = 3;
       		break;
    	case R.id.b4:
    		val = 4;
    		break;
    	case R.id.b5:
    		val = 5;
    		break;
    	case R.id.b6:
    		val = 6;
    		break;
    	case R.id.b7:
    		val = 7;
    		break;
    	case R.id.b8:
    		val = 8;
    		break;
    	case R.id.b9:
    		val = 9;
    		break;
    	case R.id.b0:
    		val = 0;
    		break;
    	case R.id.bExit:
    		val = 31;
    		break;
    	case R.id.bMenu:
    		val = 13;
    		break;
//    	case R.id.:
//    		val = 30;
//    		break;
//    	case R.id.bReplay:
//    		val = 36;
//    		break;
    	}
    	
    	b[1] = (byte) val;

    	if (addr == null) {
    		Toast.makeText(this, "Invalid Host", Toast.LENGTH_LONG).show();		
    		return;
    	}
    	
    	try {
			s.send(new DatagramPacket(b,2, addr, 16000));
//			b[0] = 0x10;
//			Thread.sleep(100);
//			s.send(new DatagramPacket(b,2, addr, 16000));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
    }
    
    private void initHostNameBox() {
        HostNameBox = (EditText) findViewById(R.id.hostName);
        
        if(HostNameBox == null)
        	return;

        HostNameBox.setText(hostname);    
		HostNameBox.addTextChangedListener( new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				hostname = HostNameBox.getText().toString();
				setAddr();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}
		});
    }

    private class MyPagerAdapter extends PagerAdapter {

		public int getCount() {
            return 2;
        }

        public Object instantiateItem(View collection, int position) {

            LayoutInflater inflater = (LayoutInflater) collection.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            int resId = 0;
            switch (position) {
            case 0:
                resId = R.layout.page_one;
                break;
            case 1:
                resId = R.layout.page_two;
                break;
            }

            View view = inflater.inflate(resId, null);

            ((ViewPager) collection).addView(view, 0);
    		initHostNameBox();
            return view;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == ((View) arg1);
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }
}