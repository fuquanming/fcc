package com.fcc.app.util;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public final class PropertyUtil {
	private static Properties properties;

	private PropertyUtil() {
	}

	static {
		try {
			if (properties == null) {
				properties = new Properties();
			}
			properties.clear();
			// 获取当前类加载器
			ClassLoader classLoader = PropertyUtil.class.getClassLoader();
			// 获取置于classpath路径下的deploy.properties文件信息
			URL url = classLoader.getResource("conf/jdbc.properties");
			// 加载配置文件信息
			properties.load(url.openStream());
//			Set<Object> keySet = properties.keySet();
			// 遍历配置文件信息
//			for (Object key : keySet) {
				//                System.out.println("[" + key + "]:[" + properties.get(key) + "]");
//			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
		}
	}

	public static String getValue(String key) {
		return properties.getProperty(key);
	}
	
	public static void main(String[] args) {
		System.out.println(PropertyUtil.getValue("jdbc.url"));
	}
}
