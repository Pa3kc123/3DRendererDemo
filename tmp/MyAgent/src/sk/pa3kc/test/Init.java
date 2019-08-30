package sk.pa3kc.test;

import java.lang.instrument.Instrumentation;

public class Init {
    public static void premain(String args, Instrumentation instrumentation) {
        instrumentation.addTransformer(new MyClassLogger());
    }
}
