package models;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;


public class Sign {

	 public static String signData(byte[] data, PrivateKey privKey) throws Exception {

		 Signature signer = Signature.getInstance("SHA1withDSA");
		 signer.initSign(privKey);
		 signer.update(data);

		 //return (signer.sign());
                 return Base64.encode(signer.sign());
	}

	 public static boolean verifySig(byte[] data, String signData, PublicKey pubKey) throws Exception {
            Boolean result = false;

            Signature signer = Signature.getInstance("SHA1withDSA");
            signer.initVerify(pubKey);
            signer.update(data);

            if (Base64.decode(signData) != null) {
                result = signer.verify(Base64.decode(signData));
            }

            return result;
	}

	 public static PrivateKey getPrivateKey(){

		 PrivateKey key=null;// = new Object();
		 ObjectInputStream ois;
		try {

			ois = new ObjectInputStream(new FileInputStream(System.getProperty("user.dir") + "/conf/key"));

				key  =(PrivateKey)ois.readObject();
				ois.close();
				System.out.println("Privatekey = "+key);

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
	 public static PublicKey getPublicKey(){
            String path = System.getProperty("user.dir") + "/conf/key.pub";
                PublicKey key =null;
                ObjectInputStream ois;

		try {
			ois = new ObjectInputStream(new FileInputStream(path));
			key = (PublicKey)ois.readObject();
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
	 public static void main(String[] args) {

		 String str = "nina"+"zineb"+"essoufy_@hotmail.com";
		 String str1 = "nina"+"zineb"+"essoufy_@hotmail";// - com  6a36162d b0126c1b b6288e56

		 boolean verified;

		 Sign sign = new Sign();

		 try {


			//GenSig.generateKeyPair();
			byte[] signature =(sign.signData(str.getBytes(), Sign.getPrivateKey()));
			System.out.println("signature = "+signature.toString());



		      verified = sign.verifySig(str1.getBytes(), signature, Sign.getPublicKey());
			    System.out.println("verified = "+verified) ;

		 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
*/
}
