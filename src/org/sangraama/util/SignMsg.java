package org.sangraama.util;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Properties;

public class SignMsg {

    private Properties prop;

    public void signMessage(String messsage) {

        try {
            prop = new Properties();
            this.prop.load(getClass().getResourceAsStream("/sangraamaserver.properties"));
            byte[] encKey= prop.getProperty("privatekey").getBytes();

            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(encKey);
            KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
            PrivateKey priKey = keyFactory.generatePrivate(privKeySpec);

            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");

            dsa.initSign(priKey);
            byte[] msg=messsage.getBytes();
            dsa.update(msg);
            byte[] signature=dsa.sign();
            
            encKey= prop.getProperty("publickey").getBytes();
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
            PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
            dsa.initVerify(pubKey);
            
            boolean verifies = dsa.verify(signature);
            System.out.println("signature verifies: " + verifies);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
