package receiver.receiver;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

public class NfcCarReceiverActivity extends Activity {
    /** Called when the activity is first created. */
	private Button mReceiveBtn ;
	private Button mSendBtn ;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        FindViews();
        SetListeners();
        
        Random random = new Random();
	    
	    int RanNum = random.nextInt(100) ;
	    FormatTrans.sStr = RanNum + "" ;
        
        
    }
    
private void FindViews() {
    	
    	mReceiveBtn = (Button)findViewById(R.id.MainActReceiveBtn);  
    	mSendBtn = (Button)findViewById(R.id.MainActSendBtn);    
        
    } // findViews()
    
    private void SetListeners() {
    	
    	mReceiveBtn.setOnClickListener( receive );
    	mSendBtn.setOnClickListener( send );
    	
    } // setListeners()
    
    private Button.OnClickListener receive = new OnClickListener() {
        public void onClick(View v) {
        	;
          // Perform action on click
        	
          Intent intent = new Intent();
          intent.setClass(NfcCarReceiverActivity.this, receiveP.class);
          startActivity(intent);	
            
        } // onClick()
    }; // receive button
    
    private Button.OnClickListener send = new OnClickListener() {
        public void onClick(View v) {
        	;
          // Perform action on click
        	
          Intent intent = new Intent();
          intent.setClass(NfcCarReceiverActivity.this, sendP.class);
          startActivity(intent);	
            
        } // onClick()
    }; // send button
    
}