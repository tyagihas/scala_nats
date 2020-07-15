package samples

import java.util.Properties
import org.nats._

object SubTimeout {

	def main(args: Array[String]){
		var conn = Conn.connect(new Properties)
	  
		println("Listening on : " + args(0))
		var received = 0
		var sid : Integer = conn.subscribe(args(0), (msg:Msg) => {received += 1})
		
		conn.timeout(sid, 1, null, (o:Object) => {println("Timeout waiting for a message!")})
		
		println("\nPress enter to exit.")
		scala.io.StdIn.readLine
		
		conn.close
		sys.exit
	}
}