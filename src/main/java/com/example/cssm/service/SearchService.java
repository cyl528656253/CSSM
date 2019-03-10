package com.example.cssm.service;

import com.example.cssm.async.EventConsumer;
import com.example.cssm.domain.News;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Autowired
    NewsService newsService;

    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
    private static final String SOLR_URL = "http://127.0.0.1:8983/solr/wenda";
    private HttpSolrClient client = new HttpSolrClient.Builder(SOLR_URL).build();
    private static final String NEWS_TITLE_FIELD = "toutiao_title";
    private static final String NEW_ID_FIELD = "toutiao_id";

    public List<News> searchQuestion(String keyword, int offset, int count,
                                     String hlPre, String hlPos) {
        List<News> newsList = new ArrayList<>();
        try {
            SolrQuery query = new SolrQuery();

            query.set("q", keyword);//相关查询，比如某条数据某个字段含有周、星、驰三个字  将会查询出来 ，这个作用适用于联想查询

            //参数df,给query设置默认搜索域，从哪个字段上查找
            query.set("df", NEWS_TITLE_FIELD);
            query.set("hl.fl",NEWS_TITLE_FIELD);
            //参数sort,设置返回结果的排序规则
        //    query.setSort("id",SolrQuery.ORDER.desc);

            //设置分页参数
            query.setStart(offset);
            query.setRows(count);

            //设置高亮显示以及结果的样式
            query.setHighlight(true);
        //    query.addHighlightField(NEWS_TITLE_FIELD);
            query.setHighlightSimplePre(hlPre);
            query.setHighlightSimplePost(hlPos);
            QueryResponse response = client.query(query);

            /*
            for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
                News news = new News();
                news.setId(Integer.parseInt(entry.getKey()));
                if (entry.getValue().containsKey(NEWS_TITLE_FIELD)) {
                    List<String> contentList = entry.getValue().get(NEWS_TITLE_FIELD);
                    if (contentList.size() > 0) {
                        news.setTitle(contentList.get(0));
                    }
                }
                newsList.add(news);

            }*/
            SolrDocumentList resultList = response.getResults();
            for(SolrDocument document: resultList){
                String id = (String) document.get(NEW_ID_FIELD);
            //    System.out.println(id);
               News news = newsService.getById(Integer.parseInt(id));
               logger.info(news.getId() + news.getTitle());
               newsList.add(news);
            }
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return newsList;
    }


}