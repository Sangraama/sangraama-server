package org.sangraama.util;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

public enum SignMsg {

    INSTANCE;
    private byte[] signature;
    private String TAG = "SignMsg : ";

    /**
     * Sign the message using a private key
     *
     * @param message message to be signed
     * @return signature of the message
     */
    public byte[] signMessage(String message) {

        try {
            InputStream keyfis = this.getClass().getResourceAsStream("/key/PrivateKey.txt");
            byte[] encKey = new byte[keyfis.available()];
            keyfis.read(encKey);
            keyfis.close();
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(encKey);
            KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
            PrivateKey priKey = keyFactory.generatePrivate(privKeySpec);
            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(priKey);
            dsa.update(message.getBytes());
            signature = dsa.sign();
            // System.out.println(TAG + "Done signing the message");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return signature;
    }
}
