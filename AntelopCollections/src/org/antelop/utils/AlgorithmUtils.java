package org.antelop.utils;

import java.math.BigDecimal;

/**
 * 算法运算
 */
public class AlgorithmUtils
{
    /**
     * 操作符
     * 
     * @author antelop Creation Time： 2014年12月29日 下午2:27:37
     * @version
     */
    private enum OpType {
        Add, Subtract, Multiply, Divide
    }

    /** 除法运算辅助精度 */
    private final static int DEFAULT_NEWSCALE = 20;
    /** 默认四舍五入 */
    private final static int DEFAULT_ROUNDINGMODE = BigDecimal.ROUND_HALF_UP;

    /**
     * 加
     * 
     * @param a1
     * @param a2
     * @param a
     * @return a1+a2+a3+a4...
     */
    public static double add(double a1, double a2, double... a)
    {
        return calculate(OpType.Add, a1, a2, a);
    }

    /**
     * 减
     * 
     * @param a1
     * @param a2
     * @param a
     * @return a1-a2-a3-a4...
     */
    public static double subtract(double a1, double a2, double... a)
    {
        return calculate(OpType.Subtract, a1, a2, a);
    }

    /**
     * 乘
     * 
     * @param a1
     * @param a2
     * @param a
     * @return a1*a2*a3*a4...
     */
    public static double multiply(double a1, double a2, double... a)
    {
        return calculate(OpType.Multiply, a1, a2, a);
    }

    /**
     * 乘
     * 
     * @param newScale
     *            精度(默认四舍五入)
     * @param a1
     *            被乘数
     * @param a2
     *            乘数
     * @param a
     *            乘数
     * @return a1*a2*a3*a4...
     */
    public static double multiply(
        String newScale,
        double a1,
        double a2,
        double... a)
    {
        return calculate(
            Integer.parseInt(newScale), DEFAULT_ROUNDINGMODE,
            OpType.Multiply, a1, a2, a);
    }

    /**
     * 乘
     * 
     * @param roundingMode
     *            取精度规则(和BigDecimal 中的规则roundingMode一致)
     * @param newScale
     *            精度
     * @param a1
     *            被乘数
     * @param a2
     *            乘数
     * @param a
     *            乘数
     * @return a1*a2*a3*a4...
     */
    public static double multiply(
        int roundingMode, String newScale, double a1, double a2, double... a)
    {
        return calculate(
            Integer.parseInt(newScale), roundingMode, OpType.Multiply, a1, a2,
            a);
    }

    /**
     * 除
     * 
     * @param newScale
     *            精度(默认四舍五入)
     * @param a1
     *            被除数
     * @param a2
     *            除数
     * @param a
     *            除数
     * @return a1/a2/a3/a4...
     */
    public static double divide(
        String newScale,
        double a1,
        double a2,
        double... a)
    {
        return calculate(
            Integer.parseInt(newScale), DEFAULT_ROUNDINGMODE,
            OpType.Divide, a1, a2, a);
    }

    /**
     * 
     * @param roundingMode
     *            取精度规则(和BigDecimal 中的规则roundingMode一致)
     * @param newScale
     *            精度
     * @param a1
     *            被除数
     * @param a2
     *            除数
     * @param a
     *            除数
     * @return a1/a2/a3/a4...
     */
    public static double divide(
        int roundingMode,
        String newScale,
        double a1,
        double a2,
        double... a)
    {
        return calculate(
            Integer.parseInt(newScale), roundingMode, OpType.Divide, a1, a2, a);
    }

    private static double calculate(
        OpType opType,
        double a1,
        double a2,
        double... a)
    {
        BigDecimal b1 = new BigDecimal(a1 + "");
        BigDecimal b2 = new BigDecimal(a2 + "");
        if (OpType.Add.equals(opType)) {
            BigDecimal tmp = b1.add(b2);
            for (int i = 0; i < a.length; i++) {
                tmp = new BigDecimal(a[i] + "").add(tmp);
            }
            return tmp.doubleValue();
        }
        else if (OpType.Subtract.equals(opType)) {
            BigDecimal tmp = b1.subtract(b2);
            for (int i = 0; i < a.length; i++) {
                tmp = tmp.subtract(new BigDecimal(a[i] + ""));
            }
            return tmp.doubleValue();
        }
        else if (OpType.Multiply.equals(opType)) {
            BigDecimal tmp = b1.multiply(b2);
            for (int i = 0; i < a.length; i++) {
                tmp = tmp.multiply(new BigDecimal(a[i] + ""));
            }
            return tmp.doubleValue();
        }

        throw new RuntimeException("不支持该运算方法.");
    }

    private static double calculate(
        int newScale,
        int roundingMode,
        OpType opType,
        double a1,
        double a2,
        double... a)
    {
        BigDecimal b1 = new BigDecimal(a1 + "");
        BigDecimal b2 = new BigDecimal(a2 + "");
        BigDecimal tmp;
        if (OpType.Add.equals(opType)) {
            tmp = b1.add(b2);
            for (int i = 0; i < a.length; i++) {
                tmp = new BigDecimal(a[i] + "").add(tmp);
            }
        }
        else if (OpType.Multiply.equals(opType)) {
            tmp = b1.multiply(b2);
            for (int i = 0; i < a.length; i++) {
                tmp = tmp.multiply(new BigDecimal(a[i] + ""));
            }
        }
        else {
            tmp = b1.divide(b2, DEFAULT_NEWSCALE, roundingMode);
            for (int i = 0; i < a.length; i++) {
                tmp = tmp.divide(
                    new BigDecimal(a[i] + ""), DEFAULT_NEWSCALE, roundingMode);
            }
        }

        return tmp.setScale(newScale, roundingMode).doubleValue();
    }

    public static void main(String[] args)
    {
        System.out.println("-----加-----");
        System.out.println(add(1, 2));
        System.out.println(add(1, 2, 3, 4));

        System.out.println("-----减-----");
        System.out.println(subtract(1, 2));
        System.out.println(subtract(10, 2, 3, 4));

        System.out.println("-----乘-----");
        System.out.println(multiply(1, 2));
        System.out.println(multiply(10, 2, 3, 4));
        System.out.println(multiply("3", 2, 3, 4));
        System.out.println(multiply(BigDecimal.ROUND_UP, "3", 2, 3, 4));

        System.out.println("-----除-----");
        System.out.println(divide("2", 2, 2));
        System.out.println(divide("2", 1, 2, 2, 2, 3.5));
        System.out.println(divide(BigDecimal.ROUND_DOWN, "2", 1, 2, 2, 2, 3.5));
    }
}
