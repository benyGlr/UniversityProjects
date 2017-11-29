import java.util.concurrent.Semaphore;

/* Design Decisions/Assumptions:
	- Made constant number of consumer but infinite number of producers because 
	I didn't want producers to send to consumers that didn't exist
	- Used a semaphore on the buffer so that more producers than spaces in the buffer wouldn't
	be able to input things at the same time sine it would lead to one of the producers losing their message
	- In order for producers to not lose messages I have them not produce messages while the buffer is full
	- Each producer send 1 message then die's since for testing purposes its easier this way to have infinite producers
	- A consumer that has multiple messages addressed to him will only read one of those messages each time he accesses the buffer
	
	Testing:
		In the Mix class i made a testProg() method which creates a fixed number of producer that send a random
		message to a random consumer then the consumers go through the buffer one at a time to see if there was a 
		message for them in order to run this multiple times i also made a clearBuffer method for testing purposes
		then i saw if the messages that the producer made matched up to the messages the consumers read 
		
		Also have a lost message exception that happens if a producer puts something into a buffer while its full
		since that would result in a lost message, then ran the code as is for a while and checked that the exception
		didn't come up
	
	
*/
	
import java.util.Random;
public class Ass3Q3JAVA {
	public static void main(String[] args) {
		Mix.startProg();
		//Mix.testProg();
	}
}
class Mix{
	// so multiple producers can't put in messages at the same time to exceed the buffer 
	public static Semaphore s = new Semaphore(1); 
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
		
	public static void testProg() {
		// consumers with id starting from 1
		for (int i = 1; i < c.length; i++) { 
			c[i] = new Consumer(i);
		}
		
		for (int i = 0; i < 10; i++) {
	        for (int x = 0 ; x < Buffer.bufSize; x++)
	        {
	        	Producer p = new Producer();
	        	p.run();
	        }
	        for (int x = 1 ; x < Mix.numOfConsumers(); x++)
	        {
	        	c[x].run();
	        }
	        Buffer.clearBuffer();
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
			try {
				Buffer.store(cid, msg);
			} catch(LostMessageException l) {}
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
		
		System.out.println("PRODUCER made new message = " + msgs[msgMap] + ", for cid: " + cid);
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
	final public static int bufSize = 3;
	private static int consArray[] = new int[bufSize];
	private static String msgArray[] = new String[bufSize];
	
	public static int store(int cid, String msg) throws LostMessageException {	
		for(int i = 0; i < bufSize; i++ ) {
			if (consArray[i] == 0) {
				consArray[i] = cid;
				msgArray[i] = msg;
				return 0;
			}
		}
		throw new LostMessageException(msg, cid);
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
	// only for testing purpose
	public static void clearBuffer() {
		for(int i = 0; i < bufSize; i++ ) {
			consArray[i] = 0;
		}
	}
}


