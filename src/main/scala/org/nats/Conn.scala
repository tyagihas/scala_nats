/**
The MIT License (MIT)
Copyright (c) 2015-2016 Teppei Yagihashi
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to
deal in the Software without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
IN THE SOFTWARE.
**/

package org.nats

import java.util.Properties
import org.nats.common.Constants
import scala.reflect.runtime.universe.{typeOf, TypeTag}

class Conn private (pprops : Properties, connHandler : MsgHandler, disconnHandler : MsgHandler)
           extends Connection(pprops, connHandler, disconnHandler) {
	val version: String = "0.3.0"

	def publish(subject : String, msg : String, handler : () => Unit) {
		this.publish(subject, null, msg, handler)
	}
	
	def publish(subject : String, opt_reply : String, msg : String, handler : () => Unit) {
		this.publish(subject, msg, opt_reply, new MsgHandler {
			override def execute() { handler() }
		})
	}
	
	def publish(subject : String, msg : Array[Byte], handler : () => Unit) {
		this.publish(subject, null, msg, handler)
	}

	def publish(subject : String, opt_reply : String, msg : Array[Byte], handler : () => Unit) {
		this.publish(subject, opt_reply, msg, new MsgHandler {
		  override def execute() { handler() }
		})
	}

	def subscribe[T : TypeTag](subject : String, handler : T => Unit) : Integer = {
		return this.subscribe(subject, null, handler)
	}
	  
	def subscribe[T : TypeTag](subject : String, popts : Properties, handler : T => Unit) : Integer = {
	  if (typeOf[T] =:= typeOf[org.nats.MsgB]) {
	    var f = handler.asInstanceOf[MsgB => Unit]
		  return this.subscribe(subject, popts, new MsgHandler {
			  override def execute(msg : Array[Byte], replyTo : String, subject : String) {
				  f(new MsgB(msg, replyTo, subject))
			  }
		  })
	  }
		else {
		  var f = handler.asInstanceOf[Msg => Unit]
		  return this.subscribe(subject, popts, new MsgHandler {
			  override def execute(msg : String, replyTo : String, subject : String) {
				  f(new Msg(msg, replyTo, subject))
			  }
		  })
		}
	}

	def request(subject : String, handler : Msg => Unit) : Integer = {
		return this.request(subject, Constants.EMPTY, null, handler);
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

object Conn {
	def connect(popts : Properties,
	    connHandler : Object => Unit = null,
	    disconnHandler : Object => Unit = null) : Conn = {
		var cHandler, dHandler : MsgHandler = null
		if (connHandler != null) cHandler = new MsgHandler {
				override def execute(o : Object) { connHandler(o.asInstanceOf[Conn]) }
		}

		if (disconnHandler != null) dHandler = new MsgHandler {
				override def execute(o : Object) { disconnHandler(o.asInstanceOf[Conn]) }
		}
		
		Connection.init(popts)
		return new Conn(popts, cHandler, dHandler)
	}
}
