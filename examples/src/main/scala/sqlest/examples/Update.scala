package sqlest.examples

import sqlest._

object UpdateExamples extends App with DatabaseExample {
  InsertExamples.insertAll

  val updateStatement =
    update(FruitTable)
      .set(FruitTable.juiciness -> 9)
      .where(FruitTable.name === "Banana")

  database.withTransaction { implicit transaction =>
    // running execute on the update statement returns the number of lines changed (ie updated)
    val numberUpdated = updateStatement.execute
    println(numberUpdated)
  }

  case class Fruit(id: Int, name: String, juiciness: Int)
  lazy val fruitExtractor = extract[Fruit](
    id = FruitTable.id,
    name = FruitTable.name,
    juiciness = FruitTable.juiciness
  )

  val newGrape = Fruit(3, "Grape", 1)
  val newUpdateStatement =
    update(FruitTable)
      .set(fruitExtractor.settersFor(newGrape))
      .where(FruitTable.id === newGrape.id)

  database.withTransaction { implicit transaction =>
    // running execute on the update statement returns the number of lines changed (ie updated)
    val newNumberUpdated = newUpdateStatement.execute
    println(newNumberUpdated)
  }
}
