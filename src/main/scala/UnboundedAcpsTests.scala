import scala.annotation.tailrec

/*
 * Copyright (C) 2017 Piotr Tarsa ( http://github.com/tarsa )
 *
 *  This software is provided 'as-is', without any express or implied
 *  warranty.  In no event will the author be held liable for any damages
 *  arising from the use of this software.
 *
 *  Permission is granted to anyone to use this software for any purpose,
 *  including commercial applications, and to alter it and redistribute it
 *  freely, subject to the following restrictions:
 *
 *  1. The origin of this software must not be misrepresented; you must not
 *     claim that you wrote the original software. If you use this software
 *     in a product, an acknowledgment in the product documentation would be
 *     appreciated but is not required.
 *  2. Altered source versions must be plainly marked as such, and must not be
 *     misrepresented as being the original software.
 *  3. This notice may not be removed or altered from any source distribution.
 *
 */
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
