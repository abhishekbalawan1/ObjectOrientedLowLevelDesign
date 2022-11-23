public class Main {
    public static void main(String[] args){

        CacheFactory factory = new CacheFactory();
        Cache cache = factory.createCacheFactory("LFUCache", 4);

        cache.add("key1","desc1");
        cache.add("key2","desc2");
        cache.add("key3","desc3");
        cache.add("key4","desc4");
        cache.getValue("key1");
        cache.getValue("key2");
        cache.add("key5","desc5");
        cache.getValue("key2");
        cache.getValue("key2");
        cache.getValue("key2");
        cache.add("key6","desc6");
        cache.getValue("key6");
        cache.add("key7","desc7");
        cache.getValue("key7");
        cache.add("key8","desc8");
        cache.printCache();
    }
}
