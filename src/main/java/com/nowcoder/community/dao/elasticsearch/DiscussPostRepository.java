package com.nowcoder.community.dao.elasticsearch;


import com.nowcoder.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


//Integer代表实体类主键类型
//ElasticsearchRepository已经自动定义了es服务器访问的增删改查操作
@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {

}
