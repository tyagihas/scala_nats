package org.nats

import java.util.Properties

class Conn private (pprops : Properties, handler : MsgHandler) extends Connection(pprops, handler) {
	val version: String = "0.1"	

	// Instance variables and methods
	def publish(subject : String, msg : String, opt_reply : String = null, handler : => Unit) {
		this.publish(subject, msg, new MsgHandler {
			override def execute { handler }
		})
	}

	def subscribe(subject : String, handler : Msg => Unit) : Integer = {
		return this.subscribe(subject, null, handler)
	}
	  
	def subscribe(subject : String, popts : Properties, handler : Msg => Unit) : Integer = {
		return this.subscribe(subject, popts, new MsgHandler {
			override def execute(msg : String, replyTo : String, subject : String) {
				handler(new Msg(msg, replyTo, subject))
			}
		})
	}

	def request(subject : String, handler : Msg => Unit) : Integer = {
		return this.request(subject, Connection.EMPTY, null, handler);
	}
	
	def request(subject : String, data : String, popts : Properties, handler : Msg => Unit) : Integer = {
		return this.request(subject, data, popts, new MsgHandler {
			override def execute(msg : String, replyTo : String, subject : String) {
				handler(new Msg(msg, replyTo, subject))
			}
		})
	}

	def flush(handler : Object => Unit) { 
		this.flush(new MsgHandler {
			override def execute(o : Object) { handler(o) }
		})
	}

	def timeout(sid : Integer, tout : Long, prop : Properties, handler : Object => Unit) {
		this.timeout(sid, tout, prop, new MsgHandler {
			override def execute(o : Object) { handler(o) }
		})
	}
	
	override def getVersion : String = { return version }
}

// Static methods
object Conn {
  
	def connect(popts : Properties, handler : Object => Unit = null) : Conn = { 
		var mhandler : MsgHandler = null
		if (handler != null) mhandler = new MsgHandler {
				override def execute(o : Object) { handler(o.asInstanceOf[Connection]) }
		}
		
		Connection.init(popts)
		return new Conn(popts, mhandler) 
	}
}	
