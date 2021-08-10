package com.whale.provider.es.handler;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.bytes.ByteBufferReference;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * @Author: sy
 * @Date: Created by 2021/8/10 15:47
 * @description:
 */
public class HighlightResultMapper extends DefaultResultMapper {

    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
        for (SearchHit hit : response.getHits()) {
            Map<String, Object> sourceMap = hit.getSourceAsMap();
            for (Map.Entry<String, HighlightField> entry : hit.getHighlightFields().entrySet()) {
                String key = entry.getKey();
                if (sourceMap.containsKey(key)) {
                    Text[] fragments = entry.getValue().getFragments();
                    sourceMap.put(key, transTextArrayToString(fragments));
                }
            }
            hit.sourceRef(new ByteBufferReference(ByteBuffer.wrap(JSONObject.toJSONString(sourceMap).getBytes())));
        }
        return super.mapResults(response, clazz, pageable);
    }

    private String transTextArrayToString(Text[] fragments) {
        if (null == fragments) {
            return "";
        }
        StringBuilder buffer = new StringBuilder();
        for (Text fragment : fragments) {
            buffer.append(fragment.string());
        }
        return buffer.toString();
    }

}
