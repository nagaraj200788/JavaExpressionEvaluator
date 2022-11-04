package com.expression.evaluator;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberUtils {

    public static Number getNumber (  String num){
        boolean containsDecimal = num.contains(".");
        if(isValuesFitIntoPrimitiveTypes(num,containsDecimal))
        {
            if (isIntegerNumber(num)) {
                return Long.valueOf(num);
            } else if (isFloatingNumber(num)) {
                return Double.valueOf(num);
            }
        }else {
            if(containsDecimal)
            {
                return new BigDecimal(num);
            }else {
                return new BigInteger(num);
            }
        }
        return 0;
    }

    public static Number getNumber ( Number num1){
        String num=num1.toString();
        boolean containsDecimal = num.contains(".");
        if(isValuesFitIntoPrimitiveTypes(num,containsDecimal))
        {
            if (isIntegerNumber(num)) {
                return Long.valueOf(num);
            } else if (isFloatingNumber(num)) {
                return Double.valueOf(num);
            }
        }else {
            if(containsDecimal)
            {
                return new BigDecimal(num);
            }else {
                return new BigInteger(num);
            }
        }
        return 0;
    }
    public  static boolean isValuesFitIntoPrimitiveTypes(Number a, Number b) {
        if (a instanceof Double || b instanceof Double)
        {
            return isNumberLargerThan(a,Double.MAX_VALUE) && isNumberLargerThan(b,Double.MAX_VALUE);
        }else
        {
            return isNumberLargerThan(a,Long.MAX_VALUE) && isNumberLargerThan(b,Long.MAX_VALUE);
        }
    }
    public  static boolean isValuesFitIntoPrimitiveTypes(String a,Boolean containsDecimal) {
        if (containsDecimal)
        {
            return isNumberLargerThan(a,Double.MAX_VALUE);
        }else
        {
            return isNumberLargerThan(a,Long.MAX_VALUE);
        }
    }
    public  static boolean isNumberLargerThan(Number a, double maxValue) {
        return new BigDecimal(String.valueOf(a.toString())).compareTo(new BigDecimal(maxValue))!=1 ? true : false;
    }
    public  static boolean isNumberLargerThan(String a, double maxValue) {
        return new BigDecimal(String.valueOf(a)).compareTo(new BigDecimal(maxValue))!=1 ? true : false;
    }

    public  static boolean isFloatingNumber ( final String num){
        try {
            Double.valueOf(num);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    public  static boolean isIntegerNumber ( final String num){
        try {
            Long.valueOf(num);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }
}
