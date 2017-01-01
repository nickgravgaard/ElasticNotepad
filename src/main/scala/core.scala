package object core {

  def maxConsecutive(list: List[Option[Int]]) : List[Option[Int]] = list match {
    // scala>     maxConsecutive(List(Some(1), Some(2), None, Some(4), None, None, Some(7), Some(8), Some(9)))
    // res1: List[Option[Int]] = List(Some(2), Some(2), None, Some(4), None, None, Some(9), Some(9), Some(9))
    case Nil => Nil
    case h::t => h match {
      case None => None :: maxConsecutive(list.drop(1))
      case Some(cell) => {
        val segment = list.takeWhile(_.isDefined).map(_.get)
        List.fill(segment.length)(Option(segment.max)) ::: maxConsecutive(list.drop(segment.length))
      }
    }
  }

  def calcMaxedWidthsPerLine(textWidthsPerLine: List[Array[Int]]) : List[List[Int]] = {
    val maxNofCells = (for (textWidthsThisLine <- textWidthsPerLine) yield textWidthsThisLine.length).max

    val maxedWidthsPerColumn = for (c <- 0 until maxNofCells)
      yield maxConsecutive(for (textWidthsThisLine <- textWidthsPerLine)
        yield if (c < textWidthsThisLine.indices.last)
          Option(textWidthsThisLine(c)) else None)

    for (maxedWidthsThisLine <- maxedWidthsPerColumn.toList.transpose)
      yield maxedWidthsThisLine.takeWhile(_.isDefined).map(_.get)
  }

  def calcTabstopPositions(textPerLine: List[String], measureText: String => Int): List[List[Int]] = {
    val textWidthsPerLine = for (textThisLine <- textPerLine)
      yield for (textThisCell <- textThisLine.split('\t'))
        yield measureText(textThisCell)

    for (maxedWidthsThisLine <- calcMaxedWidthsPerLine(textWidthsPerLine))
      yield maxedWidthsThisLine.scanLeft(0)(_ + _).drop(1)
  }

}