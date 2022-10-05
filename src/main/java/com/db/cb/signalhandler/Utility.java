package com.db.cb.signalhandler;

import akka.japi.Pair;
import com.typesafe.config.ConfigValue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    public static String unwrapConfigValue(ConfigValue cv) {
        String t1 = cv.toString()
                .replaceFirst("Quoted|Unquoted", "")
                .replaceFirst("\\(", "")
                .replaceAll("\"", "");
        return t1.substring(0, t1.length() - 1);
    }

    public static Matcher checkParams(String methWithParams){
        Pattern p = Pattern.compile("\\((\\d+)\\|(\\d+)\\)");
        return p.matcher(methWithParams);
    }

    public static Pair<Integer, Integer> extractParams(Matcher m){
        return new Pair<>(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
    }

    public static int parseJson(String in) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(in);
        return Integer.parseInt(obj.get("signal").toString());
    }
}
