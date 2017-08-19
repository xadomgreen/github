package com.itheima.solr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itheima.solr.service.ProductService;
import com.itheima.solr.vo.ResultModel;

/**
 * 
 * @author Xadom Green 功能：接收页面传递过来的参数调用service查询商品列表。将查询结果返回给jsp页面，还需要查询参数的回显。
 */
@Controller
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	/**
	 * 参数： 1、查询条件：字符串 2、商品分类的过滤条件：商品的分类名称，字符串
	 * 3、商品价格区间：传递一个字符串，满足格式：“0-100、101-200、201-*”
	 * 4、排序条件：页面传递过来一个升序或者降序就可以，默认是价格排序。0：升序1：降序
	 * 5、分页信息：每页显示的记录条数创建一个常量60条。传递一个当前页码就可以了。 6、Model：相当于request。
	 *
	 * 返回结果：String类型，就是一个jsp的名称。
	 */
	@RequestMapping("/list")
	public String queryProduct(String queryString, String catalog_name, String price, String sort, Integer page,
			Model model) throws Exception {
		// 查询商品列表
		ResultModel resultModel = productService.queryProduct(queryString, catalog_name, price, sort, page);

		// 列表传递给jsp
		model.addAttribute("result", resultModel);
		// 参数回显
		// 参数回显
		model.addAttribute("queryString", queryString);
		model.addAttribute("caltalog_name", catalog_name);
		model.addAttribute("price", price);
		model.addAttribute("sort", sort);
		model.addAttribute("page", page);

		return "product_list";
	}

	/**
	 * 跳转到product_list.jsp页面
	 */
	@RequestMapping("/product_list.action")
	public String listUI() {
		return "product_list";
	}

}
