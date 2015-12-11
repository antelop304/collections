package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CreateSql
{
    String sql_Plan = 
        "SELECT T2.* "
            +"  FROM ( "
//                    --包租车驾驶员1
            +"        SELECT  "
            +"                T1.F_NAME AS F_DRIVERNAME, "
            +"                T.F_LISENCE, "
            +"                T.F_DATE AS WORKDAY, "
            +"                T.F_DRIVERIDCARD1 as F_DRIVERIDCARD, "
            +"                T1.F_JOINEDTIME, "
            +"                1 AS F_TOTALTIMES, "
            +"                ((CASE WHEN T.F_LEN1 IS NOT NULL THEN F_LEN1 ELSE 0 END)  "
            +"                  + (CASE WHEN T.F_LEN2 IS NOT NULL THEN F_LEN2 ELSE 0 END)  "
            +"                  + (CASE WHEN T.F_LEN3 IS NOT NULL THEN F_LEN3 ELSE 0 END)  "
            +"                  + (CASE WHEN T.F_LEN4 IS NOT NULL THEN F_LEN4 ELSE 0 END)  "
            +"                  + (CASE WHEN T.F_LEN5 IS NOT NULL THEN F_LEN5 ELSE 0 END)  "
            +"                  + (CASE WHEN T.F_LEN6 IS NOT NULL THEN F_LEN6 ELSE 0 END)  "
            +"                  + (CASE WHEN T. F_NOUSELEN IS NOT NULL THEN F_NOUSELEN ELSE 0 END) "
            +"                 ) AS F_TOTALLEN "
            +"          FROM T_PLAN T, T_DRIVER T1 "
            +"         WHERE T1.F_TEAM = '01' "
            +"           AND T.F_DATE BETWEEN TO_DATE('2015-06-21', 'YYYY-MM-DD') AND TO_DATE('2015-07-20', 'YYYY-MM-DD') "
            +"           AND T.f_driveridcard1 = T1.F_IDCARD "
            +"           and t.f_driveridcard1 is not null "
            +"           and t.f_driveridcard2 is null "
            +"           and t.f_isrental = 2 "
            +"         "
//                    --包租车驾驶员2 
            +"        UNION ALL "
            +"        SELECT  "
            +"               T1.F_NAME AS F_DRIVERNAME, "
            +"               T.F_LISENCE, "
            +"               T.F_DATE AS WORKDAY, "
            +"               T.f_driveridcard2 as F_DRIVERIDCARD, "
            +"               T1.F_JOINEDTIME, "
            +"               1 AS F_TOTALTIMES, "
            +"               ((CASE WHEN T.F_LEN1 IS NOT NULL THEN F_LEN1 ELSE 0 END)  "
            +"                  + (CASE WHEN T.F_LEN2 IS NOT NULL THEN F_LEN2 ELSE 0 END)  "
            +"                  + (CASE WHEN T.F_LEN3 IS NOT NULL THEN F_LEN3 ELSE 0 END)  "
            +"                  + (CASE WHEN T.F_LEN4 IS NOT NULL THEN F_LEN4 ELSE 0 END)  "
            +"                  + (CASE WHEN T.F_LEN5 IS NOT NULL THEN F_LEN5 ELSE 0 END)  "
            +"                  + (CASE WHEN T.F_LEN6 IS NOT NULL THEN F_LEN6 ELSE 0 END)  "
            +"                  + (CASE WHEN T. F_NOUSELEN IS NOT NULL THEN F_NOUSELEN ELSE 0 END) "
            +"                 ) AS F_TOTALLEN "
            +"          FROM T_PLAN T, T_DRIVER T1, T_SALARYCARTYPE TS "
            +"         WHERE T1.F_TEAM = '01' "
            +"           AND T.F_DATE BETWEEN TO_DATE('2015-06-21', 'YYYY-MM-DD') AND TO_DATE('2015-07-20', 'YYYY-MM-DD') "
            +"           AND T.f_driveridcard2 = T1.F_IDCARD "
            +"           and t.f_driveridcard2 is not null "
            +"           and t.f_driveridcard1 is null "
            +"           and t.f_isrental = 2 "
            +"         "
//                    --包租车驾驶员1(驾驶员1和驾驶员2)
            +"        UNION ALL "
            +"        SELECT  "
            +"               T1.F_NAME AS F_DRIVERNAME, "
            +"               T.F_LISENCE, "
            +"               T.F_DATE AS WORKDAY, "
            +"               T.f_driveridcard1 as F_DRIVERIDCARD, "
            +"               T1.F_JOINEDTIME, "
            +"               0.5 AS F_TOTALTIMES, "
            +"               ((CASE WHEN T.F_LEN1 IS NOT NULL THEN F_LEN1 ELSE 0 END)  "
            +"                  + (CASE WHEN T.F_LEN2 IS NOT NULL THEN F_LEN2 ELSE 0 END)  "
            +"                  + (CASE WHEN T.F_LEN3 IS NOT NULL THEN F_LEN3 ELSE 0 END)  "
            +"                  + (CASE WHEN T.F_LEN4 IS NOT NULL THEN F_LEN4 ELSE 0 END)  "
            +"                  + (CASE WHEN T.F_LEN5 IS NOT NULL THEN F_LEN5 ELSE 0 END)  "
            +"                  + (CASE WHEN T.F_LEN6 IS NOT NULL THEN F_LEN6 ELSE 0 END)  "
            +"                  + (CASE WHEN T. F_NOUSELEN IS NOT NULL THEN F_NOUSELEN ELSE 0 END)) / 2 as F_TOTALLEN "
            +"          FROM T_PLAN T, T_DRIVER T1 "
            +"         WHERE T1.F_TEAM = '01' "
            +"           AND T.F_DATE BETWEEN TO_DATE('2015-06-21', 'YYYY-MM-DD') AND TO_DATE('2015-07-20', 'YYYY-MM-DD') "
            +"           AND T.f_driveridcard1 = T1.F_IDCARD "
            +"           and t.f_driveridcard1 is not null "
            +"           and t.f_driveridcard2 is not null "
            +"           and t.f_isrental = 2 "
            +"         "
//                    --包租车驾驶员2(驾驶员1和驾驶员2)
            +"        UNION ALL "
            +"        SELECT  "
            +"               T1.F_NAME AS F_DRIVERNAME, "
            +"               T.F_LISENCE, "
            +"               T.F_DATE AS WORKDAY, "
            +"               T.f_driveridcard2 as F_DRIVERIDCARD, "
            +"               T1.F_JOINEDTIME, "
            +"               0.5 AS F_TOTALTIMES, "
            +"               ((CASE WHEN T.F_LEN1 IS NOT NULL THEN F_LEN1 ELSE 0 END)  "
            +"                  + (CASE WHEN T.F_LEN2 IS NOT NULL THEN F_LEN2 ELSE 0 END)  "
            +"                  + (CASE WHEN T.F_LEN3 IS NOT NULL THEN F_LEN3 ELSE 0 END)  "
            +"                  + (CASE WHEN T.F_LEN4 IS NOT NULL THEN F_LEN4 ELSE 0 END)  "
            +"                  + (CASE WHEN T.F_LEN5 IS NOT NULL THEN F_LEN5 ELSE 0 END)  "
            +"                  + (CASE WHEN T.F_LEN6 IS NOT NULL THEN F_LEN6 ELSE 0 END)  "
            +"                  + (CASE WHEN T. F_NOUSELEN IS NOT NULL THEN F_NOUSELEN ELSE 0 END)) / 2 as F_TOTALLEN "
            +"          FROM T_PLAN T, T_DRIVER T1 "
            +"         WHERE T1.F_TEAM = '01' "
            +"           AND T.F_DATE BETWEEN TO_DATE('2015-06-21', 'YYYY-MM-DD') AND TO_DATE('2015-07-20', 'YYYY-MM-DD') "
            +"           AND T.f_driveridcard2 = T1.F_IDCARD "
            +"           and t.f_driveridcard1 is not null "
            +"           and t.f_driveridcard2 is not null "
            +"           and t.f_isrental = 2) T2; ";

    
    
    
    
    String sql_CopyPlan = 
        "SELECT T2.*   "
            +"FROM (      "
            +"             "
            //--非包租车驾驶员1          
            +"         SELECT  "
            +"             T1.F_NAME AS F_DRIVERNAME,     "
            +"             T.F_CL as F_LISENCE,    "
            +"             T.F_DATE AS WORKDAY,   "
            +"             T.f_driveridcard1 as F_DRIVERIDCARD,   "
            +"             T1.F_JOINEDTIME as F_JOINEDTIME,       "
            +"             1 AS F_TOTALTIMES,   "
            +"             to_number(T.F_DDJL) AS F_TOTALLEN "
            +"        FROM T_COPYPLAN T, T_DRIVER T1    "
            +"       WHERE T1.F_TEAM ='01'   "
            +"         AND T.F_DATE BETWEEN TO_DATE('2015-06-21','YYYY-MM-DD')  AND TO_DATE('2015-07-20','YYYY-MM-DD')       "
            +"         AND T.f_driveridcard1 = T1.F_IDCARD    "
            +"         and t.f_driveridcard1 is not null      "
            +"         and t.f_driveridcard2 is null      "
            +"         and t.f_isrental=1      "
            +"                "
            //--非包租车驾驶员2  
            +"     UNION ALL          "
            +"         SELECT    "
            +"             T1.F_NAME AS F_DRIVERNAME,     "
            +"             T.F_CL as F_LISENCE,    "
            +"             T.F_DATE AS WORKDAY,   "
            +"             T.f_driveridcard1 as F_DRIVERIDCARD,   "
            +"             T1.F_JOINEDTIME as F_JOINEDTIME,        "
            +"             1 AS F_TOTALTIMES,   "
            +"             to_number(T.F_DDJL) AS F_TOTALLEN "
            +"        FROM T_COPYPLAN T, T_DRIVER T1     "
            +"       WHERE T1.F_TEAM ='01'     "
            +"        AND T.F_DATE BETWEEN TO_DATE('2015-06-21','YYYY-MM-DD')  AND TO_DATE('2015-07-20','YYYY-MM-DD')       "
            +"        AND T.f_driveridcard2 = T1.F_IDCARD   "
            +"        and t.f_driveridcard2 is not null      "
            +"        and t.f_driveridcard1 is null      "
            +"         and t.f_isrental=1      "
            +"               "
            //--非包租车驾驶员1(驾驶员1和驾驶员2)
            +"     UNION ALL          "
            +"         SELECT   "
            +"             T1.F_NAME AS F_DRIVERNAME,     "
            +"             T.F_CL as F_LISENCE,    "
            +"             T.F_DATE AS WORKDAY,   "
            +"             T.f_driveridcard1 as F_DRIVERIDCARD,   "
            +"             T1.F_JOINEDTIME as F_JOINEDTIME,         "
            +"             1 AS F_TOTALTIMES,   "
            +"             to_number(T.F_DDJL) AS F_TOTALLEN "
            +"        FROM T_COPYPLAN T, T_DRIVER T1      "
            +"       WHERE T1.F_TEAM ='01'      "
            +"         AND T.F_DATE BETWEEN TO_DATE('2015-06-21','YYYY-MM-DD')  AND TO_DATE('2015-07-20','YYYY-MM-DD')       "
            +"         AND T.f_driveridcard1 = T1.F_IDCARD    "
            +"         and t.f_driveridcard1 is not null      "
            +"         and t.f_driveridcard2 is not null      "
            +"         and t.f_isrental=1      "
            +"             "
            //--非包租车驾驶员2(驾驶员1和驾驶员2)
            +"     UNION ALL          "
            +"         SELECT    "
            +"             T1.F_NAME AS F_DRIVERNAME,     "
            +"             T.F_CL as F_LISENCE,    "
            +"             T.F_DATE AS WORKDAY,   "
            +"             T.f_driveridcard2 as F_DRIVERIDCARD,   "
            +"             T1.F_JOINEDTIME as F_JOINEDTIME,        "
            +"             1  AS F_TOTALTIMES,   "
            +"             to_number(T.F_DDJL) AS F_TOTALLEN "
            +"        FROM T_COPYPLAN T, T_DRIVER T1      "
            +"       WHERE T1.F_TEAM ='01'  "
            +"         AND T.F_DATE BETWEEN TO_DATE('2015-06-21','YYYY-MM-DD')  AND TO_DATE('2015-07-20','YYYY-MM-DD')       "
            +"         AND T.f_driveridcard2 = T1.F_IDCARD    "
            +"         and t.f_driveridcard1 is not null      "
            +"         and t.f_driveridcard2 is not null      "
            +"         and t.f_isrental=1      "
            +"      ) T2; ";

    
    
    
    
    
    String sql_other = 
        "SELECT T2.*   "
            +"  FROM (      "
            //  -- 终端主驾驶员    
            +"        SELECT "
            +"               T1.F_NAME AS F_DRIVERNAME, "
            +"               T.F_LISENCE, "
            +"               T.F_DATE AS WORKDAY, "
            +"               T.F_DRIVER as F_DRIVERIDCARD, "
            +"               T1.F_JOINEDTIME, "
            +"               T.F_TIMES AS F_TOTALTIMES, "
            +"               T.F_CHARGE_MILE * F_TIMES AS F_TOTALLEN "
            +"          FROM T_OTHERPLAN T, T_DRIVER T1 "
            +"         WHERE T1.F_TEAM ='01'     "
            +"           AND T.F_DATE BETWEEN TO_DATE('2015-06-21','YYYY-MM-DD')  AND TO_DATE('2015-07-20','YYYY-MM-DD') AND T.F_TEAM='福州实华'     "
            +"           AND T.F_DRIVER = T1.F_IDCARD   "
            +"           AND T.F_TYPE != 3      "
            +"          AND T.F_DRIVER IS NOT NULL "
            +"          AND T.F_DRIVER1 IS NULL "
            +" "
            //  -- 终端副驾驶员
            +"        UNION ALL "
            +"        SELECT "
            +"               T1.F_NAME AS F_DRIVERNAME, "
            +"               T.F_LISENCE, "
            +"               T.F_DATE AS WORKDAY, "
            +"               T.F_DRIVER1 as F_DRIVERIDCARD, "
            +"               T1.F_JOINEDTIME, "
            +"               T.F_TIMES AS F_TOTALTIMES, "
            +"               T.F_CHARGE_MILE * F_TIMES AS F_TOTALLEN "
            +"          FROM T_OTHERPLAN T, T_DRIVER T1 "
            +"         WHERE T1.F_TEAM ='01'    "
            +"           AND T.F_DATE BETWEEN TO_DATE('2015-06-21','YYYY-MM-DD')  AND TO_DATE('2015-07-20','YYYY-MM-DD') AND T.F_TEAM='福州实华'     "
            +"           AND T.F_DRIVER1 = T1.F_IDCARD   "
            +"           AND T.F_TYPE != 3      "
            +"          AND T.F_DRIVER IS NULL "
            +"          AND T.F_DRIVER1 IS NOT NULL "
            +" "
            //  -- 终端主驾驶员(主驾驶员和副驾驶员)
            +"        UNION ALL "
            +"        SELECT "
            +"               T1.F_NAME AS F_DRIVERNAME, "
            +"               T.F_LISENCE, "
            +"               T.F_DATE AS WORKDAY, "
            +"               T.F_DRIVER as F_DRIVERIDCARD, "
            +"               T1.F_JOINEDTIME, "
            +"               T.F_TIMES AS F_TOTALTIMES, "
            +"               (T.F_CHARGE_MILE * F_TIMES) AS F_TOTALLEN "
            +"          FROM T_OTHERPLAN T, T_DRIVER T1 "
            +"         WHERE T1.F_TEAM ='01'        "
            +"           AND T.F_DATE BETWEEN TO_DATE('2015-06-21','YYYY-MM-DD')  AND TO_DATE('2015-07-20','YYYY-MM-DD') AND T.F_TEAM='福州实华'     "
            +"           AND T.F_DRIVER = T1.F_IDCARD   "
            +"           AND T.F_TYPE != 3      "
            +"          AND T.F_DRIVER IS NOT NULL "
            +"          AND T.F_DRIVER1 IS NOT NULL "
            +" "
            //  -- 终端副驾驶员(主驾驶员和副驾驶员)
            +"        UNION ALL "
            +"        SELECT "
            +"               T1.F_NAME AS F_DRIVERNAME, "
            +"               T.F_LISENCE, "
            +"               T.F_DATE AS WORKDAY, "
            +"               T.F_DRIVER1 as F_DRIVERIDCARD, "
            +"               T1.F_JOINEDTIME, "
            +"               T.F_TIMES AS F_TOTALTIMES, "
            +"               (T.F_CHARGE_MILE * F_TIMES) AS F_TOTALLEN "
            +"          FROM T_OTHERPLAN T, T_DRIVER T1 "
            +"         WHERE T1.F_TEAM ='01' "
            +"           AND T.F_DATE BETWEEN TO_DATE('2015-06-21','YYYY-MM-DD')  AND TO_DATE('2015-07-20','YYYY-MM-DD') AND T.F_TEAM='福州实华'     "
            +"           AND T.F_DRIVER1 = T1.F_IDCARD   "
            +"           AND T.F_TYPE != 3      "
            +"          AND T.F_DRIVER IS NOT NULL "
            +"          AND T.F_DRIVER1 IS NOT NULL "
            +"        ) T2; ";

    public static void main(String[] args)
    {
        String path = "C:\\Users\\antelop\\Desktop\\test\\plan.sql";
        String copyplan = "C:\\Users\\antelop\\Desktop\\test\\copyplan.sql";
        String otherpath = "C:\\Users\\antelop\\Desktop\\test\\otherplan.sql";
        String salaryCartType = "C:\\Users\\antelop\\Desktop\\test\\salaryCartType.sql";
        String car = "C:\\Users\\antelop\\Desktop\\test\\car.sql";
        String javaSql = "C:\\Users\\antelop\\Desktop\\test\\包租车javaSql.txt";
        
        String getMainSymptomtypesAndSymptomsSql = "D:\\comvee需求\\中医诊疗\\sql\\getMainSymptomtypesAndSymptoms.txt";
        
        readFile(getMainSymptomtypesAndSymptomsSql);
//        stringToSql(javaSql22);
    }
    
    private static void readFile(String path)
    {
        try {
            FileReader reader = new FileReader(new File(path));
            BufferedReader br = new BufferedReader(reader);
            String tempString = null;
            while ((tempString = br.readLine()) != null) {
                if(tempString.trim().startsWith("--"))
                    System.out.println("//" + tempString);
                else
                    System.out.println("+\"" + tempString + " \"");
            }
           
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void stringToSql(String path){
        try {
            String teamCode = "'01'";
            String beginTime = "'2015-06-21'";
            String endTime = "'2015-07-20'";
            int totalParam = 16;
            int perParam = 4;
            int f_isrental = 2;
            Object[] params = new Object[totalParam];
            int index1 = 0;
            for (int i = 0; i < perParam; i++) {
                params[index1++] = teamCode;
                params[index1++] = beginTime;
                params[index1++] = endTime;
                params[index1++] = f_isrental;
            }
            
            FileReader reader = new FileReader(new File(path));
            BufferedReader br = new BufferedReader(reader);
            String tempString = null;
            int index = 0;
            while ((tempString = br.readLine()) != null) {
                if (tempString.contains("?")){
                    String splits[] = tempString.split("\\?");
                    int num = splits.length - 1;
                    for (int i = 0; i < num; i++) {
                        tempString = tempString.replaceFirst("\\?", "" + params[index]);
                        index++;
                    }
                }
                   
                tempString = tempString.replace("+\"", "");
                tempString = tempString.replace("\"", "");
                System.out.println(tempString);
            }
           
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
