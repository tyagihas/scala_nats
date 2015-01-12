package org.nats

class Msg  (pbody : String = null, preply : String = null, psubject : String = null) {
		var body : String = pbody
		var reply : String = preply
		var subject : String = psubject
}