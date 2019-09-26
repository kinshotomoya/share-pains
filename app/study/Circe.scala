package study

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import play.api.libs.json.{Format, Json}

object Circe {
  val jsonString = """{
                     "foo" : "foo value",
                     "bar" : {
                     "bar_child" : "bar child value"
                     },
                     "array":[
                     { "content" : 1 },
                     { "content" : 2 },
                     { "content" : 3 }
                     ]
                     }"""
  def parseJson = {
    parse(this.jsonString)
  }

  def encodeToJson: Unit = {
    val json = Greeting("hello", Person("tomoya")).asJson  // asJsonの引数に暗黙的に、encodeGreeting渡される
    println(json)
  }
}


case class Person(name: String)

case class Greeting(word: String, person: Person)

object Greeting {
  // asJsonメソッドの引数にフォーマットを指定できる。
  // formatを指定することによって、jsonのフィールド名を独自に変更できる。
  // asJsonメソッドは、引数をimplicitで受け取っているので、implicit valを指定してあげる。
  implicit lazy val encodeGreeting: Encoder[Greeting] = Encoder.forProduct1("WORD")(g =>
    // この場合は、json出力が、以下のようになる
//    """{
//       |  "WORD" : "hello"
//       |}"""
    (g.word)
  )
}
