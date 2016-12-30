package com.fcc.commons.http.client.methods.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.fcc.commons.http.client.methods.Response;

public class HttpGetTest {

    @Test
    public void testHttpGet() {
        //        fail("Not yet implemented");
        Pattern divStartPattern = Pattern.compile(".*<div class=\"alist\">.*");
        Pattern divEndPattern = Pattern.compile(".*</div>.*");
        Pattern aPattern = Pattern.compile("<a title=\"(.*)\" target=\"(.*)\" href=\"(.*)\".*<span>(.*)</span>.*");

        String url = "http://us.mofcom.gov.cn/article/jmxw/";
        HttpGet request = new HttpGet(url);
        Response response = request.execute();
        List<String> divList = new ArrayList<String>();
        List<String> aList = new ArrayList<String>();
        boolean startFlag = false;
        boolean endFlag = false;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int checkYear = 2016;
        int checkMonth = 10;
        int checkDay = 11;

        if (response.getStatusLine().contains(String.valueOf(HttpURLConnection.HTTP_OK))) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(response.getInputStream(), "UTF-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
//                    System.out.println(line);
                    if (startFlag == false) {
                        Matcher divMatcher = divStartPattern.matcher(line);
                        if (divMatcher.find()) {
                            divList.add(line);
                            startFlag = true;
                        }
                    } else if (endFlag == false) {
                        Matcher divEndMatcher = divEndPattern.matcher(line);
                        if (divEndMatcher.find()) {
                            endFlag = true;
                        }

                        Matcher aMatcher = aPattern.matcher(line);
                        if (aMatcher.find()) {
                            String time = aMatcher.group(4);
                            String aurl = aMatcher.group(3);
                            Calendar now = Calendar.getInstance();
                            int year = now.get(Calendar.YEAR);
                            int month = now.get(Calendar.MONTH);
                            int day = now.get(Calendar.DAY_OF_MONTH);

                            now.setTime(format.parse(time));
                            if (now.get(Calendar.YEAR) == checkYear && now.get(Calendar.MONTH) == checkMonth
                                    && now.get(Calendar.DAY_OF_MONTH) == checkDay) {
                                aList.add(aurl);
                                aList.add(time);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(br);
            }
        }

        System.out.println("--------------------");
        for (String str : divList) {
            System.out.println(str);
        }
        System.out.println("--------------------");
        for (String str : aList) {
            System.out.println(str);
        }
        request.close();
        
        
        
        Pattern titlePattern = Pattern.compile("<h2 id=\"artitle\" align=\"center\">(.*)</h2>.*");
        Pattern contentStartPattern = Pattern.compile(".*<div id=\"zoom\" class=\"cont\">.*");
        Pattern contentPattern = Pattern.compile(".*<p>(.*)</p>.*", Pattern.CASE_INSENSITIVE);

        boolean titleFlag = false;
        boolean contentFlag = false;
        String title = null;
        String content = null;
        url = "http://us.mofcom.gov.cn";
        request = new HttpGet(url + aList.get(0));
        response = request.execute();
        if (response.getStatusLine().contains(String.valueOf(HttpURLConnection.HTTP_OK))) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(response.getInputStream(), "UTF-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    if (titleFlag == false) {
                        Matcher titleMatcher = titlePattern.matcher(line);
                        if (titleMatcher.find()) {
                            title = titleMatcher.group(1);
                            titleFlag = true;
                        }
                    } else if (contentFlag == false) {
                        Matcher contentStartMatcher = contentStartPattern.matcher(line);
                        if (contentStartMatcher.find()) {
                            contentFlag = true;
                        }
                    } else if (contentFlag == true && content == null) {
                        Matcher contentMatcher = contentPattern.matcher(line);
                        if (contentMatcher.find()) {
                            content = contentMatcher.group(1);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(br);
            }
        }
        request.close();
        
        System.out.println("-----------------");
        System.out.println("title=" + title);
        System.out.println("-----------------");
        System.out.println("content=" + content);
    }
}
