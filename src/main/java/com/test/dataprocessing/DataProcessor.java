package com.test.dataprocessing;

import com.google.common.collect.Lists;
import com.test.base.Base;
import com.test.dboperations.HSQLDBInsertData;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Process Json Data for Each Row in Log File
 */
public class DataProcessor  extends Thread {

    public List<Map<String, String>> list = null;
    public ConcurrentHashMap map;
    public Set<String> processedJson;
    public HSQLDBInsertData insert;

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DataProcessor.class.getName());

    public DataProcessor() {
        this.map = new ConcurrentHashMap<>();
        this.processedJson = ConcurrentHashMap.newKeySet();
        this.insert = new HSQLDBInsertData();

    }

    /**
     * Internal method to concatenate multiple lists into single ArrayList
     *
     * @param lists
     * @return Concatenated ArrayList of JSONObjects
     */
    public static List<JSONObject> concatenate(List<List> lists) {
        List<JSONObject> arr = new ArrayList<>();
        lists.stream()
                .forEach(s -> s.
                        stream()
                        .forEach(j -> arr.add((JSONObject) j)));
        return arr;
    }

    /**
     * Process Json Objects and Insert in HSQL Database
     *
     * @param objects
     */
    public void  ProcessAndInsertDataFromJsonObjects(List<JSONObject> objects) {
        try {
            Map<String, List<JSONObject>> mapObject =
                    objects.parallelStream().collect(Collectors.groupingBy(o -> (String) o.get("id")));

            List<ArrayList> listOfLists = mapObject.values().parallelStream().collect(Collectors.toList())
                    .stream().map(obj -> (ArrayList) obj)
                    .collect(Collectors.toList());


            int THREADSIZE = Math.min(listOfLists.size(), Integer.parseInt(Base.getProperty("THREADSIZE")));
            int SUBLISTSIZE = Math.min(listOfLists.size(), listOfLists.size()==0?1:listOfLists.size() % THREADSIZE ==0 ? (listOfLists.size() / THREADSIZE): ((listOfLists.size() / THREADSIZE)+1));

            log.info("Processing the Json Objects with THREAD Count of: "+ THREADSIZE);

            List smallerLists = Lists.partition(listOfLists, SUBLISTSIZE);
            for (int i = 0; i < THREADSIZE; i++) {
                List<JSONObject> newList = concatenate((List) smallerLists.get(i));

                new Thread(() -> {
                    log.debug("ThreadName: " + Thread.currentThread().getName()+ " is processing: " + newList);
                    this.iterateOverJsonObjectList(newList);
                }).start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Internal Method to Iterate of Sublist grouped based on Json Object Id attribute
     * This Method also Inserts the Data in HSQL Database
     *
     * @param objectsSubList
     */

    public void iterateOverJsonObjectList(List<JSONObject> objectsSubList) {

        try {
            for (JSONObject o : objectsSubList) {
                long duration;
                String id = (String) o.get("id");

                if (!processedJson.contains(id)) {
                    //log.debug("id : "  + id+ " ,n:" + (++n));
                    Map<String, String> data = new HashMap<>();

                    duration = calculateDuration(objectsSubList, id);

                    if (duration != -1) {
 /*                         duration = (long) list.get(1) - (long) list.get(0);
                            duration = duration > 0 ? duration : -duration;*/
                            data.put("id", id);
                            data.put("type", (String) o.get("type"));
                            data.put("host", (String) o.get("host"));
                            data.put("duration", String.valueOf(duration));
                            data.put("alert", String.valueOf(duration > 4));
                            insert.insertRecord(data);
                            processedJson.add(id);
                    } else {
                        log.warn("Started/Finished Record is missing or Duplicate records are present for event_id: " + id);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            //e.printStackTrace();
        }
    }

    /**
     *
     * @param objectsSubList
     * @param id
     * @return duration
     */
    public long calculateDuration(List<JSONObject> objectsSubList, String id){
        long duration;

        List<Object> list = objectsSubList
                .stream()
                .filter(x -> x.get("id").equals(id))
                .map(y -> y.get("timestamp"))
                .collect(Collectors.toList());
        if (list.size() == 2) {
            duration = (long) list.get(1) - (long) list.get(0);
            duration = duration > 0 ? duration : -duration;
            return duration;
        }else{
            return -1;
        }
    }


}