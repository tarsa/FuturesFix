import scala.annotation.tailrec

abstract class CounterSetup {
  val counter = new java.util.concurrent.atomic.AtomicLong(0)

  @tailrec
  final def waitToStabilize(lastCounter: Long): Unit = {
    Thread.sleep(1234)
    val newCounter = counter.get()
    if (newCounter != lastCounter) {
      waitToStabilize(newCounter)
    }
  }
}

object UnboundedAcpsBefore extends CounterSetup with App {
  import futures.before.concurrent.ExecutionContext.Implicits.global
  import futures.before.concurrent.Future

  def loop(): Future[Unit] = {
    Future(throw new Exception).recoverWith { case _ =>
      counter.incrementAndGet()
      loop()
    }
  }

  loop() onComplete println
  waitToStabilize(0)
  println(counter.get())
}

object UnboundedAcpsAfter extends CounterSetup with App {
  import futures.after.concurrent.ExecutionContext.Implicits.global
  import futures.after.concurrent.Future

  def loop(): Future[Unit] = {
    Future(throw new Exception).recoverWith { case _ =>
      counter.incrementAndGet()
      loop()
    }
  }

  loop() onComplete println
  waitToStabilize(0)
  println(counter.get())
}

object UnboundedAcpsFixed extends CounterSetup with App {
  import futures.fixed.concurrent.ExecutionContext.Implicits.global
  import futures.fixed.concurrent.Future

  def loop(): Future[Unit] = {
    Future(throw new Exception).recoverWith { case _ =>
      counter.incrementAndGet()
      loop()
    }
  }

  loop() onComplete println
  waitToStabilize(0)
  println(counter.get())
}

object UnboundedAcpsCurrent extends CounterSetup with App {
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.Future

  def loop(): Future[Unit] = {
    Future(throw new Exception).recoverWith { case _ =>
      counter.incrementAndGet()
      loop()
    }
  }

  loop() onComplete println
  waitToStabilize(0)
  println(counter.get())
}
