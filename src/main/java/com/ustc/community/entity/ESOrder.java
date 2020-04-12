package com.ustc.community.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;



//配置之后实体与索引绑定在一起
@Document(indexName = "iffOrder", type = "_doc", shards = 6, replicas = 3)
public class ESOrder {

    @Id
    private int id;

    @Field(type = FieldType.Text)
    private String purchaseOrderId;


    @Field(type = FieldType.Text)
    private String cartId;

    @Field(type = FieldType.Text)
    private String sellerId;

    @Field(type = FieldType.Text)
    private String fundingStatus;

    @Field(type = FieldType.Text)
    private String isPrimaryOrder;



}
