package com.itheima.solr.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itheima.solr.dao.ProductDao;
import com.itheima.solr.utils.Commons;
import com.itheima.solr.vo.ResultModel;

/**
 * 功能：接收action传递过来的参数，根据参数拼装一个查询条件，调用dao层方法，查询商品列表。接收返回的商品列表和商品的总数量，
 * 根据每页显示的商品数量计算总页数。
 * 
 * @author Xadom Green
 *
 */
@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	/**
	 * @author Xadom Green
	 * 
	 * @Param 1、查询条件：字符串
	 * @Param 2、商品分类的过滤条件：商品的分类名称，字符串
	 * @Param 3、商品价格区间：传递一个字符串，满足格式：“0-100、101-200、201-*”
	 * @Param 4、排序条件：页面传递过来一个升序或者降序就可以，默认是价格排序。0：升序1：降序
	 * @Param 5、分页信息：每页显示的记录条数创建一个常量60条。传递一个当前页码就可以了
	 *
	 * @return ResultModel
	 */
	@Override
	public ResultModel queryProduct(String queryString, String catalog_name, String price, String sort, Integer page)
			throws Exception {

		// 拼装查询条件
		SolrQuery query = new SolrQuery();
		// 查询条件
		if (null != queryString && !"".equals(queryString)) {
			query.setQuery(queryString);
		} else {
			query.setQuery("*:*");
		}
		// 商品分类名称过滤
		if (null != catalog_name && !"".equals(catalog_name)) {
			query.addFilterQuery("product_catalog_name:" + catalog_name);
		}
		// 价格区间过滤
		if (null != price && !"".equals(price)) {
			String[] strings = price.split("-");
			query.addFilterQuery("product_price:[" + strings[0] + " TO " + strings[1] + "]");
		}
		// 排序条件
		if ("1".equals(sort)) {
			query.setSort("product_price", ORDER.desc);
		} else {
			query.setSort("product_price", ORDER.asc);
		}
		// 分页处理
		if (null == page) {
			page = 1;
		}

		// start
		int start = (page - 1) * Commons.PAGE_SIZE;

		query.setStart(start);
		query.setRows(Commons.PAGE_SIZE);

		// 设置默认搜索域
		query.set("df", "product_name");
		// 高亮设置
		query.setHighlight(true);
		query.addHighlightField("product_name");
		query.setHighlightSimplePre("<span style=\"color:red\">");
		query.setHighlightSimplePost("</span>");

		// 查询商品列表
		ResultModel resultModel = productDao.queryProduct(query);

		// 计算总页数
		long recordCount = resultModel.getRecordCount();
		int pages = (int) (recordCount / Commons.PAGE_SIZE);
		if (recordCount % Commons.PAGE_SIZE > 0) {
			pages++;
		}

		resultModel.setPageCount(pages);
		resultModel.setCurPage(page);
		return resultModel;
	}

}
