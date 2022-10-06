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

/**
 * This is your teams code and should be changed as you see fit.
 */
class SignalService implements SignalHandler {
    private final Algo algo;
    private final Object inst;
    private Map<Integer, ArrayList<String>> methMap;
    private Config conf = ConfigFactory
            .load("service-config.conf")
            .getConfig("signal-service");

    public SignalService() throws InstantiationException, IllegalAccessException {
        this.algo = new Algo();
        this.inst = Algo.class.newInstance();
        this.methMap = new HashMap<>();
        initMethodMap();
    }

    @Override
    public void handleSignal(int signal) {
        try{
            for(String name: this.methMap.get(signal)){
                Matcher match = Utility.checkParams(name);
                if(match.find()){
                    Pair<Integer, Integer> kv = Utility.extractParams(match);
                    callMethod(name.split("\\(")[0], kv.first(), kv.second());
                }
                else callMethod(name);
            }
        }catch(NullPointerException ex){
            List<String> methods = this.methMap.get(0);
            Arrays.stream(methods.toArray()).forEach(m -> {
                try {
                    callMethod(m.toString());
                } catch (
                        InvocationTargetException
                        | IllegalAccessException
                        | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (NoSuchMethodException
                 | InvocationTargetException
                 | IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally{
            algo.doAlgo();
        }
    }

    private void initMethodMap(){
        Config methConf;
        ArrayList<String> methods;
        for(int i = 0; i < conf.entrySet().size(); i++){
            methods = new ArrayList<>();
            methConf = conf.getConfig(String.valueOf(i));

            for(ConfigValue cv: methConf.getList("name")){
                methods.add(Utility.unwrapConfigValue(cv).trim());
            }
            this.methMap.put(i, methods);
        }
    }

    private void callMethod(String name) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Algo.class.getMethod(name).invoke(this.inst);
    }

    private void callMethod(String name, int p, int v) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method meth = this.inst.getClass().getMethod(name, int.class, int.class);
        Parameter[] params = meth.getParameters();
        meth.invoke(inst, p, v);
    }
}