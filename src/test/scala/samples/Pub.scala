package samples

import java.util.Properties
import org.nats._

object Pub {
  
	def main(args: Array[String]){
		var conn = Conn.connect(new Properties)
		
		println("Publishing...")
		conn.publish("hello", "world")
		
		conn.close
		sys.exit
	}
}