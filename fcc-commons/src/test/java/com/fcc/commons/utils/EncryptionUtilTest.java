package com.fcc.commons.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class EncryptionUtilTest {

    @Test
    public void test() {
        System.out.println(EncryptionUtil.encodeMD5("123456"));
    }

}
