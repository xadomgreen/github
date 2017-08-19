package com.itheima.solr.dao;

import org.apache.solr.client.solrj.SolrQuery;

import com.itheima.solr.vo.ResultModel;

/**
 * 功能：接收service层传递过来的参数，根据参数查询索引库，返回查询结果。
 * @author Xadom Green
 *
 */
public interface ProductDao {
	
	// 根据索引查询条件查询solr服务器,返回结果集
	ResultModel queryProduct(SolrQuery query) throws Exception;
}
