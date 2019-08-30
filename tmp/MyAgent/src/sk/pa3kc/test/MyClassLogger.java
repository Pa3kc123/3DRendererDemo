package sk.pa3kc.test;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.ProtectionDomain;

public class MyClassLogger implements ClassFileTransformer {
    @Override
    public byte[] transform(
        ClassLoader loader,
        String className,
        Class<?> classBeingRedefined,
        ProtectionDomain protectionDomain,
        byte[] classfileBuffer
    ) throws IllegalClassFormatException {
        try {
            System.out.println(className);
            //Path path = Paths.get(className + ".class");
            //Files.write(path, classfileBuffer);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        return classfileBuffer;
	}
}
