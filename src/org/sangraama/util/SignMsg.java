package org.sangraama.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

public enum SignMsg {

    INSTANCE;
    private byte[] signature;
    private String TAG = "SignMsg : ";

    public byte[] signMessage(String messsage) {

        try {
            InputStream keyfis = this.getClass().getResourceAsStream("/PrivateKey.txt");
            //FileInputStream keyfis = new FileInputStream("/sangraama-server/WebContent/WEB-INF/sign/PrivateKey.txt");
            byte[] encKey = new byte[keyfis.available()];
            keyfis.read(encKey);
            keyfis.close();
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(encKey);
            KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
            PrivateKey priKey = keyFactory.generatePrivate(privKeySpec);
            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(priKey);
            dsa.update(messsage.getBytes());
            signature = dsa.sign();
            //System.out.println(TAG + "Done signing the message");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return signature;
    }
}
