package com.db.cb.signalhandler;

import akka.japi.Pair;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

@RunWith(SpringRunner.class)
public class UtilityTest {

    SignalService ss = new SignalService();
    Config conf = ConfigFactory
            .load("service-config.conf")
            .getConfig("signal-service");
    ConfigValue cv = conf.getConfig(String.valueOf(1)).getList("name").get(2);
    ConfigValue cv2 = conf.getConfig(String.valueOf(1)).getList("name").get(1);

    public UtilityTest() throws InstantiationException, IllegalAccessException {
    }

    @Test
    public void testSignals() {
        //foreach signal, test if each method it is read correctly from config
        Set<String> l1 = new HashSet<>();
        l1.add("cancelTrades");
        Set<String> l2 = new HashSet<>();
        l2.add("setUp"); l2.add("setAlgoParam(1|60)"); l2.add("performCalc"); l2.add("submitToMarket");
        Set<String> l3 = new HashSet<>();
        l3.add("reverse"); l3.add("setAlgoParam(1|80)"); l3.add("submitToMarket");
        Set<String> l4 = new HashSet<>();
        l4.add("setAlgoParam(1|90)"); l4.add("setAlgoParam(2|15)"); l4.add("performCalc"); l4.add("submitToMarket");
        Set<String> l5 = new HashSet<>();
        l5.add("setUp"); l5.add("submitToMarket");
        ArrayList<Set> arr = new ArrayList<>();
        arr.add(l1); arr.add(l2); arr.add(l3); arr.add(l4); arr.add(l5);

        //check for null values
        for (int i = 0; i < conf.entrySet().size(); i++) {
            Config methConf = conf.getConfig(String.valueOf(i));
            methConf.getList("name")
                    .forEach(
                            m -> Assert.assertNotNull(m.render())
                    );
        }
        ArrayList<String> list;
        Config methConf;
        for (int i = 0; i < conf.entrySet().size(); i++) {
            list = new ArrayList<>();
            methConf = conf.getConfig(String.valueOf(i));

            for(ConfigValue o : methConf.getList("name")){
                list.add(Utility.unwrapConfigValue(o));
            }
            //if set is unchanged hence no more or less, then false
            Assert.assertFalse(arr.get(i).retainAll(list));
        }
    }

    @Test
    public void testUtilityCheckParams(){
        //params found for setAlgoParam(1|60)
        Assert.assertTrue(Utility.checkParams(cv2.render()).find());
        //params NOT found for performCalc
        Assert.assertFalse(Utility.checkParams(cv.render()).find());
    }
    @Test
    public void testUtilityExtractParams(){
        Matcher match = Utility.checkParams(cv2.render().trim());
        System.out.println("match: " + match.find());
        Pair<Integer, Integer> kv = Utility.extractParams(match);
        Pair<Integer,Integer> pair = new Pair<>(1,60);
        Assert.assertNotNull(kv);
        Assert.assertEquals(kv, pair);
    }
    @Test
    public void testUnwrapConfigValue(){
        String unwrapped = Utility.unwrapConfigValue(cv2);
        Assert.assertEquals("setAlgoParam(1|60)", unwrapped);
    }
}
