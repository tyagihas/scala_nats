package samples

import java.util.Properties
import org.nats._

object ClusteredPub {
  
	def main(args: Array[String]){
		var opts : Properties = new Properties
		opts.put("servers", "nats://user1:pass1@server1,nats://user1:pass1@server2:4243");
		var conn = Conn.connect(opts)
		
		println("Publishing...")
		conn.publish("hello", "world")
		
		conn.close
		sys.exit
	}
}