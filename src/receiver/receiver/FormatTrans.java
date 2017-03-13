// StyleCheckType Boolean, InputStreamReader, BufferedReader
//package PL100_9827204 ;

package receiver.receiver;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.security.SecureRandom ;
import java.io.IOException;
import java.util.ArrayList;


import java.security.Key;   
import java.security.KeyFactory;   
import java.security.KeyPair;   
import java.security.KeyPairGenerator;   
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;   
import java.security.PublicKey;   
import java.security.interfaces.RSAPrivateKey;   
import java.security.interfaces.RSAPublicKey;   
import java.security.spec.PKCS8EncodedKeySpec;   
import java.security.spec.X509EncodedKeySpec;   
    
import javax.crypto.Cipher;   


    
import sun.misc.BASE64Decoder;   
import sun.misc.BASE64Encoder; 


class FormatTrans {  
	
    public static PublicKey sPublicKey = null ;
	public static PrivateKey sPrivateKey = null ;
	public static Cipher sCipher = null ;
	public static String sStr = "HRU?" ;
    // rsa 加密
    public static PublicKey getPublicKey(String key) throws Exception {   
        byte[] keyBytes;   
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);   

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);   
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");   
        PublicKey publicKey = keyFactory.generatePublic(keySpec);   
        return publicKey;   
    } // getPublicKey()
    
    public static PrivateKey getPrivateKey(String key) throws Exception {   
        byte[] keyBytes;   
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);   

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);   
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");   
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);   
        return privateKey;   
    } // getPrivateKey(()

    
    public static String getKeyString(Key key) throws Exception {   
        byte[] keyBytes = key.getEncoded();   
        String s = (new BASE64Encoder()).encode(keyBytes);   
        return s;   
    } // getKeyString() 
    
    /////以上是加密用
    
    
public static byte[] Encryption( int seed, String text ) throws Exception {   
  // 把明文text做加密  return 被加密後的文章.
  
  SecureRandom random = new SecureRandom( intToByte(seed) ) ;  
  // 用random
  
  KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");   
  //密鑰位數   
  keyPairGen.initialize(1024, random);   
  //密鑰對   
  KeyPair keyPair = keyPairGen.generateKeyPair();   
  // 公鑰私鑰 
  sPublicKey = (RSAPublicKey) keyPair.getPublic(); 
  sCipher = Cipher.getInstance("RSA");  		
  sCipher.init(Cipher.ENCRYPT_MODE, sPublicKey);   
  
  return sCipher.doFinal(text.getBytes());  
  // 加密
           
} // Encryption()


