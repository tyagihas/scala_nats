package samples

import java.util.Properties
import org.nats._

object Sub {
  
	def main(args: Array[String]){
		var conn = Conn.connect(new Properties)
	  
		println("Listening on : " + args(0))
		conn.subscribe(args(0), (msg:Msg) => {println("Received update : " + msg.body)})
		
		println("\nPress enter to exit.")
		scala.io.StdIn.readLine
		
		conn.close
		sys.exit
	}
}
   
