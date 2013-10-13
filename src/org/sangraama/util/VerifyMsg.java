package org.sangraama.util;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

public enum VerifyMsg {

    INSTANCE;
    private boolean verifyResult = false;
    private String TAG = "VerifyMsg : ";

    public boolean verifyMessage(String message, byte[] signature) {

        try {

            InputStream keyfis = this.getClass().getResourceAsStream("/PublicKey.txt");
            // FileInputStream keyfis = new FileInputStream("src/PublicKey.txt");
            byte[] encKey = new byte[keyfis.available()];
            keyfis.read(encKey);
            keyfis.close();
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
            KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
            PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
            Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
            sig.initVerify(pubKey);
            sig.update(message.getBytes());
            verifyResult = sig.verify(signature);
            System.out.println(TAG + "Message verification - " + verifyResult);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return verifyResult;
    }

}
