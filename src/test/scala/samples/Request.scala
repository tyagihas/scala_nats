package samples

import java.util.Properties
import org.nats._

object Request {
  
	def main(args: Array[String]){
		var conn = Conn.connect(new Properties)
	  
		println("Subscribing...")
		conn.subscribe("help", (msg:Msg) => {conn.publish(msg.reply, "I can help!")})
		
		println ("Sending a request...")
		conn.request("help", (msg:Msg) => {
			println("Got a response for help : " + msg.body)
			sys.exit
		})		
	}
}