package service.member

import com.sksamuel.elastic4s.RefreshPolicy
import com.sksamuel.elastic4s.http.ElasticDsl.scriptQuery
import com.sksamuel.elastic4s.http.search.queries.QueryBuilderFn
import com.sksamuel.elastic4s.http.{ElasticClient, ElasticProperties}
import com.sksamuel.elastic4s.script.Script
import com.sksamuel.elastic4s.searches.queries.{Query, ScriptQuery}


object memberSearchService {
  private val client = ElasticClient(ElasticProperties("http://localhost:9200"))

  def searchMember = {
    import com.sksamuel.elastic4s.http.ElasticDsl._

    // indexを作る
    client.execute{
      createIndex("artists").mappings(
        mapping("modern").fields(
          textField("name")
        )
      )
    }.await

    // indexにデータを追加
    client.execute{
      // artist = index(database)
      // modern = type(table)
      indexInto("artists" / "modern").fields("name" -> "kinsho tomoya").refresh(RefreshPolicy.Immediate)
    }.await

    // 検索を行う
    val resp = client.execute {
      search("artists")
    }.await

    // update_by_query
    // queryで対象を絞り込む
    // 今回の場合は、filed = name, value = tomoyaを更新対象のドキュメントにしている
    // scriptで、実際に更新したい値を設定
    // 以下のクエりを投げて、elasticsearchないのドキュメントを更新したい
//    """{
//      |  "query": {
//      |    "match": {
//      |      "name": "tomoya"
//      |    }
//      |  },
//      |  "script": {
//      |    "lang": "painless",
//      |    "source": "ctx._source.name = params.update_name",
//      |    "params": {
//      |      "update_name": "kinsho smile tomoya"
//      |    }
//      |  }
//      |}
//      |"""

    client.execute{
      updateByQuery("artists", "modern", createQuery).script(createScript)
    }

    printJson

    // 以下のようなクエリを組み立てたい
//    """{
//      |  "query": {
//      |    "term": { //一致する
//      |      "type.keyword": {// fieldのkyeがtypeで。
//      |        "value": "fruit" // fieldのvalueがfruitのdocumentの
//      |      }
//      |    }
//      |  },
//      |  "script": {
//      |    "lang": "painless",
//      |    "source": "ctx._source.price += params.price_raise",  //上記の条件に当てはまるドキュメントのpriceフィールドを更新する
//      |    "params": {
//      |      "price_raise": 1000
//      |    }
//      |  }
//      |}"""

    def printJson = {
      // val query: String = QueryBuilderFn(createScript).string()
      // print(query)
//      {
//        termQuery("type.keyword", value = "fruit")
//        scriptQuery("s")
//      }
    }

    def createQuery: Query = {
      matchQuery("name", "tomoya")
      // termQuery("type.keyword", "fruit")
    }

    def createScript: Script = {
      Script("ctx._source.price += params.price_raise").lang("painless").param("price_raise", 1000)
    }

  }
}
