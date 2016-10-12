package com.fcc.commons.data;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Description:字符转化
 * </p>
 * <p>
 * Copyright:Copyright (c) 2009
 * </p>
 * 
 * @author 傅泉明
 * @version v1.0
 */
public class CharacterConversion {

	private Map<Character, String> charMap;
	private char[] changeChars;
	private String[] replaceStrs;

	public void init(char[] changeChars, String[] replaceStrs) {
		this.changeChars = changeChars;
		this.replaceStrs = replaceStrs;
		this.charMap = new HashMap<Character, String>();
		int length = changeChars.length;
		for (int i = 0; i < length; i++) {
			charMap.put(changeChars[i], replaceStrs[i]);
		}
	}
	
	public void put(char changeChar, String replaceStr) {
	    charMap.put(changeChar, replaceStr);
	}
	
	public void remove(char changeChar) {
	    charMap.remove(changeChar);
	}

	public String filter(String value) {
		if (value == null) {
			return null;
		}
		if (changeChars != null && replaceStrs != null) {
			StringBuffer result = new StringBuffer(value.length());
			for (int i = 0; i < value.length(); ++i) {
				char c = value.charAt(i);
				String str = charMap.get(c);
				if (str != null) {
					result.append(str);
				} else {
					result.append(c);
				}
			}
			return result.toString();
		}
		return null;
	}
//	switch(value.charAt(i)){
//	case '<':
//		result.append("&lt;");
//		break;
//	case '>':
//		result.append("&gt;");
//		break;
//	case '"':
//		result.append("&quot;");
//		break;
//	case '\'':
//		result.append("&#39;");
//		break;
//	case '%':
//		result.append("&#37;");
//		break;
////	case ';':
////		result.append("&#59;");
////		break;
//	case '(':
//		result.append("&#40;");
//		break;
//	case ')':
//		result.append("&#41;");
//		break;
//	case '&':
//		result.append("&amp;");
//		break;
//	case '+':
//		result.append("&#43;");
//		break;
//	default:
//		result.append(value.charAt(i));
//		break;
//	}
	public static void main(String[] args) {
		char[] changeChars = new char[]{'+','<'};
		String[] replaceStrs = new String[]{"&#43;","&lt;"};
		CharacterConversion cc = new CharacterConversion();
		cc.init(changeChars, replaceStrs);
		System.out.println(cc.filter("+<123"));
	}

}
