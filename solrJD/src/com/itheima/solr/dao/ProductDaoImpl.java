package com.itheima.solr.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itheima.solr.vo.ProductModel;
import com.itheima.solr.vo.ResultModel;

/**
 * 功能：接收service层传递过来的参数，根据参数查询索引库，返回查询结果。
 * 
 * @author Xadom Green
 *
 */
@Repository
public class ProductDaoImpl implements ProductDao {

	@Autowired
	private HttpSolrServer httpSolrServer;
	/**
	 * @param SolrQuery对象
	 * @return 一个商品列表List<ProductModel>，还需要返回查询结果的总数量。
	 * @return ResultModel
	 */
	@Override
	public ResultModel queryProduct(SolrQuery query) throws Exception {
		
		System.out.println(query);
		ResultModel resultModel = new ResultModel();

		// 根据queryModel 对象查询商品列表
		QueryResponse queryResponse = httpSolrServer.query(query);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		// 取查询的结果的总数量
		resultModel.setRecordCount(solrDocumentList.getNumFound());
		List<ProductModel> productList = new ArrayList<>();
		// 遍历查询结果
		for (SolrDocument solrDocument : solrDocumentList) {
			// 取商品信息
			ProductModel productModel = new ProductModel();
			productModel.setPid((String) solrDocument.get("id"));

			// 取高亮显示
			String productName = "";
			Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			
			List<String> list = highlighting.get(solrDocument.get("id")).get("product_name");
			if (null != list) {
				productName = list.get(0);
			} else {
				productName = (String) solrDocument.get("product_name");
			}
			productModel.setName(productName);

			productModel.setPrice((float) solrDocument.get("product_price"));
			productModel.setCatalog_name((String) solrDocument.get("product_catalog_name"));
			productModel.setPicture((String) solrDocument.get("product_picture"));

			// 添加到商品列表
			productList.add(productModel);
		}
		// 商品列表添加到resultModel中
		resultModel.setProductList(productList);
		return resultModel;
	}
}
