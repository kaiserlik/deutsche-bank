package com.db.cb.signalhandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * This is your teams code and should be changed as you see fit.
 */
class Application implements SignalHandler {
    private final Algo algo;
    private enum Meth{
        reverse,
        setUp,
        setAlgoParam,
        submitToMarket,
        doAlgo,
        performCalc,
        cancelTrades
    }
    private Map<Integer, ArrayList<String>> methMap;

    public Application(){
        this.algo = new Algo();
        this.methMap = new HashMap<>();
        initMethodMap();
    }

    @Override
    public void handleSignal(int signal) {
        try{
            for(String name: this.methMap.get(signal)){
                //make into switch (signal, isSetAlgoParam(name)) case (true, 1):
                if(isSetAlgoParam(name) && signal == 1)
                    callMethod(name, 1, 60);
                else if(isSetAlgoParam(name) && signal == 2)
                    callMethod(name, 1, 80);
                else if(isSetAlgoParam(name) && signal == 3) {
                    callMethod(name, 1, 90);
                    callMethod(name, 2, 15);
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

    private boolean isSetAlgoParam(String name){
        return name.equals(Meth.setAlgoParam.name());
    }

    //use typesafe config later to set method names to integer
    private void initMethodMap(){
        ArrayList<String> methods = new ArrayList<>();

        //default
        methods.add(Meth.cancelTrades.name());
        this.methMap.put(0, methods);
        methods = new ArrayList<>();

        methods.add(Meth.setUp.name());
        methods.add(Meth.setAlgoParam.name());
        methods.add(Meth.performCalc.name());
        methods.add(Meth.submitToMarket.name());
        this.methMap.put(1, methods);
        methods = new ArrayList<>();

        methods.add(Meth.reverse.name());
        //algo.setAlgoParam(1, 80); need to set parameter values later
        methods.add(Meth.setAlgoParam.name()); //(1, 80);
        methods.add(Meth.submitToMarket.name());
        this.methMap.put(2, methods);
        methods = new ArrayList<>();

        //algo.setAlgoParam(1, 90);
        //algo.setAlgoParam(2, 15);
        methods.add(Meth.setAlgoParam.name());
        methods.add(Meth.setAlgoParam.name());
        methods.add(Meth.performCalc.name());
        methods.add(Meth.submitToMarket.name());
        this.methMap.put(3, methods);
        //default be if signal = null
    }

    private void callMethod(String name) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        Object inst = Algo.class.newInstance();
        Algo.class.getMethod(name).invoke(inst);
    }
    /***
     * @param name : method name
     * @param p : parameter
     * @param v : value
     */
    private void callMethod(String name, int p, int v) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Object inst = Algo.class.newInstance();
        Method meth = inst.getClass().getMethod(name, int.class, int.class);
        Parameter[] params = meth.getParameters();
        meth.invoke(inst, p, v);
    }

//    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException{
//        Application app = new Application();
////        app.callMethod(Meth.setAlgoParam.name(), 100, 200);
//        app.methMap.forEach((k,v) -> System.out.println(k + " : " + v.toString()));
//        System.out.println("-----------------");
//        app.handleSignal(1);
//        System.out.println("-----------------");
//        app.handleSignal(2);
//        System.out.println("-----------------");
//        app.handleSignal(3);
//
//    }
}