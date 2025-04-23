package com.newardassociates.demo;

import java.util.*;

public class ByteArrayClassLoader extends ClassLoader {
    private Map<String, byte[]> classDataMap = new HashMap<>();

    public ByteArrayClassLoader(ClassLoader parent, Map<String, byte[]> classDataMap) {
        super(parent);
        this.classDataMap = classDataMap;
    }
    public ByteArrayClassLoader(ClassLoader parent) {
        super(parent);
        this.classDataMap = new HashMap<>();
    }

    public void addClassData(String className, byte[] classData) {
        classDataMap.put(className, classData);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (classDataMap.get(name) == null) {
            throw new ClassNotFoundException("Class not found: " + name);
        }
    
        byte[] classData = classDataMap.get(name);
        return defineClass(name, classData, 0, classData.length);
    }    
}
