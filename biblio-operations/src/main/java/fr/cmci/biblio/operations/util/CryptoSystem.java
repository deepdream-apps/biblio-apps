package fr.cmci.biblio.operations.util;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class CryptoSystem {
	@Autowired
	private Environment env ;
	private static CryptoSystem crypto ;
	
	public static CryptoSystem getCrypto() {
		if (crypto == null) {
			crypto = new CryptoSystem() ;
		}
		return crypto ;
	}
	
	public  String encrypt(String plaintext) {
		try {
			IvParameterSpec iv = new IvParameterSpec(env.getProperty("app.security.encryption.init_vector").getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(env.getProperty("app.security.encryption.key").getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(plaintext.getBytes());
			return Base64.encodeBase64String(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	

	public  String decrypt(String ciphertext) {
		try {
			IvParameterSpec iv = new IvParameterSpec(env.getProperty("app.security.encryption.init_vector").getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(env.getProperty("app.security.encryption.key").getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(Base64.decodeBase64(ciphertext));
			return new String(original) ;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
