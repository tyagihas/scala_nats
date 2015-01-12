package samples

import java.util.Properties
import org.nats._

object SubUnsub {

    def main(args: Array[String]){
		var conn = Conn.connect(new Properties)
	  
		println("Subscribing...")
		var sid : Integer = conn.subscribe(args(0), (msg:Msg) => {println("Received update : " + msg.body)})
		
		println("\nPress enter to unsubscribe.")
		readLine
		conn.unsubscribe(sid);
		
		conn.close
		sys.exit
	}
}