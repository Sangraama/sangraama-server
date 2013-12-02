package org.sangraama.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sangraama.util.SignMsg;

public class TestSignMsg {

    private SignMsg signMsg;
    private byte[] signature1 ;
    private String message1 = "{\"positionX\":156.26292,\"positionY\":11.90625,\"health\":100.0,\"score\":0.0,\"url\":\"localhost:8081/sangraama/sangraama/player\",\"pt\":1}";
    private byte[] signature2;
    private String message2 = "{\"positionX\":43.64371,\"positionY\":123.32567,\"health\":100.0,\"score\":10.0,\"url\":\"localhost:8080/sangraama/sangraama/player\",\"pt\":1}";
    
    @Before
    public void setUp(){
        signMsg = SignMsg.INSTANCE;
        signature1 = signMsg.signMessage(message1);
        signature2 = signMsg.signMessage(message2);
    }
    
    @Test
    public void testFunctionSignMessage_testCase1() {
        Assert.assertTrue(signature1!=null);
    }
    
    @Test
    public void testFunctionSignMessage_testCase2() {
        Assert.assertTrue(signature2!=null);
    }
}
