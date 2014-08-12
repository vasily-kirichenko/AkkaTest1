package AkkaTest1

import akka.actor._
import concurrent.duration._

sealed trait Message
case object Ping extends Message
case class Pong(count: Int) extends Message
case object Stop extends Message

class Server extends Actor {
  var count = 0
  def receive = {
    case Ping => {
      count += 1
      count match {
        case x if x < 1000 => sender ! Stop
        case _ => sender ! Pong(count)
    }
  }
}

class Client extends Actor {
  def receive = {
    case Pong => sender ! Ping
    case Stop => println("[Client]: Stopped.")
  }
}

object Test extends App {
  val system = ActorSystem("test1")
  val greeter = system.actorOf(Props[Greeter], "greeter")
  greeter.tell(WhoToGreet("kot"), ActorRef.noSender)
  inbox.send(greeter, Greet)
  val Greeting(message1) = inbox.receive(5 seconds)
  println(s"Greeting: $message1")

  for (i <- 1 to 1000000) {
    greeter ! WhoToGreet(i.toString)
  }
}