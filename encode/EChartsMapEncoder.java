package *;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * 用于将地图geo信息按echarts的格式进行压缩
 */
public class EChartsMapEncoder {

    public static final ScriptEngine engine         = new ScriptEngineManager().getEngineByName("nashorn");

    /**
     * 根路径
     */
    private static final String      ROOT_PATH      = EChartsMapEncoder.class.getResource("/").getPath().replaceFirst("/", "").replaceAll("target/classes/", "") + "src/main/webapp";

    /**
     * 实际执行工作的js路径
     */
    private static final String      WORK_JS        = ROOT_PATH + "/MapEncode/e-charts-encode.js";

    /**
     * 需要被压缩并编码的json文件
     */
    private static final String      JSON_TO_ENCODE = ROOT_PATH + "/MapEncode/shenzhen-add-shenshan.json";

    static {
        try {
            engine.eval(new FileReader(WORK_JS));
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static final Invocable invocable = (Invocable) engine;

    /**
     * 压缩为 echarts 格式的地图
     * @param geoJson
     * @param fileName
     * @param type   json 或 其他（按js处理）
     * @return
     */
    public static String convert2Echarts(String geoJson, String fileName, String type) {
        try {
            return (String) invocable.invokeFunction("convert2Echarts", geoJson, fileName, type);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        StringBuilder stringBuilder = new StringBuilder("");
        BufferedReader br = new BufferedReader(new FileReader(JSON_TO_ENCODE));
        String line = null;
        while ((line = br.readLine()) != null) {
            stringBuilder.append(line);
        }
        br.close();
        System.out.println("压缩后的json为:\n" + convert2Echarts(stringBuilder.toString(), "shenzhen", "json"));
    }
}