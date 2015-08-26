package com.miempresa.apppractica.utils;

public class StringUtils {

    public static String emptyValue(){
        return "";
    }

    public static String nullValue(){
        return "(null)";
    }

    public static boolean isNullOrEmpty(String s){
        return ((s == null) || ("".equals(s)));
    }

    public static boolean startsWith(String s, String prefix, boolean ignoreCase){
        if(s != null && prefix != null){
            if(ignoreCase){
                return s.toLowerCase().startsWith(prefix.toLowerCase());
            }
            else{
                return s.startsWith(prefix);
            }
        }
        return s==null && prefix==null;
    }

    public static boolean equals(String s1, String s2, boolean ignoreCase){
        if(s1 != null && s2 != null){
            if(ignoreCase){
                return s1.equalsIgnoreCase(s2);
            }
            else{
                return s1.equals(s2);
            }
        }
        else{
            return ((s1 == null && s2 == null)? true : false);
        }
    }

    public static String unquote(String s, String quote){
        if(!StringUtils.isNullOrEmpty(s) && !StringUtils.isNullOrEmpty(quote)){
            if(s.startsWith(quote) && s.endsWith(quote)){
                return s.substring(1, s.length()-quote.length());
            }
        }
        return s;
    }

    public static String quote(String s, String quote){
        if(!StringUtils.isNullOrEmpty(s) && !StringUtils.isNullOrEmpty(quote)){
            return quote.concat(s).concat(quote);
        }
        return s;
    }

    public static long parseLong(String value, long defaultValue){
        try{
            if(StringUtils.isNullOrEmpty(value)){
                return defaultValue;
            }
            return Long.parseLong(value);
        }
        catch(NumberFormatException e){
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static int parseInt(String value, int defaultValue){
        try{
            if(StringUtils.isNullOrEmpty(value)){
                return defaultValue;
            }
            return Integer.parseInt(value);
        }
        catch(NumberFormatException e){
            e.printStackTrace();
        }
        return defaultValue;
    }

}