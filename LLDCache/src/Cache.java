import java.util.HashMap;
import java.util.LinkedList;

public abstract class Cache {
    HashMap<String, String> map;
    int size;
    Cache(int size){
        map = new HashMap<String, String>();
        this.size = size;
    }
    abstract void add(String key, String value);
    abstract void remove();
    abstract String getValue(String key);
    void printCache(){
        System.out.println(map);
    }
}

class LRUCache extends Cache {
    LinkedList<String> LList;
    LRUCache(int size){
        super(size);
        LList = new LinkedList<String>();
    }

    @Override
    public void add(String key, String value) {
        if(map.size() == size){
            remove();
        }
        LList.addFirst(key);
        map.put(key, value);
    }

    @Override
    public void remove() {
        String key = LList.removeLast();
        map.remove(key);
    }

    @Override
    public String getValue(String key){
        int index = 0;
        for(index=0; index<LList.size(); index++){
            if(LList.get(index).equals(key)){
                break;
            }
        }
        LList.remove(index);
        LList.addFirst(key);
        return map.get(key);
    }
}
class LFUCache extends Cache{
    HashMap<String, Integer> frequencyCounter;
    LFUCache(int size){
        super(size);
        frequencyCounter = new HashMap<>();
    }

    @Override
    void add(String key, String value) {
        if(map.size() == size){
            remove();
        }
        frequencyCounter.put(key, frequencyCounter.getOrDefault(key, 0)+1);
        map.put(key, value);
    }

    @Override
    void remove() {
        int lowestFrequency = Integer.MAX_VALUE;
        String lowestFrequencyKey = "";
        for(String key : frequencyCounter.keySet()){
            if(frequencyCounter.get(key) < lowestFrequency){
                lowestFrequencyKey = key;
                lowestFrequency = frequencyCounter.get(key);
            }
        }
        map.remove(lowestFrequencyKey);
        frequencyCounter.remove(lowestFrequencyKey);
    }

    @Override
    public String getValue(String key) {
        if(frequencyCounter.containsKey(key)== false){
            return "Key is not present in the cache";
        }
        frequencyCounter.put(key, frequencyCounter.getOrDefault(key, 0)+1);
        return map.get(key);
    }
}

class MRUCache extends Cache {
    LinkedList<String> LList;
    MRUCache(int size){
        super(size);
        LList = new LinkedList<String>();
    }

    @Override
    public void add(String key, String value) {
        if(map.size() == size){
            remove();
        }
        LList.addFirst(key);
        map.put(key, value);
    }

    @Override
    public void remove() {
        String key = LList.removeFirst();
        map.remove(key);
    }

    @Override
    public String getValue(String key){
        int index = 0;
        for(index=0; index<LList.size(); index++){
            if(LList.get(index).equals(key)){
                break;
            }
        }
        LList.remove(index);
        LList.addFirst(key);
        return map.get(key);
    }
}

class CacheFactory{
    Cache createCacheFactory(String cacheType){
        if(cacheType.equals("LRUCache")){
            return new LRUCache(5);
        }
        else if(cacheType.equals("LFUCache")){
            return new LFUCache(5);
        }
        else if(cacheType.equals("MRUCache")){
            return new MRUCache(5);
        }
        else{
            return null;
        }
    }
    Cache createCacheFactory(String cacheType, int size){
        if(cacheType.equals("LRUCache")){
            return new LRUCache(size);
        }
        else if(cacheType.equals("LFUCache")){
            return new LFUCache(size);
        }
        else if(cacheType.equals("MRUCache")){
            return new MRUCache(size);
        }
        else{
            return null;
        }
    }
}
