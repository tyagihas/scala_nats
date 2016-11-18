package samples

import java.util.Properties
import org.nats._

object SecurePub {
  
	def main(args: Array[String]){
    var props = new Properties
    props.put("truststore", "./truststore")
    props.put("truststore_pass", "password")
    // KeyStore is used only when tlsverify is set on the server.
    props.put("keystore", "./keystore")
    props.put("keystore_pass", "password")
    
		var conn = Conn.connect(props)
		
		println("Publishing...")
		conn.publish("hello", "world")
		
		conn.close
		sys.exit
	}
}