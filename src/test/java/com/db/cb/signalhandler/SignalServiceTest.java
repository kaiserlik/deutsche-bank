package com.db.cb.signalhandler;

import akka.japi.Pair;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import org.junit.Assert;
import org.junit.Test;

public class SignalServiceTest {
    SignalService ss = new SignalService();
    Utility utility;
    Config conf = ConfigFactory
            .load("service-config.conf")
            .getConfig("signal-service");
    ConfigValue cv = conf.getConfig(String.valueOf(1)).getList("name").get(2);
    ConfigValue cv2 = conf.getConfig(String.valueOf(1)).getList("name").get(1);

    public SignalServiceTest() throws InstantiationException, IllegalAccessException {
    }

    @Test
    public void testSignals() {
        for (int i = 0; i < conf.entrySet().size(); i++) {
            Config methConf = conf.getConfig(String.valueOf(i));
            methConf.getList("name")
                    .forEach(
                            m -> Assert.assertNotNull(m.render())
                    );
        }
    }
    @Test
    public void testUtilityCheckParams(){
        Assert.assertTrue(Utility.checkParams(cv2.render()).find());
        Assert.assertFalse(Utility.checkParams(cv.render()).find());
    }
    @Test
    public void testUtilityExtractParams(){
        Pair<Integer,Integer> pair = new Pair<>(1,60);
    }
}
