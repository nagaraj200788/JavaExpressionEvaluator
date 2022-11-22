package oracle.expression.evaluator;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberUtils {

    public static Number getNumber (  String num){
        boolean containsDecimal = num.contains(".");//!(num.indexOf(".")<0);
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
        if(a instanceof BigDecimal || a instanceof BigInteger || b instanceof BigDecimal || b instanceof BigInteger){
            return false;
        }else if (a instanceof Double || b instanceof Double)
        {
            return !isNumberLargerThan(a.toString(),Double.MAX_VALUE) || !isNumberLargerThan(b.toString(),Double.MAX_VALUE);
        }else
        {
            return !isNumberLargerThanLongMax(a.toString(),Long.MAX_VALUE) || isNumberLargerThanLongMax(b.toString(),Long.MAX_VALUE);
        }
    }


    public  static boolean isValuesFitIntoPrimitiveTypes(String a,Boolean containsDecimal) {
        if (containsDecimal)
        {
            return !isNumberLargerThan(a,Double.MAX_VALUE);
        }else
        {
            return !isNumberLargerThanLongMax(a,Long.MAX_VALUE);
        }
    }
    public  static boolean isNumberLargerThan(String a, double maxValue) {
        return new BigDecimal(String.valueOf(a)).compareTo(new BigDecimal(maxValue))==1 ? true : false;
    }
    public  static boolean isNumberLargerThanLongMax(String a, long maxValue) {
        return new BigInteger(String.valueOf(a)).compareTo(new BigInteger(String.valueOf(maxValue)))==1 ? true : false;
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
