package fr.cmci.biblio.web.util;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import fr.cmci.biblio.web.controlers.AbonneCtrl;

@Component
public class CryptoSystem {
	@Autowired
	private Environment env ;
	private Logger logger = Logger.getLogger(AbonneCtrl.class.getName()) ;
	

	
	public  String encrypt(String plaintext) {
		try {
			IvParameterSpec iv = new IvParameterSpec(env.getProperty("app.security.encryption.init_vector").getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(env.getProperty("app.security.encryption.key").getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(plaintext.getBytes());
			return Base64.encodeBase64String(encrypted);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
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
			logger.log(Level.SEVERE, ex.getMessage());
			return null;
		}
	}

}
