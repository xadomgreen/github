package com.itheima.solr.service;

import com.itheima.solr.vo.ResultModel;

public interface ProductService {

	// 按条件查询商品  2 
	ResultModel queryProduct(String queryString, String caltalog_name, String price, String sort, Integer page) throws Exception;}
