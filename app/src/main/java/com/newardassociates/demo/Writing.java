package com.newardassociates.demo;

import java.lang.classfile.*;
import java.lang.classfile.constantpool.*;
import java.lang.classfile.instruction.*;
import java.lang.constant.*;
import java.lang.reflect.*;

public class Writing {
    public static byte[] generateHelloWorld() {
        var CD_Hello = ClassDesc.of("com.newardassociates.demo.HelloWorld");
        var CD_String = ClassDesc.of("java.lang.String");
        var CD_System = ClassDesc.of("java.lang.System");
        var CD_PrintStream = ClassDesc.of("java.io.PrintStream");
        var MTD_void_void = MethodTypeDesc.of(ConstantDescs.CD_void);
        var MTD_void_String = MethodTypeDesc.of(ConstantDescs.CD_void, CD_String);
        var public_static = ClassFile.ACC_PUBLIC + ClassFile.ACC_STATIC;

        byte[] bytes = ClassFile.of().build(CD_Hello,
            clb -> clb
                    .withFlags(ClassFile.ACC_PUBLIC)
                    .withMethod(ConstantDescs.INIT_NAME, 
                                ConstantDescs.MTD_void,
                                ClassFile.ACC_PUBLIC,
                                mb -> mb.withCode(
                                        cob -> cob.aload(0)
                                                  .invokespecial(ConstantDescs.CD_Object,
                                                                 ConstantDescs.INIT_NAME, 
                                                                 ConstantDescs.MTD_void)
                                                  .return_()))
                    .withMethod("main", MTD_void_void, public_static,
                                mb -> mb.withCode(
                                        cob -> cob.getstatic(CD_System, "out", CD_PrintStream)
                                                    .ldc("Hello World")
                                                    .invokevirtual(CD_PrintStream, 
                                                        "println", MTD_void_String)
                                                    .return_())));
        return bytes;
    }

    public static void main(String... args) throws Exception {
        // Create a ClassLoader that uses this class' CL as a parent
        ByteArrayClassLoader bacl = new ByteArrayClassLoader(Writing.class.getClassLoader());

        // Load in our whipped-up class definition
        bacl.addClassData("com.newardassociates.demo.HelloWorld", generateHelloWorld());

        // Let's load it and run it
        Class<?> hw = bacl.loadClass("com.newardassociates.demo.HelloWorld");
        // Let's invoke main() reflectively
        Class<?> stringArray = args.getClass();
        Method main = hw.getDeclaredMethod("main");
        main.invoke(null);
    }
}
