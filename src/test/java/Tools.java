import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangshu on 2016/10/26.
 */
public class Tools {
    final  String NEED = "+";
    final  String NONEED = "-";
    /**
     * 将对应的参数存储到相应文件内
     * @param parameter  for jmeter --vars.get("paramter")
     * @param path  file -/Users/zhangshu/Documents/Project/Automation/JmeterProject/src/test/java/get.csv
     * @param append  true 追加， false 清空后写入
     */
    public void saveParameter(String parameter,String path, boolean append) {
        try {
            FileWriter fw = new FileWriter(path,append);
            BufferedWriter bfw = new BufferedWriter(fw);
            bfw.write(parameter);
            bfw.newLine();
            bfw.flush();
            bfw.close() ;
        }catch (Exception e){
            System.out.println("Error");
        }
    }
    /**
     * 按行读取文件内容，每行作为集合的一个元素
     * @param path:文件路径
     * @return
     */
    public List getParameterList(String path){
        List list = new ArrayList();
        try {
            FileReader reader = new FileReader(path);
            BufferedReader br = new BufferedReader(reader);
            String str = null;
            while((str = br.readLine()) != null) {
                list.add(str);
            }
            br.close();
            reader.close();
        }catch (Exception e){
            System.out.println("Error");
        }
        return list;

    }


    /**
     * 将UTF格式的汉字转为普通格式
     * @param strUtf8 参数输入
     * @return
     */
    public String changeUtf8(String strUtf8) {
       // strUtf8 = "\u4e2d\u56fd\u4f01\u4e1a\u5bb6\u6742\u5fd7";
        String strChinese = null;
        try {
            strChinese = new String(strUtf8.getBytes("UTF-8"),  "utf-8");
        } catch (Exception e) {

            strChinese = "decode error";
        }
        return  strChinese;
    }

    /**
     * 正则处理
     * @param matcher:需要匹配的字符串
     * @param compile：正则表达式
     * @return ：匹配的结果
     */
    public List getMatcher(String matcher, String compile) {

        List result = new ArrayList();
        Pattern pattern = Pattern.compile(compile);
        Matcher mat = pattern.matcher(matcher);

        while (mat.find()){
            System.out.println(mat.group(0));
            result.add(mat.group(0));
            System.out.println(result);
        }
        return result;
    }
    /**
     * 正则处理
     * @param matcher:需要匹配的字符串
     * @param compile：正则表达式
     * @param index : 需要提取的表达式下标位置:0全部提取，1->提取第一个表达式....
     * @return ：匹配的结果
     */
    public List getMatcher(String matcher, String compile,int index) {

        List result = new ArrayList();
        Pattern pattern = Pattern.compile(compile);
        Matcher mat = pattern.matcher(matcher);

        while (mat.find()){
            //System.out.println(mat.group(0));
            result.add(mat.group(index));
            //System.out.println(result);
        }
        return result;
    }


    /**
     * 将固定的响应数据，组合成数组返回
     * @param value -- 指定的响应数据
     * @return
     */
    public List getResponseList(String value){
        List list = new ArrayList();
        list.add(value);
        return list;

    }

    /**
     * Keyword Helper-划分输入词
     * @param query
     * @param type --"+"  "-"
     * @return
     */

    public List getRequireList(String query,String type){
        List need = new ArrayList();
        List noNeed = new ArrayList();
        List result = new ArrayList();
        String current = null;

        String[] wordList = query.split(",");
        for(int i = 0; i<wordList.length;i++){
            current = wordList[i].toString();
            if (current.startsWith("-")){
                noNeed.add(current.replaceAll("-",""));
            }else {
                need.add(current);
            }
        }
        if (type.equals(NEED)){
            if(need.size()>0){
            result = need;}else {
                need.add("");
                result = need;
            }
        }
        if (type.equals(NONEED)){
            if(noNeed.size()>0){
                result = noNeed;
            }else {
                noNeed.add("");
                result = noNeed;
            }
        }
        return result;
    }


