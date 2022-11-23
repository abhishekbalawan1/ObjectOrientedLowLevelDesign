import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String args[]) throws InterruptedException {
        RateLimiterFactory rateLimiterFactory = new RateLimiterFactory();
        RateLimiter rateLimiter = rateLimiterFactory.createSlidingWindowRateLimiter(4, 1000);

        rateLimiter.insert(new Request("Request1", java.time.LocalTime.now()));
        rateLimiter.insert(new Request("Request2", java.time.LocalTime.now()));
        rateLimiter.insert(new Request("Request3", java.time.LocalTime.now()));
        rateLimiter.insert(new Request("Request4", java.time.LocalTime.now()));
        rateLimiter.insert(new Request("Request5", java.time.LocalTime.now()));
        rateLimiter.insert(new Request("Request6", java.time.LocalTime.now()));

        Timer timer = new Timer();
        TimerTask task = rateLimiter;
        timer.schedule(task, 0, 1000);

        Thread.sleep(2000);
        rateLimiter.insert(new Request("Request5", java.time.LocalTime.now()));
        rateLimiter.insert(new Request("Request6", java.time.LocalTime.now()));

    }
}
class Request{
    String requestURL;
    LocalTime time;
    Request(){

    }
    Request(String requestRUL, LocalTime now){
        this.requestURL = requestRUL;
        this.time = now;
    }
}

abstract class RateLimiter extends TimerTask {

    int size;
    LinkedList<Request> queue;

    RateLimiter(int size){
        this.size = size;
        queue = new LinkedList<Request>();
    }
    RateLimiter(){
        queue = new LinkedList<Request>();
    }
    @Override
    public void run(){}
    abstract void insert(Request request);
    abstract void serve();
}

class LeakyBucket extends RateLimiter{

    LeakyBucket(){
        super();
    }

    @Override
    void insert(Request request) {
        System.out.println("request inserted : "+request.requestURL +"  "+ request.time);
        queue.addLast(request);
    }

    @Override
    void serve() {
        Request request = queue.removeFirst();
        if(request != null){
            System.out.println("served request : "+request.requestURL +"  "+ request.time);
        }
        else{
            System.out.println("No request to serve");
        }
    }
    @Override
    public void run(){
        serve();
    }
}


class FixedWindow extends RateLimiter{

    int counter;

    FixedWindow(int size){
        super(size);
        this.counter = 0;
    }

    @Override
    void insert(Request request) {
        if(counter >= size){
            System.out.println("request can not be served");
            return;
        }
        System.out.println("request inserted : "+request.requestURL +"  "+ request.time);
        queue.addLast(request);
        counter++;
    }

    @Override
    void serve() {
        if(queue.size() > 0){
            Request request = queue.removeFirst();
            System.out.println("served request : "+request.requestURL +"  "+ request.time);
            return;
        }
        System.out.println("No request to serve");
    }
    @Override
    public void run(){
        counter = 0;
    }
}

class SlidingWindow extends RateLimiter{

    int windlowLnegth;
    SlidingWindow(int size, int windlowLnegth){
        super(size);
        this.windlowLnegth = windlowLnegth;
    }

    @Override
    void insert(Request request) {
        LocalTime time = java.time.LocalTime.now();
        int counter = 0;
        int i=0;
        while(i<queue.size() && time.minusSeconds((long)windlowLnegth).isBefore(queue.get(i).time)){
            i++;
            counter++;
        }
        if(counter >= size){
            System.out.println("request can not be served");
            return;
        }
        System.out.println("request inserted : "+request.requestURL +"  "+ request.time);
        queue.addLast(request);
    }

    @Override
    void serve() {
        if(queue.size() > 0){
            Request request = queue.removeFirst();
            System.out.println("served request : "+request.requestURL +"  "+ request.time);
            return;
        }
        System.out.println("No request to serve");
    }

    @Override
    public void run(){
        serve();
    }
}


class RateLimiterFactory{
    public RateLimiter createSlidingWindowRateLimiter(int size, int windowLength){
        return new SlidingWindow(size, windowLength);
    }
    public RateLimiter createLeakyBucketRateLimiter(){
        return new LeakyBucket();
    }
    public RateLimiter createFixedWindowRateLimiter(int size){
        return new FixedWindow(size);
    }
}