package samples

import java.util.Properties
import org.nats._

object Connect {
  
	def main(args: Array[String]){
		var conn = Conn.connect(new Properties, 
		    (o : Object) => { println("connected") },
		    (o : Object) => { println("disconnected") } )
		
		conn.publish("hello", "world")		
		Thread.sleep(5000)
		
		conn.close
		sys.exit
	}
}