    /**g
     * 验证自动更新时间是否更新，且为今天
     * @param indexTime --13位时间戳
     * @return
     */
    public boolean checkIndexTime(String indexTime){
        Long index = Long.parseLong(indexTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String indexDate = sdf.format(index);
        Long currentTimeMillis = System.currentTimeMillis();
        String currentDate = sdf.format(currentTimeMillis);
        if(indexDate.equals(currentDate)){
            return true;
        }else {
            return false;
        }
    }


    /**
     * 获取getResponseFieldName
     * @param json Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param key Json响应数据中的第一层下所有的字段名称
     * @return
     */

    public String  getResponseFieldName(String json,String key){
        JSONObject object = JSON.parseObject(json);
        JSONObject filedName = object.getJSONObject("data").getJSONObject(key);
        String fieldNameStr = Arrays.toString(filedName.keySet().toArray());
        return  this.sortString(fieldNameStr," ,");

    }

    /**
     * 获取Json的第一层字段值
     * @param json Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @return
     */

    public String getOverviewBasicField(String json,String key){
        JSONObject object = JSON.parseObject(json);
        String basicfield = object.getJSONObject("data").getString(key);
        return  basicfield;
    }


    /**
     * 获取Json的第二层的字段值
     * @param json Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param key  Json
     * @param firstkey
     * @return
     */

    public String getOverviewBasicSecondfield(String json,String key,String firstkey){
        JSONObject object = JSON.parseObject(json);
        String object1 = object.getJSONObject("data").getJSONObject(key).getString(firstkey);
        return object1;
    }


    /**
     * 获取Json的第一层的数组且无filed
     * @param json Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param key
     * @return
     */
    public String getOverviewBasicList(String json,String key){
        List fields = new ArrayList();
        JSONObject object = JSON.parseObject(json);
        JSONArray basicObject = object.getJSONObject("data").getJSONArray(key);
        for (int i =0;i<basicObject.size();i++){
            fields.add(basicObject.get(i));
        }
        Collections.sort(fields);
        return  Arrays.toString(fields.toArray());
    }

    /**
     * 获取Json的第一层的数组且含有filed
     * @param json Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param key
     * @param field 返回的值可以为String Integer List
     * @return
     */
    public String getOverviewBasicListAndFieldNoSort(String json,String key,String field) {
        List fields = new ArrayList();
        JSONObject object = JSON.parseObject(json);
        JSONArray basicObject = object.getJSONObject("data").getJSONArray(key);
        for (int i = 0; i < basicObject.size(); i++) {
            //field为String
            if (basicObject.getJSONObject(i).get(field) instanceof String) {
                fields.add(basicObject.getJSONObject(i).getString(field));
            } else if (basicObject.getJSONObject(i).get(field) instanceof Integer) {
                //field为Integer
                fields.add(basicObject.getJSONObject(i).getInteger(field));
            } else {
                //field为JSONArray
                JSONArray fieldList = basicObject.getJSONObject(i).getJSONArray(field);
                for (int j = 0; j < fieldList.size(); j++) {
                    fields.add(fieldList.getString(j));
                }
            }
        }
        return Arrays.toString(fields.toArray());
    }


    /**
     * 获取Json的第一层的数组且返回字段的值可以为String的也可以为List
     * 返回的数据是排序的
     * @param json Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param firstkey  第一层数组
     * @param field 返回的值可以为String Integer List
     * @return
     */
    public String getOverviewBasicListAndField(String json,String firstkey,String field){
        List fields = new ArrayList();
        JSONObject object = JSON.parseObject(json);
        JSONArray basicObject = object.getJSONObject("data").getJSONArray(firstkey);
        for (int i =0;i<basicObject.size();i++){
            //field为String
            if (basicObject.getJSONObject(i).get(field) instanceof String) {
                fields.add(basicObject.getJSONObject(i).getString(field));
            } else if (basicObject.getJSONObject(i).get(field) instanceof Integer) {
                //field为Integer
                fields.add(basicObject.getJSONObject(i).getInteger(field));
            } else {
                //field为JSONArray
                JSONArray fieldList = basicObject.getJSONObject(i).getJSONArray(field);
                for (int j = 0; j < fieldList.size(); j++) {
                    fields.add(fieldList.getString(j));
                }
            }
        }
        Collections.sort(fields);
        return Arrays.toString(fields.toArray());
    }

    /**
     * 获取Json的第一层的数组且返回的字段是一个对象
     * @param json Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param firstkey 第一层数组
     * @param firstObject 第一层数组下面的某一个字段 且 返回值是一个对象
     * @param filed 对象下面的某一个字段名称
     * @return
     */
    public String getResponseFirstListWhichResponseObject(String json,String firstkey,String firstObject,String filed){
        List fields = new ArrayList();
        JSONObject object = JSON.parseObject(json);
        JSONArray basicObject = object.getJSONObject("data").getJSONArray(firstkey);
        for (int i=0;i<basicObject.size();i++){
            fields.add(basicObject.getJSONObject(i).getJSONObject(firstObject).getString(filed));
        }
        Collections.sort(fields);
        return Arrays.toString(fields.toArray());
    }



    /**
     * 获取Json的第二层数组且无Field
     * 返回的数据顺序固定
     * @param json Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param firstkey
     * @param secondkey
     * @return
     */
    public String getOverviewBasicSecondListNoField(String json,String firstkey,String secondkey){
        List fields = new ArrayList();
        JSONObject object = JSON.parseObject(json);
        JSONArray  basicObject = object.getJSONObject("data").getJSONObject(firstkey).getJSONArray(secondkey);
        for (int i =0;i<basicObject.size();i++){
            fields.add(basicObject.get(i));
        }
        Collections.sort(fields);
        return  Arrays.toString(fields.toArray());
    }


    /**
     * 获取Json的第二层数组且无Field
     * @param json  Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param firstkey
     * @param secondkey
     * @return
     */
    public String getOverviewBasicSecondListNoFieldNoSort(String json,String firstkey,String secondkey){
        List fields = new ArrayList();
        JSONObject object = JSON.parseObject(json);
        JSONArray  basicObject = object.getJSONObject("data").getJSONObject(firstkey).getJSONArray(secondkey);
        for (int i =0;i<basicObject.size();i++){
            fields.add(basicObject.get(i));
        }
        Collections.sort(fields);
        return  Arrays.toString(fields.toArray());
    }

    /**
     * 可以任意获取Json的第二层数组中某一个字段对应的值的值（只能为String）
     * @param json  Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param firstkey  Json响应数据中的第一层
     * @param secondkey Json响应数据中的第二层
     * @param filed  Json响应数据中的第二层对象中对应的字段名称
     * @return
     */
    public String getOverviewBasicSecondList(String json,String firstkey,String secondkey,String filed){
        List fields = new ArrayList();
        JSONObject object = JSON.parseObject(json);
        JSONArray  baseObject = object.getJSONObject("data").getJSONObject(firstkey).getJSONArray(secondkey);
        for (int i =0;i<baseObject.size();i++){
            fields.add(baseObject.getJSONObject(i).getString(filed));
        }
        Collections.sort(fields);
        return Arrays.toString(fields.toArray());
    }

    /**
     * 可以任意获取Json的第二层数组中某一个字段对应的值（可以为String也可以为一个数组）
     * @param json  Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param firstkey Json响应数据中的第一层
     * @param secondkey Json响应数据中的第二层
     * @param filed Json响应数据中的第二层对象中对应的字段名称
     * @return
     */
    public String getOverviewBasicSecondListWhichResponseStringANDList(String json,String firstkey,String secondkey,String filed){
        List fields = new ArrayList();
        JSONObject object = JSON.parseObject(json);
        JSONArray  basicObject = object.getJSONObject("data").getJSONObject(firstkey).getJSONArray(secondkey);
        for (int i =0;i<basicObject.size();i++){
            if (basicObject.getJSONObject(i).get(filed) instanceof  String){
                //字段名称的值为String
                fields.add(basicObject.getJSONObject(i).getString(filed));

            }else {
                //字段名称的值为数组
                JSONArray fieldsArray = basicObject.getJSONObject(i).getJSONArray(filed);
                for (int j=0;j<fieldsArray.size();j++){
                    fields.add(fieldsArray.get(j));
                }
            }
        }
        Collections.sort(fields);
        String fieldsStr = Arrays.toString(fields.toArray());
        return fieldsStr;
    }

    /**
     * 获取reponse中第一层为list 第二层为list或是String的返回值
     * @param json Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param firstkey 第一层 List
     * @param secondkey 第二层可以为List 也可以是String
     * @param filed 第二层为List下的field
     * @return
     */
    public String getOverviewBasicFirstListSecondListOrField(String json,String firstkey,String secondkey,String filed){
        List fields = new ArrayList();
        JSONObject object = JSON.parseObject(json);
        //第一层为List
        JSONArray firstArray = object.getJSONObject("data").getJSONArray(firstkey);
        for (int i=0;i<firstArray.size();i++){
            if (firstArray.getJSONObject(i).get(i) instanceof  String){
                //第二层为String
                fields.add(firstArray.getJSONObject(i).getString(secondkey));
            }else {
                //第二层为List
                JSONArray secondArray = firstArray.getJSONObject(i).getJSONArray(secondkey);
                for (int j=0;j<secondArray.size();j++){
                    //第二层下的某一个field
                    fields.add(secondArray.getJSONObject(j).getString(filed));
                }
            }
       }
        Collections.sort(fields);
        String fieldsStr = Arrays.toString(fields.toArray());
        return fieldsStr;
    }

    /**
     * 获取第一层和第二层都为list且没有filed的数据
     * @param json Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param firstkey 第一层为list
     * @param secondkey 第二层为list
     * @return
     */
    public String getOverviewBasicFirstListSecondListNoField(String json,String firstkey,String secondkey){
        List fields = new ArrayList();
        JSONObject object = JSON.parseObject(json);
        //第一层为List
        JSONArray firstArray = object.getJSONObject("data").getJSONArray(firstkey);
        for (int i=0;i<firstArray.size();i++){
            if (firstArray.getJSONObject(i).get(secondkey) instanceof Integer){
                //第二层为Integer
                fields.add(firstArray.getJSONObject(i).getInteger(secondkey));
            }  else if (firstArray.getJSONObject(i).get(secondkey) instanceof String){
                //第二层为String
                fields.add(firstArray.getJSONObject(i).getString(secondkey));
            } else {
                //第二层为List
                JSONArray secondArray = firstArray.getJSONObject(i).getJSONArray(secondkey);
                for (int j=0;j<secondArray.size();j++){
                    //第二层下的某一个field
                    fields.add(secondArray.getString(j));
                }
            }
            }
        Collections.sort(fields);
        String fieldsStr = Arrays.toString(fields.toArray());
        return fieldsStr;
    }

    /**
     * 获取Response中第二层中为Object
     * @param json Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param firstkey
     * @param secondkey
     * @param filed
     * @return
     */

    public String getOverviewBasicFirstListSecondObject(String json,String firstkey,String secondkey,String filed){
        List fields = new ArrayList();
        JSONObject object = JSON.parseObject(json);
        JSONArray firstArray = object.getJSONObject("data").getJSONArray(firstkey);
        for (int i=0;i<firstArray.size();i++){
            JSONArray secondArray = firstArray.getJSONObject(i).getJSONObject(secondkey).getJSONArray(filed);

                for (int j=0;j<secondArray.size();j++)
                {
                fields.add(secondArray.getString(j));}

        }
        Collections.sort(fields);
        String fieldsStr = Arrays.toString(fields.toArray());
        return fieldsStr;
    }

    /**
     * 获取Json返回数据中的第三层某一个字段的值
     * @param json Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param firstKey Json响应数据中的第一层
     * @param secondKey Json响应数据中的第二层
     * @param thirdKey Json响应数据中的第三层
     * @param filed Json响应数据中的第三层字段名称的值
     * @return
     */

    public String getResponseThirdListFieldValue(String json,String firstKey,String secondKey,String thirdKey,String filed) {
        List fields = new ArrayList();
        JSONObject object = JSON.parseObject(json);
        JSONArray basicObject = object.getJSONObject("data").getJSONObject(firstKey).getJSONArray(secondKey);

        for (int i=0;i<basicObject.size();i++){
            fields.add(basicObject.getJSONObject(i).getJSONObject(thirdKey).getString(filed));
        }
        Collections.sort(fields);
        String fieldsStr = Arrays.toString(fields.toArray());
        return fieldsStr;
    }

    /**
     * 获取Json第三层数组中某一个字段的值
     * @param json  Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param firstKey 第一层
     * @param secondKey 第二层数组
     * @param thirdKey 第三层数组
     * @param filed 数组中某一个字段名称
     * @return
     */
    public String getResponseThirdList(String json,String firstKey,String secondKey,String thirdKey,String filed){
        List fields = new ArrayList();
        JSONObject object = JSON.parseObject(json);
        //获取第二层数组
        JSONArray basicObject = object.getJSONObject("data").getJSONObject(firstKey).getJSONArray(secondKey);
        for (int i=0;i<basicObject.size();i++){
            //获取第三层数组
            JSONArray TArray = basicObject.getJSONObject(i).getJSONArray(thirdKey);
            for (int j=0;j<TArray.size();j++){
                fields.add(TArray.getJSONObject(j).getString(filed));
            }
        }
        Collections.sort(fields);
        String fieldsStr = Arrays.toString(fields.toArray());
        return fieldsStr;
    }


    /**
     * 获取第一层为List的数据
     * @param json Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param key
     * @return
     */
    public String getFirstListResponse(String json,String key){
        List fields = new ArrayList();
        JSONObject object = JSON.parseObject(json);
        JSONArray basicObject = object.getJSONArray("data");
        for (int i=0;i<basicObject.size();i++){
            if (basicObject.getJSONObject(i).get(key) instanceof  String){
                fields.add(basicObject.getJSONObject(i).getString(key));
            }else if (basicObject.getJSONObject(i).get(key) instanceof  JSONArray){
                JSONArray array = basicObject.getJSONObject(i).getJSONArray(key);
                for (int j=0;j<array.size();j++){
                    fields.add(array.getJSONObject(j).getString(key));
                }
            }
        }
        Collections.sort(fields);
        String fieldsStr = Arrays.toString(fields.toArray());
        return fieldsStr;
    }

    /**
     * 获取第一层数组
     * @param json Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param key
     * @return
     */

    public String getFirstField(String json,String key){
        List fields = new ArrayList();
        JSONObject object = JSON.parseObject(json);
        JSONArray basicObject = object.getJSONObject("data").getJSONArray(key);
        for (int i=0;i<basicObject.size();i++){
            fields.add(basicObject.get(i));
        }
        Collections.sort(fields);
        String fieldsStr = Arrays.toString(fields.toArray());
        return fieldsStr;
    }

    /**
     * 获取返回的数据第一层data为数组的数据
     * @param json Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param field
     * @return
     */
    public String getReponseFirstDataList(String json,String field){
        List fields = new ArrayList();
        JSONObject object = JSON.parseObject(json);
        JSONArray basicObject = object.getJSONArray("data");
        for (int i=0;i<basicObject.size();i++){
            fields.add(basicObject.getJSONObject(i).getString(field));
        }
        Collections.sort(fields);
        String fieldsStr = Arrays.toString(fields.toArray());
        return fieldsStr;
    }

    /**
     * 直接获取data中的响应数据(String或是List)
     * @param json Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @return
     */
    public String getDataResonse(String json){
        List fields = new ArrayList();
        JSONObject object = JSON.parseObject(json);
        if (object.get("data") instanceof  String){
           fields.add(object.getString("data"));
        }else {
            JSONArray array = object.getJSONArray("data");
            for (int i=0;i<array.size();i++){
                fields.add(array.get(i));
            }
        }
        Collections.sort(fields);
        String fieldsStr = Arrays.toString(fields.toArray());
        return fieldsStr;
    }

    /**
     * 直接获取data中的响应数据 String
     * @param json
     * @return
     */
    public String getDataStringResonse(String json){
        JSONObject object = JSON.parseObject(json);
        String data = object.getString("data");
        return data;
    }


    /**
     * 获取data为Object且key返回值为JsonArray
     * @param json Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param firstKey
     * @return
     */
    public String getDataObjectResponse(String json,String firstKey){
        List fields = new ArrayList();
        JSONObject object = JSON.parseObject(json);
        JSONArray basicObject = object.getJSONObject("data").getJSONArray(firstKey);
        for (int i=0;i<basicObject.size();i++){
            fields.add(basicObject.get(i));
        }
        Collections.sort(fields);
        String fieldsStr = Arrays.toString(fields.toArray());
        return fieldsStr;
    }

    /**
     * 获取data为Object且key返回值为String
     * @param json Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param firstKey
     * @return
     */
    public String getDataObjectAndStringResponse(String json,String firstKey){
        JSONObject object = JSON.parseObject(json);
        String  basicObject = object.getJSONObject("data").getString(firstKey);
        return basicObject;
    }

    /**
     * 获取data为Object且key返回值含有字段的JSONArray
     * @param json Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param firstKey
     * @return
     */

    public String getDataObjectAndListWithFieldResponse(String json,String firstKey,String field){
        List fields = new ArrayList();
        JSONObject object = JSON.parseObject(json);
        JSONArray basicObject = object.getJSONObject("data").getJSONArray(firstKey);
        for (int i=0;i<basicObject.size();i++){
            fields.add(basicObject.getJSONObject(i).getString(field));
        }
        Collections.sort(fields);
        String fieldsStr = Arrays.toString(fields.toArray());
        return fieldsStr;
    }

    /**
     *获取data为Object且key返回值含有字段的Object
     * @param json Jmeter中通过String json = prev.getResponseDataAsString();获取对应的请求的Json响应数据
     * @param firstKey
     * @param field
     * @return
     */
    public String getDataObjectAndObjectWithFieldResponse(String json,String firstKey,String field){
        List fields = new ArrayList();
        JSONObject object = JSON.parseObject(json);
        JSONArray basicObject = object.getJSONObject("data").getJSONObject(firstKey).getJSONArray(field);
        for (int i=0;i<basicObject.size();i++){
            fields.add(basicObject.get(i));
        }
        Collections.sort(fields);
        String fieldsStr = Arrays.toString(fields.toArray());
        return fieldsStr;
    }


    /**
     * 验证比较两个变量是否相等
     *
     * @param expValue
     * @param actValue
     * @return
     */

    public String verifyTwoVariablesWhetherEqual(String expValue, String actValue) {
        String result;
        if (actValue.equals(expValue)) {
            result = "true";
        } else {
            result = "false";
        }

        return result;
    }


    /**
     * 验证实际的值是否包含预期的值
     * @param expValue
     * @param actValue
     * @return
     */
    public String verifyActValueContainExpValue(String expValue,String actValue){
        String result;
        if (actValue.contains(expValue)){
            result = "true";
        }
        else{
            result = "false";
        }
        return result;

    }

    /**
     * 将字符串转换为List
     * @param str
     * @return
     */

    public List stringToList(String str){
        String[] array = str.split(",");
        List list = new ArrayList();
        for (int i=0;i<array.length;i++){
            list.add(array[i]);
        }
        return list;
    }

    /**
     * 截取str中从begin开始至end结束时的字符串，并将其赋值给str
     * @param str 某一个字符串（这边字符串其实是一个数组）
     * @param begin 开始截取的位置
     * @param end   结束截取的位置
     * @return
     */
    public String  subStringInList(String str,int begin,int end){
        List list = new ArrayList();
        String[] array = str.split(",");
        for (int i=0;i<array.length;i++){
            list.add(array[i].substring(begin,end));
        }
        System.out.println(list);
        String fieldsStr = Arrays.toString(list.toArray());

        return fieldsStr;
    }

    /**
     * 把用字符分割的字符串进行升序排序后返回相应字符串
     * @param str:字符串
     * @param separator：分割符
     * @return：升序排序后的字符串
     */
    public String sortString(String str,String separator){
        if(str == null || "".equals(str)){
            return null;
        }else{
            String[] str2 = str.split(separator);
            Arrays.sort(str2);
            String sortString = "";
            for(int i=0;i<str2.length;i++){
                if(i == str2.length-1){
                    sortString+=str2[i];
                }else{
                    sortString+=str2[i]+separator;
                }

            }
            return sortString;

        }
    }

    /**
     * 判断一个字符是否为中文
     * @param c:char
     * @return boolean
     */
    public  boolean isChinese(char c) {
        return c >= 0x4E00 &&  c <= 0x9FA5;// 根据字节码判断
    }

    /**
     * 把中文转成对应的unicode码
     * @param str：含有中文的字符串
     * @return ：返回把对应中文转成unicode编码后的字符串
     */
    public String chineseToUnicode(String str){
        char[] chars = str.toCharArray();
        String returnStr = "";
        for (int i = 0; i < chars.length; i++) {

            if(isChinese(chars[i])){
                returnStr += "\\u" + Integer.toString(chars[i], 16);
            }else{
                returnStr+=chars[i];
            }
        }
        return returnStr;
    }

    /**
     * 根据字段值的返回匹配到的数据
     * @param jsonArrayStr:json数组字符串
     * @param key:字段名
     * @param matchValue：需要匹配的字段值
     * @return ：匹配到的json对象字符串
     */
    public String findJsonObjectByKeyValue(String jsonArrayStr,String key,String matchValue){
        List list = new ArrayList();
        JSONArray jsonArray = JSON.parseArray(jsonArrayStr);
        for(int i = 0; i < jsonArray.size();i++){
            JSONObject obj = jsonArray.getJSONObject(i);
            if(matchValue.equals(obj.getString(key))){
                return obj.toJSONString();
            }
        }
        return null;
    }

    /**
     * 获取指定key
     * @param jsonObject:一条json数据
     * @param key:字段名
     * @return ：字段名对应的值
     */
    public String findValueByKey(String jsonObject,String key){
        JSONObject object = JSON.parseObject(jsonObject);
        if(object.get(key) == null){
            return null;
        }else{
            return object.get(key).toString();
        }

    }

    /**
     * 获取当前日期/前一天/后一天等
     * @param dayIndex:day偏移量
     * @param format:日期格式
     * @return
     */
    public String getDate(int dayIndex,String format){
        Calendar c = Calendar.getInstance();
        if(dayIndex ==0){
            //返回当前日期
            return (new SimpleDateFormat(format)).format(new Date());
        }else{
            //返回指定日期
            c.add(Calendar.DAY_OF_MONTH,dayIndex);
            return new SimpleDateFormat(format).format(c.getTimeInMillis());
        }
    }



    public static void main(String[] args) {Tools tools = new Tools();}

}

