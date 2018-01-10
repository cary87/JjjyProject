package com.jiujiu.autosos.common.utils;

import android.text.TextUtils;
import android.util.Base64;

import com.code19.library.StringUtils;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * RSA加解密管理类
 * 
 * @author yangweiquan 2017年3月27日
 */
public class RSACoder {

	private static final int KEY_SIZE = 1024;
	private static final String KEY_ALGORITHM = "RSA";
	// 算法名称
	private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

	private static final String PUBLIC_KEY = "RSAPublicKey";
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	/**
	 * RSA加密
	 * 
	 * @param data
	 *            明文（经过Base64编码）
	 * @param publicKeyBase64
	 *            公钥（经过Base64编码）
	 * @return
	 * @throws Exception
	 */
	public static String encodeByPublicKey(String data, String publicKeyBase64) throws Exception {
		try {

			// 对公钥进行Base64解码
			byte[] keyBytes = Base64.decode(publicKeyBase64,Base64.DEFAULT);

			// 取得公钥
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key publicKey = keyFactory.generatePublic(x509KeySpec);

			// 对数据加密
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);

			byte[] encodeBytes = cipher.doFinal(data.getBytes());
			return Base64.encodeToString(encodeBytes, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
			String message = "未知";
			if (e instanceof NoSuchAlgorithmException) {
				message = "没有 " + KEY_ALGORITHM + " 算法";
				message = "没有该算法";
			} else if (e instanceof InvalidKeySpecException || e instanceof InvalidKeyException) {
				message = "公钥非法";
			} else if (e instanceof NoSuchPaddingException) {
				message = "没有 " + TRANSFORMATION + " 这种加解密格式";
				// 使用的加解密格式不应该向外暴露
				message = "";
			} else if (e instanceof BadPaddingException) {
				message = "坏填充 " + TRANSFORMATION;
				// 使用的加解密格式不应该向外暴露
				message = "";
			} else if (e instanceof IllegalBlockSizeException) {
				message = "非法的区块大小 " + TRANSFORMATION;
				// 使用的加解密格式不应该向外暴露
				message = "";
			}

			if (TextUtils.isEmpty(message)) {
				message = "加密失败";
			} else {
				message = "加密失败：" + message;
			}
			throw new Exception(message);
		}
	}

	/**
	 * RSA解密
	 * 
	 * @param encodeData
	 *            加密数据（经过Base64编码）
	 * @param privateKeyBase64
	 *            私钥（经过Base64编码）
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String encodeData, String privateKeyBase64) throws Exception {
		try {
			// 对密钥进行Base64解码
			byte[] keyBytes = Base64.decode(privateKeyBase64,Base64.DEFAULT);

			// 取得私钥
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

			// 对数据解密
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);

			byte[] decodeBytes = cipher.doFinal(Base64.decode(encodeData,Base64.DEFAULT));
			return new String(decodeBytes);
		} catch (Exception e) {
			e.printStackTrace();
			String message = "未知";
			if (e instanceof NoSuchAlgorithmException) {
				message = "没有 " + KEY_ALGORITHM + " 算法";
				message = "没有该算法";
			} else if (e instanceof InvalidKeySpecException || e instanceof InvalidKeyException) {
				message = "公钥非法";
			} else if (e instanceof NoSuchPaddingException) {
				message = "没有 " + TRANSFORMATION + " 这种加解密格式";
				// 使用的加解密格式不应该向外暴露
				message = "";
			} else if (e instanceof BadPaddingException) {
				message = "坏填充 " + TRANSFORMATION;
				// 使用的加解密格式不应该向外暴露
				message = "";
			} else if (e instanceof IllegalBlockSizeException) {
				message = "非法的区块大小 " + TRANSFORMATION;
				// 使用的加解密格式不应该向外暴露
				message = "";
			}

			if (StringUtils.isEmpty(message)) {
				message = "解密失败";
			} else {
				message = "解密失败：" + message;
			}
			throw new Exception(message);
		}
	}

	/**
	 * 获得私钥
	 * 
	 * @param keyMap
	 *            公钥、私钥的Map
	 * @return
	 */
	public static String getPrivateKey(Map<String, Object> keyMap) {
		return getKeyBase64(keyMap, PRIVATE_KEY);
	}

	/**
	 * 获得公钥
	 * 
	 * @param keyMap
	 *            公钥、私钥的Map
	 * @return
	 */
	public static String getPublicKey(Map<String, Object> keyMap) {
		return getKeyBase64(keyMap, PUBLIC_KEY);
	}

	/**
	 * 获取key的Base64编码
	 * 
	 * @param keyMap
	 *            公钥、私钥的Map
	 * @param type
	 *            类型：公钥、私钥
	 * @return
	 */
	private static String getKeyBase64(Map<String, Object> keyMap, String type) {
		Key key = (Key) keyMap.get(type);
		byte[] binaryData = key.getEncoded();

		return Base64.encodeToString(binaryData,Base64.DEFAULT);
	}

	/**
	 * 生成RSA的公钥、私钥
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static Map<String, Object> initKey() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(KEY_SIZE);

		KeyPair keyPair = keyPairGen.generateKeyPair();

		// 公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		// 私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);

		return keyMap;
	}

}