public static String Decryption( int seed, byte[] text ) throws Exception {   
  // 把祕文text 解密  return 解密後的明文.

  SecureRandom random = new SecureRandom( intToByte(seed) ) ;  
  // 用random
  
  KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");   
  //密鑰位數   
  keyPairGen.initialize(1024, random);   
  //密鑰對   
  KeyPair keyPair = keyPairGen.generateKeyPair();   
  // 公鑰私鑰 
  
  sPrivateKey = (RSAPrivateKey) keyPair.getPrivate();     
  sCipher = Cipher.getInstance("RSA");
           
  sCipher.init(Cipher.DECRYPT_MODE, sPrivateKey);   
  byte[] deBytes = sCipher.doFinal(text);      
  // 解密  
  
  String str = new String ( deBytes );
  // 把明文轉成string
  return str ;

} // Decryption()	
	

  public static byte[] ndefTextByte( String str ) {
    byte[] BshortHeader = new byte[] {  
		 (byte) 0xd1, //11010 001(MB ME CF SR IL INF(001))  
		 (byte) 0x01  
	   } ;//Type length=11  }
		 
		 
	   int length = str.length() + 3 ;
		 
	   if ( length > 255 ) str = str.substring( 0 , 252 ) ;
		 
	   // byte[] BpayloadLength = intToByte( str.length() + 3 ) ;
		 
	   byte[] BpayloadLength = FormatTrans.intToByte( str.length() + 3 ) ;
		 
		 
    byte[] Btype = new byte[] {
		 (byte) 0x54,// Type=T-->record type is TEXT  
	     (byte) 0x02,//Status byte 00000010 -->UTF-8, Language codes length=2  
	              
	     //Language Codes="en"  
	     (byte) 0x65,    // e      
	     (byte) 0x6e    // n
	   } ;
		 
		 
		 
		 
	   byte[] Bstr = FormatTrans.StringToByte( str ) ;
		 
	   byte[] fake1 = FormatTrans.ByteAdd(BshortHeader,BpayloadLength) ;
	   byte[] fake2 = FormatTrans.ByteAdd(fake1,Btype) ;
		
	
	   byte[] fakeX = FormatTrans.ByteAdd(fake2, Bstr) ;
		
	   
	   return fakeX;
  } // ndefText()
  
  public static byte[] ndefENTextByte( byte[] bstr ) {
	  byte[] BshortHeader = new byte[] {  
				 (byte) 0xd1, //11010 001(MB ME CF SR IL INF(001))  
				 (byte) 0x01  
			   } ;//Type length=11  }
				 
				 
			   int length = bstr.length + 3 ;
				 
			   
			   // if ( length > 255 ) str = str.substring( 0 , 252 ) ;
				 
			   // byte[] BpayloadLength = intToByte( str.length() + 3 ) ;
				 
			   byte[] BpayloadLength = FormatTrans.intToByte( length ) ;
				 
				 
		    byte[] Btype = new byte[] {
				 (byte) 0x54,// Type=T-->record type is TEXT  
			     (byte) 0x02,//Status byte 00000010 -->UTF-8, Language codes length=2  
			              
			     //Language Codes="en"  
			     (byte) 0x65,    // e      
			     (byte) 0x6e    // n
			   } ;
				 
				 
				 
				 
			   // byte[] Bstr = FormatTrans.StringToByte( str ) ;
				 
			   byte[] fake1 = FormatTrans.ByteAdd(BshortHeader,BpayloadLength) ;
			   byte[] fake2 = FormatTrans.ByteAdd(fake1,Btype) ;
				
			
			   byte[] fakeX = FormatTrans.ByteAdd(fake2, bstr) ;
				
			   
			   return fakeX;
	  } // ndefText()
  
  public static byte[] ndefTextENByte( byte[] byteArr ) {
	    byte[] BshortHeader = new byte[] {  
			 (byte) 0xd1, //11010 001(MB ME CF SR IL INF(001))  
			 (byte) 0x01  
		   } ;//Type length=11  }
			 
			 
		   int length = byteArr.length ;
			 
		   // if ( length > 255 ) str = str.substring( 0 , 252 ) ;
			 
		   // byte[] BpayloadLength = intToByte( str.length() + 3 ) ;
			 
		   byte[] BpayloadLength = FormatTrans.intToByte( length ) ;
			 
			 
	    byte[] Btype = new byte[] {
			 (byte) 0x54,// Type=T-->record type is TEXT  
		     (byte) 0x02,//Status byte 00000010 -->UTF-8, Language codes length=2  
		              
		     //Language Codes="en"  
		     (byte) 0x65,    // e      
		     (byte) 0x6e    // n
		   } ;
			 
			 
			 
			 
		   // byte[] Bstr = FormatTrans.StringToByte( str ) ;
			 
		   byte[] fake1 = FormatTrans.ByteAdd(BshortHeader,BpayloadLength) ;
		   byte[] fake2 = FormatTrans.ByteAdd(fake1,Btype) ;
			
		
		   byte[] fakeX = FormatTrans.ByteAdd(fake2, byteArr ) ;
			
		   
		   return fakeX;
	  } // ndefText()

  public static int bytesToInt(byte[] bytes) {

    int num = bytes[0] & 0xFF;

    num |= ((bytes[1] << 8) & 0xFF00);

    num |= ((bytes[2] << 16) & 0xFF0000);

    num |= ((bytes[3] << 24) & 0xFF000000);

    return num;

  }


  
  public static byte[] StringToByte(String str ) {

      byte[] bt = new byte[str.length()];
      
      for( int i = 0 ; i < str.length() ; i++ ) {
      	bt[i] = (byte) ( 0xff & str.charAt(i) );
      } // for 

      


      return bt;

  }
	
	
  public static byte[] intToByte(int i) {

      byte[] bt = new byte[1];

      bt[0] = (byte) (0xff & i);


      return bt;

  }
  
  public static byte[] ByteAdd(byte[] a , byte[] b ) {

      byte[] bt = new byte[ (a.length +  b.length) ];

      for( int i = 0 ; i < a.length ; i++ ) {
      	bt[i] = a[i] ;
      } // for 

      for( int i = 0 ; i < b.length ; i++ ) {
      	bt[ (i + a.length) ] = b[i] ;
      } // for 

      return bt;

  }
  
  
  public static byte[] SubByteArray( byte[] a, int begin, int end) {

      byte[] bt = new byte[ end - begin ];

      for( int i = 0 ; i < ( end - begin ); i++ ) {
      	bt[i] = a[ i + begin ] ;
      } // for 

       

      return bt;

  }
  
} // FormatTrans