/*
 * @(#)CoderEnum.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons
 * 创建日期 : 2016年12月20日
 * 修改历史 : 
 *     1. [2016年12月20日]创建文件 by 傅泉明
 */
package com.fcc.commons.coder;

/**
 * 编码器类型
 * @version 
 * @author 傅泉明
 */
public enum CoderEnum {
    /**
     * 双向加密:对称加密
     * DES算法为密码体制中的对称密码体制，又被成为美国数据加密标准，是1972年美国IBM公司研制的对称密码体制加密算法。
     *  明文按64位进行分组, 密钥长64位，密钥事实上是56位参与DES运算（第8、16、24、32、40、48、56、64位是校验位， 使得每个密钥都有奇数个1）分组后的明文组和56位的密钥按位替代或交换的方法形成密文组的加密方法
     */
    DES("DES"),
    
    /**
     * 双向加密:对称加密
     * 3DES又称Triple DES，是DES加密算法的一种模式，它使用3条56位的密钥对3DES 数据进行三次加密。
     * 数据加密标准（DES）是美国的一种由来已久的加密标准，它使用对称密钥加密法，并于1981年被ANSI组织规范为ANSI X.3.92。DES使用56位密钥和密码块的方法，而在密码块的方法中，文本被分成64位大小的文本块然后再进行加密。比起最初的DES，3DES更为安全。 　　 
     * 3DES（即Triple DES）是DES向AES过渡的加密算法（1999年，NIST将3-DES指定为过渡的加密标准），是DES的一个更安全的变形。
     * 它以DES为基本模块，通过组合分组方法设计出分组加密算法，其具体实现如下： 
     * 设Ek()和Dk()代表DES算法的加密和解密过程，K代表DES算法使用的密钥，P代表明文，C代表密文， 
     * 这样， 　　 
     * 3DES加密过程为：C=Ek3(Dk2(Ek1(P))) 
     * 3DES解密过程为：P=Dk1((EK2(Dk3(C))) 
     */
    DES3("DESede"),
    
    /**
     * 双向加密:对称加密
     * AES密码学中的高级加密标准（Advanced Encryption Standard，AES），又称  高级加密标准 Rijndael加密法，是美国联邦政府采用的一种区块加密标准。
     * 这个标准用来替代原先的DES，已经被多方分析且广为全世界所使用。经过五年的甄选流程，高级加密标准由美国国家标准与技术研究院（NIST）于2001年11月26日发布于FIPS PUB 197，并在2002年5月26日成为有效的标准。
     * 2006年，高级加密标准已然成为对称密钥加密中最流行的算法之一。 　　
     * 该算法为比利时密码学家Joan Daemen和Vincent Rijmen所设计，结合两位作者的名字，以Rijndael之命名之，投稿高级加密标准的甄选流程
     */
    AES("AES"),
    
    /**
     * 双向加密:非对称加密
     * RSA 公钥加密算法是1977年由Ron Rivest、Adi Shamirh和LenAdleman在（美国麻省理工学院）开发的。
     * RSA取名来自开发他们三者的名字。RSA是目前最有影响力的公钥加密算法，它能够抵抗到目前为止已知的所有密码攻击，已被ISO推荐为公钥数据加密标准。
     * RSA算法基于一个十分简单的数论事实：将两个大素数相乘十分容易，但那时想要对其乘积进行因式分解却极其困难，因此可以将乘积公开作为加密密钥。
     */
    RSA("RSA"),
    
    /**
     * 双向加密:非对称加密
     * DSA-Digital Signature Algorithm 是Schnorr和ElGamal签名算法的变种，被美国NIST作为DSS(DigitalSignature Standard)。
     * 简单的说，这是一种更高级的验证方式，用作数字签名。不单单只有公钥、私钥，还有数字签名。私钥加密生成数字签名，公钥验证数据及签名。
     * 如果数据和签名不匹配则认为验证失败！也就是说传输中的数据可以不再加密，接收方获得数据后，拿到公钥与签名比对数据是否有效！
     */
    DSA("DSA");

    private final String info;

    private CoderEnum(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
