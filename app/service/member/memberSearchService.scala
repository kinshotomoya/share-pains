package service.member

import com.sksamuel.elastic4s.RefreshPolicy
import com.sksamuel.elastic4s.http.{ElasticClient, ElasticProperties}


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
    client.execute{
      updateByQuery("artists", "modern", matchQuery("name", "tomoya")).script("ctx._source.name = 'kinsho mimi'")
    }

    // update_by_query

    print("---------------")
    print(resp)
    print("---------------")
  }
}
