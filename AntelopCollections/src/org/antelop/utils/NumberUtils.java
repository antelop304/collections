package org.antelop.utils;

public class NumberUtils {

	public static String formatNumber(long num) {
    	String source = "00000";
    	String result = "";
    	String numStr = num + "";
    	int idLength = numStr.length();
    	int sourceLength = source.length();
    	for (int i = sourceLength -1; i >= 0 ; i--) {
    		char sourceChat = source.charAt(i);
    		if(idLength > i) {
    			sourceChat = numStr.charAt(i);
    		}
    		result += sourceChat;
		}
    	
    	System.out.println(result);
    	return result;
    }
}
