package controllers;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.File;

public class GenSig {

	 PrivateKey privKey ;
     PublicKey pubKey ;

     public  void generateKeyPair() throws Exception {
    	    
    	 KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
    	 SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
    	 
    	 keyGen.initialize(1024, random);
    	 KeyPair pair = keyGen.generateKeyPair();
    	 //System.out.println("key = "+keyGen.getAlgorithm());	     
	     
	      this.privKey = pair.getPrivate();
	      this.pubKey = pair.getPublic();
	      
	   // System.out.println("privkey = "+this.privKey);
	   //System.out.println("pubkey = "+this.pubKey);
    }
     public PrivateKey getPrivateKey(){
    	 return this.privKey;
     }
     
     public PublicKey getPublicKey(){
    	 return this.pubKey;
     }
	 public static byte[] signData(byte[] data, PrivateKey privKey) throws Exception {
		   
		 Signature signer = Signature.getInstance("SHA1withDSA");
		 signer.initSign(privKey);
		 signer.update(data);
		 
		 return (signer.sign());
	}
  
	 public static boolean verifySig(byte[] data, byte[] signData, PublicKey pubKey) throws Exception {
		    Signature signer = Signature.getInstance("SHA1withDSA");
		    signer.initVerify(pubKey);
		    signer.update(data);
		    return (signer.verify(signData));

		  }
	
	 public static void enregistrer(Object key, String file){
		 
		 ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(key);
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	 }
	 public static PublicKey deserialize( String file){
		 
		 PublicKey key =null;
		 ObjectInputStream ois;
		try {
			
			ois = new ObjectInputStream(new FileInputStream(file));
			key  =(PublicKey)ois.readObject();
			ois.close();
			//System.out.println("key = "+key);
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return key;
	 }
	 
	 /*
           public static Object lecture(boolean bool, String file){

		 Object key = new Object();
		 ObjectInputStream ois;
		try {

			ois = new ObjectInputStream(new FileInputStream(file));

			//if (key instanceof PrivateKey)
			if(bool == true){
				key  =(PrivateKey)ois.readObject();
			}
			//else if (key instanceof PublicKey)
			else if(bool == false){
				key  =(PublicKey)ois.readObject();
			}

			ois.close();
			//System.out.println("key = "+key);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return key;
	 }


          */
	 public static void main(String[] args) {
		 
		 String str = "nina"+"zineb"+"essoufy_@hotmail.com";
		 String str1 = "nina"+"zineb"+"essoufy_@hotmail";// - com  6a36162d b0126c1b b6288e56
		 boolean verified;
		
		 GenSig sign = new GenSig();
		 
		 try {
			 
			sign.generateKeyPair();
//			sign.enregistrer(sign.getPrivateKey(),"private.Obj");
	//		sign.enregistrer(sign.getPublicKey(),"public.Obj");
			
		//	PublicKey pubKey; 
			PrivateKey privKey=	(PrivateKey)GenSig.lecture(true,"private.Obj" );
			System.out.println("privkey  OK = "+privKey);
			
			PublicKey pubKey=	(PublicKey)GenSig.lecture(false,"public.Obj" );
			System.out.println("pubkey  ko = "+pubKey);
			
			//sign.lecture(pubKey,"public.Obj" );
			
			/*byte[]strSign = sign.signData(str.getBytes());
			byte[]strSign1 = sign.signData(str1.getBytes()); // j'ai g�n�r� une autre signature juste pour faire des test
			
			//System.out.println("strSign = "+strSign.toString());
			
						
		//	verified = sign.verifySig(str.getBytes(), strSign);// verified = true car la signature (strSign) correspond bien � la donn�e str
		  //  System.out.println("verified = "+verified) ;
		    
		   // verified = sign.verifySig(str1.getBytes(), strSign);// verified = false car la signature (strSign) ne correspond pa  � la donn�e str1 mais str h�h�
		  // System.out.println("verified = "+verified) ;
		    
		      verified = sign.verifySig(str.getBytes(), strSign1);// verified = false car la signature (strSign1) ne correspond pa  � la donn�e str mais str1 kiki
			    System.out.println("verified = "+verified) ;
			*/
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }

}
