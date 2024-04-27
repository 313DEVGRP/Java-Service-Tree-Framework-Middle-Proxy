package com.arms.util.redisrepo.util;

public class KeyName {

    private final String keyName;

    public KeyName(String keyName) {
        if(!keyName.contains(":")){
            throw new IllegalArgumentException("잘못된 키 검색");
        }
        this.keyName = keyName.split(":")[1];
    }

    public String get() {
        return keyName;
    }

}
