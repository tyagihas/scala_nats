# Scala_Nats

Scala client library for the [NATS messaging system](http://nats.io).

## Supported Platforms

```javascript
- Scala 2.11.8+
```

## Getting Started

- Install [java_nats](https://github.com/tyagihas/java_nats) and add "java_nats-\<version\>.jar" to CLASSPATH
- Download source files from scala_nats

OR

- Adding dependency

```xml
// Maven pom.xml
<dependency>
  <groupId>com.github.tyagihas</groupId>
  <artifactId>scala_nats_2.11</artifactId>
  <version>0.3.0</version>
</dependency>

// SBT build.sbt
libraryDependencies += "com.github.tyagihas" % "scala_nats_2.11" % "0.3.0"
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

## Wildcard Subcriptions

```scala
// "*" matches any token, at any level of the subject.
conn.subscribe("foo.*.baz", (msg:Msg) => {println("Received a message on [" + msg.subject + "] : " + msg.body)})

conn.subscribe("foo.bar.*", (msg:Msg) => {println("Received a message on [" + msg.subject + "] : " + msg.body)})

conn.subscribe("*.bar.*", (msg:Msg) => {println("Received a message on [" + msg.subject + "] : " + msg.body)})

// ">" matches any length of the tail of a subject, and can only be the last token
// E.g. 'foo.>' will match 'foo.bar', 'foo.bar.baz', 'foo.foo.bar.bax.22'
conn.subscribe("foo.>", (msg:Msg) => {println("Received a message on [" + msg.subject + "] : " + msg.body)})
```

## Clustered Usage

```scala
var opts : Properties = new Properties
opts.put("servers", "nats://user1:pass1@server1,nats://user1:pass1@server2:4243");
var conn = Conn.connect(opts)

println("Publishing...")
conn.publish("hello", "world")
```

## Binary Messages

```scala
conn.subscribe("test", (msg:MsgB) => {println("Received update as binary : " + new String(msg.body))})

conn.publish("test", "Hello World".getBytes)
```

## Advanced Usage

```scala
conn.publish("foo", "You done?", () => {println("Message processed!")})

// Timeouts for subscriptions
var received = 0
var sid : Integer = conn.subscribe("foo", (msg:Msg) => {received += 1})

conn.timeout(sid, TIMEOUT_IN_SECS, null, (o:Object) => {println("Timeout waiting for a message!")})

// Timeout unless a certain number of messages have been received
var opt : Properties = new Properties;
opt.put("expected", new Integer(2));
conn.timeout(sid, 10, opt, (o:Object) => {timeout_recv = true})

// Auto-unsubscribe after MAX_WANTED messages received
conn.unsubscribe(sid, MAX_WANTED)

// Multiple connections
conn1.subscribe("test", (msg:Msg) => {println("received : " + msg.body)})

// Form second connection to send message on
var conn2 = Conn.connect(new Properties, (conn:Object) => {conn.asInstanceOf[Conn].publish("test", "Hello World")})
```

## TLS

* Use "keytool" to create TrustStore and KeyStore (if necessary) or specify existing ones in next step.

* Set properties to configure TrustStore and KeyStore.

```scala
var props = new Properties
props.put("truststore", "./truststore")
props.put("truststore_pass", "password")
// KeyStore is used only when tlsverify is set on the server.
props.put("keystore", "./keystore")
props.put("keystore_pass", "password")

// Automatically detect if TLS is configured on the server.
var conn = Conn.connect(props)
```

## License

(The MIT License)

Copyright (c) 2015-2017 Teppei Yagihashi

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
