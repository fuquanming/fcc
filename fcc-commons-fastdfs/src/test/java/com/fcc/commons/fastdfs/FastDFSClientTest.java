package com.fcc.commons.fastdfs;

import java.io.File;

import org.junit.Test;

import com.fcc.commons.fastdfs.pool.ConnectionManager;

public class FastDFSClientTest extends FastDFSClient {

    @Test
    public void testUploadFile() {
        ConnectionManager.init();
        
        File file = new File("f:\\IMG_20151016_173252.jpg");
        System.out.println(FastDFSClient.uploadFile(file, file.getName()));
        
        ConnectionManager.destroy();
    }

}
