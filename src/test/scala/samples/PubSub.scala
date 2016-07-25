package samples

import java.util.Properties
import org.nats._

object PubSub {
  
	def main(args: Array[String]){
		var conn = Conn.connect(new Properties)

	  println("Listening on : " + args(0))
		conn.subscribe(args(0) + "_txt", (msg:Msg) => {println("Received update as text : " + new String(msg.body))})
		conn.subscribe(args(0) + "_bin", (msg:MsgB) => {println("Received update as binary : " + new String(msg.body))})
	 	
		conn.publish(args(0) + "_txt", "test")
		conn.publish(args(0) + "_bin", "test".getBytes)
		
		println("\nPress enter to exit.")
		readLine
		
		conn.close
		sys.exit
	}
}
   
