package org.sangraama.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestVerifyMsg {

    private SignMsg signMsg;
    private VerifyMsg verifyMsg;
    
    private String message1 = "{\"positionX\":156.26292,\"positionY\":11.90625,\"health\":100.0,\"score\":0.0,\"url\":\"localhost:8081/sangraama/sangraama/player\",\"pt\":1}" ;
    private String message2 = "{\"positionX\":43.64371,\"positionY\":123.32567,\"health\":100.0,\"score\":10.0,\"url\":\"localhost:8080/sangraama/sangraama/player\",\"pt\":1}" ;
    private byte[] signature1 ;
    private byte[] signature2 ;
    
    @Before
    public void setUp() {
        signMsg = SignMsg.INSTANCE;
        verifyMsg = VerifyMsg.INSTANCE;
        signature1 =signMsg.signMessage(message1);
        signature2 =signMsg.signMessage(message2);
    }
    
    @Test
    public void testFunctionVerifyMessage_testCase1() {
        Assert.assertTrue(verifyMsg.verifyMessage(message1, signature1));
    }
    
    @Test
    public void testFunctionVerifyMessage_testCase2() {
        Assert.assertTrue(verifyMsg.verifyMessage(message2, signature2));
    }
}
