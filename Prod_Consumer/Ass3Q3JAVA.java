import java.util.concurrent.Semaphore;
import java.util.Random;
public class Ass3Q3JAVA {
	public static void main(String[] args) {
		Mix.startProg();
	}
}
class Mix{
	// so 4 producers can't try and put things in at the same time since that exceeds the buffer length
	public static Semaphore s = new Semaphore(3); 
	private static Consumer [] c = new Consumer[6];
	public static Random randomElement = new Random();
	
	public static void startProg() {
		// consumers with id starting from 1
		for (int i = 1; i < c.length; i++) { 
			c[i] = new Consumer(i);
		}
		
		while(true) {
			// range of random values is from 1 to number of consumers
			int min = 1;
			int max = Mix.numOfConsumers();
			// pick random consumer to start
			int cid = min + Mix.randomElement.nextInt(max - 1);
			// pick random number (used to make Mix randomly select to produce or consume)
			long threadId = Thread.currentThread().getId();
	        int n = Mix.randomElement.nextInt(150);
	        
	        if(n % 2 == 0){
	        	// this is to prevent consumer from wasting their time in an empty buffer
	        	if(Buffer.bufEmpty() == false) {
	        		(new Thread (c[cid])).start();
	        	} 
	        	// this is because if the buffer is empty the only option is to produce
	        	else (new Thread (new Producer())).start();
	        }
        	else {
	        	// this is to prevent Producers from losing messages 
	        	if(Buffer.bufFull() == false) {
	        		(new Thread (new Producer())).start();
	        	}
	        	// this is because if the buffer is full the only option is to consume 
	        	else (new Thread (c[cid])).start(); 
        	}
		}
	}
	
	public static int numOfConsumers() {
		return c.length;
	}
}
class Producer implements Runnable{	
	private String [] msgs = new String[Mix.numOfConsumers()];
	
	Producer() {
		for (int i = 0; i < msgs.length; i++) {
			msgs[i] = "message: " + i;
		}
	}
	public void sendMsg(String msg, int cid){
		try {
			Mix.s.acquire();
			Buffer.store(cid, msg);
		} catch (InterruptedException e) {}
		finally {
			Mix.s.release();
		}
	}
	
	public void run() {
		// range of random values is from 1 to number of consumers
		int min = 1;
		int max = Mix.numOfConsumers();
		// pick random consumer
		int cid = min + Mix.randomElement.nextInt(max - 1);
		// pick random message
		int msgMap = min + Mix.randomElement.nextInt(max - 1);
		
		System.out.println("PRODUCER made new message for cid: " + cid);
		sendMsg(msgs[msgMap], cid);
		
	}
}

class Consumer implements Runnable{
	private int cid;
	
	Consumer(int id){
		cid = id;
	}
	
	public int getID(){
		return cid;
	}
	
	public void receive(String msg) {
		System.out.println("CONSUMER: " + cid + " received msg: " + msg);
	}
	public void run() {
		Buffer.send(this);
	}
}

class Buffer{
	final private static int bufSize = 3;
	private static int consArray[] = new int[bufSize];
	private static String msgArray[] = new String[bufSize];
	
	public static void store(int cid, String msg) {	
		for(int i = 0; i < bufSize; i++ ) {
			if (consArray[i] == 0) {
				consArray[i] = cid;
				msgArray[i] = msg;
				break;
			}
		}
	}
	
	public static int send(Consumer c) {
		for(int i = 0; i < bufSize; i++) {
			if (consArray[i] == c.getID()) {
				c.receive(msgArray[i]);
				consArray[i] = 0;
				msgArray[i] = "";
				return 0;
			}
		}
		System.out.println("no messages for consumer: " + c.getID());
		return 0;
	}
	
	public static boolean bufFull() {
		for(int i = 0; i < bufSize; i++ ) {
			if (consArray[i] == 0) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean bufEmpty() {
		for(int i = 0; i < bufSize; i++ ) {
			if (consArray[i] != 0) {
				return false;
			}
		}
		return true;
	}
}

