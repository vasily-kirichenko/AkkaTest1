package AkkaTest1

import akka.actor._

case object Ping
case class Pong(count: Int)
case object Stop

class Server extends Actor with ActorLogging {
  var count = 0

  def receive = {
    case Ping =>
      count += 1
      log.info(s"Ping received. Count = $count now.")
      count match {
        case x if x > 10 => sender ! Stop
        case _ => sender ! Pong(count)
      }
  }
}

class Client extends Actor with ActorLogging {
  val server = context.actorSelection("akka://test-system/user/server*")
  server ! Ping
  def receive = {
    case pong @ Pong(count) =>
      log.info(s"$pong received")
      sender ! Ping
    case Stop => log.info("Stop received")
  }
}

object Test extends App {
  //val config = ConfigFactory.parseFile(new File(".\\resources\\application.conf"))
  val system = ActorSystem("test-system") //, config)
  system.actorOf(Props[Server], "server1")
  system.actorOf(Props[Server], "server2")
  system.actorOf(Props[Client], "client")
  system.shutdown()
}