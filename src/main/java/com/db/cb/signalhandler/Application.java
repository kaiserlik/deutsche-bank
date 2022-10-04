package com.db.cb.signalhandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

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

    public Application(){
        this.algo = new Algo();
    }

    @Override
    public void handleSignal(int signal) {

        switch (signal) {
            case 1:
                algo.setUp();
                algo.setAlgoParam(1, 60);
                algo.performCalc();
                algo.submitToMarket();
                break;

            case 2:
                algo.reverse();
                algo.setAlgoParam(1, 80);
                algo.submitToMarket();
                break;

            case 3:
                algo.setAlgoParam(1, 90);
                algo.setAlgoParam(2, 15);
                algo.performCalc();
                algo.submitToMarket();
                break;

            default:
                algo.cancelTrades();
                break;
        }
        algo.doAlgo();
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

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException{
//        Application app = new Application();
//        app.callMethod(Meth.setAlgoParam.name(), 100, 200);
    }
}