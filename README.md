# Scala_Nats

Scala client library for the [NATS messaging system](http://nats.io).

## Supported Platforms

```javascript
scala_nats currently supports following Java Platforms :

- Java Platform, Standard Edition 6 (Java SE 6)
- Java Platform, Standard Edition 7 (Java SE 7)
- Scala 2.10.4+
```

## Getting Started

- Install [java_nats](https://github.com/tyagihas/java_nats) and add "java_nats-\<version\>.jar" to CLASSPATH.

- Download source files or adding dependency to Maven pom.xml

```xml
<dependency>
	<groupId>com.github.tyagihas</groupId>
	<artifactId>scala_nats</artifactId>
	<version>0.1</version>
</dependency>
```

## Basic Usage

```scala
import java.util.Properties
import org.nats._
...
var conn = Conn.connect(new Properties)

// Simple Publisher
conn.publish("hello", "world")

// Simple Subscriber
conn.subscribe(args(0), (msg:Msg) => {println("Received update : " + msg.body)})

// Unsubscribing
var sid : Integer = conn.subscribe(args(0), (msg:Msg) => {println("Received update : " + msg.body)})
conn.unsubscribe(sid);

// Requests
conn.request("help", (msg:Msg) => {
	println("Got a response for help : " + msg.body)
	sys.exit
})		

// Replies
conn.subscribe("help", (msg:Msg) => {conn.publish(msg.reply, "I can help!")})
				
conn.close();
```

## Clustered Usage

```scala
var opts : Properties = new Properties
opts.put("servers", "nats://user1:pass1@server1,nats://user1:pass1@server2:4243");
var conn = Conn.connect(opts)
		
println("Publishing...")
conn.publish("hello", "world")
```

## License

(The MIT License)

Copyright (c) 2014-2015 Teppei Yagihashi

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


