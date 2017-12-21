package MockClient;

import org.apache.log4j.Logger;

public class Mock{
	public static void show(String out){
		Logger log = Logger.getLogger(Mock.class.getName());
		log.info(Thread.currentThread().getName()+":"+out);
	}
}