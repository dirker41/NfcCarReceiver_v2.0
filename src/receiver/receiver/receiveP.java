package receiver.receiver;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

public class receiveP extends Activity {
	
    private TextView mTextView_ReadMsg ;
	
	private NfcAdapter mAdapter = null;

    private static PendingIntent mPendingIntent;
	private static IntentFilter[] mFilters;
	private static String[][] mTechLists;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_page);
        
        FindViews();
	    SetListensers();
	    
	    mAdapter = NfcAdapter.getDefaultAdapter(this);

        // Create a generic PendingIntent that will be deliver to this activity. The NFC stack
        // will fill in the intent with the details of the discovered tag before delivering to
        // this activity.
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Setup an intent filter for all MIME based dispatches
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[] {
                ndef,
        };

        // Setup a tech list for all NfcF tags
        mTechLists = new String[][] { new String[] { NfcF.class.getName() } };
    }
	
	private void FindViews() {
		 
	     
		mTextView_ReadMsg = (TextView) findViewById(R.id.R_PtextView1 ) ;
	       
	 } // FindViews()
	 
	 private void SetListensers() {
		 mTextView_ReadMsg.setText( FormatTrans.sStr + "\n" + "receiving..." ) ;
		 
		 
	 } // SetListensers()

	 
	 void resolveIntent(Intent intent) {
	   	    String action = intent.getAction();
	   	    
	   	    // if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
	   	    if ( "android.nfc.action.NDEF_DISCOVERED".equals(action)) {
	   	    	
	   	    	
	            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
	            NdefMessage[] msgs;


	            if (rawMsgs != null) {
	                msgs = new NdefMessage[rawMsgs.length];
	                for (int i = 0; i < rawMsgs.length; i++) {
	                    msgs[i] = (NdefMessage) rawMsgs[i];
	                } // for 
	            } // if
	            else {
	            // Unknown tag type
	              byte[] empty = new byte[] {};
	              NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
	              NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
	              msgs = new NdefMessage[] {msg};
	              
	            } // else
	            
	            
	            buildTagViews( msgs ) ;
	            
	            
	            
	    	} // if 
	  
	  } // resolveIntent()
	    
	  void buildTagViews(NdefMessage[] msgs) {
	        if (msgs == null || msgs.length == 0) {
	            return;
	        } // if
	        
	        String str = "" ;
	        
	        byte[] bstr = msgs[0].getRecords()[0].getPayload() ;

	        str = new String ( msgs[0].getRecords()[0].getPayload() );
	        
	        if ( str.length() >= 18 &&
	        	 str.substring(3,18).equals("ask to connect!") ) {
	        	// if ask request ,jmp to send page 
	        	
	        	mTextView_ReadMsg.setText( "Receive a Nfc connection..." + "\n" ) ;
	        	
	        	Intent xintent = new Intent();
	            xintent.setClass(receiveP.this, sendP.class);
	            startActivity(xintent);
	            // finish(); //////////////////////////////////////////////////////////////////////////////////
	        } // if ask request ,jmp to send page 
	        else {

	          // mTextView_ReadMsg.setText( str + "\n" ) ;
	          
	          // CheckRNumber( bstr );
	          
	          //str = str.substring( 3 , str.length() ) ;////////////////////////////////////
	          
	          //if ( str.equals( randomNum )  ) drive();//////////////////////////////////
	          //else ignore() ;////////////////////////////////////////////////////////////
	          
	        } // else 
	      
	  } // buildTagViews
	  
	  private void CheckRNumber ( byte[] bstr )  {
		  String str = "" ;
		  
		  // subbstr 3~length
		  //  FormatTrans.SubByteArray
		  
		  try { 
		    // str = FormatTrans.Decryption( 0, bstr ) ;
		  } catch ( Exception e ) {
			  
			 str = "O O this an error happen in decryption" ;
		  }
		  
		  
		  mTextView_ReadMsg.setText( str + "\n" + "had receive..." ) ;
		 
		  
		  
		  
	  } // CheckRNumber()
	 
	 @Override
	 public void onResume() {
	     super.onResume();
	     mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
	 }

	 @Override
	 public void onNewIntent(Intent intent) {
	  	mTextView_ReadMsg.setText("Discovered tag " + " with intent: " + intent);
	    resolveIntent(intent);
	    
	    // if "check" then jmp to send
	    // else if drive keep in this page  
	    
	 }

	 @Override
	 public void onPause() {
	     super.onPause();
	     mAdapter.disableForegroundDispatch(this);
	 }
}
