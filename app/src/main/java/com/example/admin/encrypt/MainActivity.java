package com.example.admin.encrypt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class MainActivity extends AppCompatActivity {

    private TextView encrypt;
    private TextView decrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {
        String key = "qwer1234";
        String data = "nihao";
        encrypt = (TextView) findViewById(R.id.encrypt);
        encrypt.setText(encrypt(encoder(key.getBytes()), encoder(data.getBytes())));
        decrypt = (TextView) findViewById(R.id.decrypt);
        String result2 = decrypt(key.getBytes(), encrypt(encoder(key.getBytes()), encoder(data.getBytes())));
        decrypt.setText(result2);
        Log.v("tag",result2);
        System.out.println(result2);
    }

    private String decrypt(byte[] key, String data) {


        try {
            // 生成一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 从原始密钥数据创建DESKeySpec对象
            DESKeySpec dks = null;
            dks = new DESKeySpec(key);
            // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密钥初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
            // 正式进行解密操作

            String result = new String(cipher.doFinal(decoder(data)));
            Log.v("tag",result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "wrong";
    }

    private String encrypt(byte[] key, byte[] data) {
        //生成一个可信的随机数
        SecureRandom sr = new SecureRandom();

        try {
            //从原始秘钥数据创建一个deskeyspec对象
            DESKeySpec desKeySpec = new DESKeySpec(key);
            //创建一个秘钥工厂，然后将deskeyspec转化为secretkey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);
            return new String(cipher.doFinal(data));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return "wrong";
    }

    public byte[] encoder(byte[] data) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data).getBytes();
    }
    public byte[] decoder(String  data) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(data);

    }
}
