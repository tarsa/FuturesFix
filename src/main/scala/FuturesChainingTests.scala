import scala.io.StdIn

trait ArraySetup {
  // Pick a large number
  val arraySize = 1000000
  // Make sure that we will hit OOME if Promise Linking doesn't work
  val tooManyArrays: Int =
    (Runtime.getRuntime.totalMemory() / arraySize).toInt * 100
}

object FlatMapBefore extends ArraySetup with App {
  import futures.before.concurrent.ExecutionContext.Implicits.global
  import futures.before.concurrent.Future

  def loop(i: Int, arraySize: Int): Future[Unit] = {
    val array = new Array[Byte](arraySize)
    Future.successful(i).flatMap { i =>
      if (i == 0) {
        Future.successful(())
      } else {
        array.length // Force closure to refer to array
        loop(i - 1, arraySize)
      }

    }
  }

  loop(tooManyArrays, arraySize) onComplete println
  StdIn.readLine()
}

object FlatMapAfter extends ArraySetup with App {
  import futures.after.concurrent.ExecutionContext.Implicits.global
  import futures.after.concurrent.Future

  def loop(i: Int, arraySize: Int): Future[Unit] = {
    val array = new Array[Byte](arraySize)
    Future.successful(i).flatMap { i =>
      if (i == 0) {
        Future.successful(())
      } else {
        array.length // Force closure to refer to array
        loop(i - 1, arraySize)
      }

    }
  }

  loop(tooManyArrays, arraySize) onComplete println
  StdIn.readLine()
}

object FlatMapFixed extends ArraySetup with App {
  import futures.fixed.concurrent.ExecutionContext.Implicits.global
  import futures.fixed.concurrent.Future

  def loop(i: Int, arraySize: Int): Future[Unit] = {
    val array = new Array[Byte](arraySize)
    Future.successful(i).flatMap { i =>
      if (i == 0) {
        Future.successful(())
      } else {
        array.length // Force closure to refer to array
        loop(i - 1, arraySize)
      }

    }
  }

  loop(tooManyArrays, arraySize) onComplete println
  StdIn.readLine()
}

object FlatMapCurrent extends ArraySetup with App {
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.Future

  def loop(i: Int, arraySize: Int): Future[Unit] = {
    val array = new Array[Byte](arraySize)
    Future.successful(i).flatMap { i =>
      if (i == 0) {
        Future.successful(())
      } else {
        array.length // Force closure to refer to array
        loop(i - 1, arraySize)
      }

    }
  }

  loop(tooManyArrays, arraySize) onComplete println
  StdIn.readLine()
}

object RecoverWithBefore extends ArraySetup with App {
  import futures.before.concurrent.ExecutionContext.Implicits.global
  import futures.before.concurrent.Future

  // An example of possible space leak using ACPS by virtue of `recoverWith`:
  def loopRW(i: Int, arraySize: Int): Future[Unit] = {
    val array = new Array[Byte](arraySize)
    Future(throw new Exception).recoverWith { case _ =>
      if (i == 0) {
        Future(())
      } else {
        array.length // Force closure to refer to array
        loopRW(i - 1, arraySize)
      }
    }
  }

  loopRW(tooManyArrays, arraySize) onComplete println
  StdIn.readLine()
}

object RecoverWithAfter extends ArraySetup with App {
  import futures.after.concurrent.ExecutionContext.Implicits.global
  import futures.after.concurrent.Future

  // An example of possible space leak using ACPS by virtue of `recoverWith`:
  def loopRW(i: Int, arraySize: Int): Future[Unit] = {
    val array = new Array[Byte](arraySize)
    Future(throw new Exception).recoverWith { case _ =>
      if (i == 0) {
        Future(())
      } else {
        array.length // Force closure to refer to array
        loopRW(i - 1, arraySize)
      }
    }
  }

  loopRW(tooManyArrays, arraySize) onComplete println
  StdIn.readLine()
}

object RecoverWithFixed extends ArraySetup with App {
  import futures.fixed.concurrent.ExecutionContext.Implicits.global
  import futures.fixed.concurrent.Future

  // An example of possible space leak using ACPS by virtue of `recoverWith`:
  def loopRW(i: Int, arraySize: Int): Future[Unit] = {
    val array = new Array[Byte](arraySize)
    Future(throw new Exception).recoverWith { case _ =>
      if (i == 0) {
        Future(())
      } else {
        array.length // Force closure to refer to array
        loopRW(i - 1, arraySize)
      }
    }
  }

  loopRW(tooManyArrays, arraySize) onComplete println
  StdIn.readLine()
}

object RecoverWithCurrent extends ArraySetup with App {
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.Future

  // An example of possible space leak using ACPS by virtue of `recoverWith`:
  def loopRW(i: Int, arraySize: Int): Future[Unit] = {
    val array = new Array[Byte](arraySize)
    Future(throw new Exception).recoverWith { case _ =>
      if (i == 0) {
        Future(())
      } else {
        array.length // Force closure to refer to array
        loopRW(i - 1, arraySize)
      }
    }
  }

  loopRW(tooManyArrays, arraySize) onComplete println
  StdIn.readLine()
}
