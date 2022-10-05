package com.db.cb.signalhandler;

import akka.japi.Pair;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is your teams code and should be changed as you see fit.
 */
class SignalService implements SignalHandler {
    private final Algo algo;
    private Map<Integer, ArrayList<String>> methMap;
    private Config conf = ConfigFactory
            .load("service-config.conf")
            .getConfig("signal-service");

    public SignalService(){
        this.algo = new Algo();
        this.methMap = new HashMap<>();
        initMethodMap();
    }

    @Override
    public void handleSignal(int signal) {
        try{
            for(String name: this.methMap.get(signal)){
                Matcher match = checkParams(name);
                if(match.find()){
                    Pair<Integer, Integer> kv = extractParams(match);
                    callMethod(name.split("\\(")[0], kv.first(), kv.second());
                }
                else callMethod(name);
            }
        }catch(NullPointerException e){
            List<String> methods = this.methMap.get(0);
            Arrays.stream(methods.toArray()).forEach(m -> {
                try {
                    callMethod(m.toString());
                } catch (InvocationTargetException|IllegalAccessException|InstantiationException|NoSuchMethodException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
        algo.doAlgo();
    }

    //use typesafe config later to set method names to integer
    private void initMethodMap(){
        Config methConf;
        System.out.println("size: " + conf.entrySet().size());
        for(int i = 0; i < conf.entrySet().size(); i++){
            ArrayList<String> methods = new ArrayList<>();
            methConf = conf.getConfig(String.valueOf(i));
            ArrayList<String> finalMethods = methods;
            methConf.getList("name")
                    .forEach(m ->
                            finalMethods.add(unwrapConfigValue(m)));
            this.methMap.put(i, finalMethods);
            methods = new ArrayList<>();
        }
    }

    private String unwrapConfigValue(ConfigValue cv){
        String t1 = cv.toString()
                .replaceFirst("Quoted|Unquoted","")
                .replaceFirst("\\(","")
                .replaceAll("\"","");
        return t1.substring(0, t1.length()-1);
    }

    private Matcher checkParams(String methWithParams){
        Pattern p = Pattern.compile("\\((\\d+)\\|(\\d+)\\)");
        return p.matcher(methWithParams);
    }

    private Pair<Integer, Integer> extractParams(Matcher m){
        return new Pair<>(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
    }

    private void callMethod(String name) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        Object inst = Algo.class.newInstance();
        Algo.class.getMethod(name).invoke(inst);
    }

    private void callMethod(String name, int p, int v) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Object inst = Algo.class.newInstance();
        Method meth = inst.getClass().getMethod(name, int.class, int.class);
        Parameter[] params = meth.getParameters();
        meth.invoke(inst, p, v);
    }
}