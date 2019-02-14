package com.ssm.Controller;

import java.util.List;

import javax.jws.WebParam.Mode;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ssm.Po.ItemsCustom;
import com.ssm.Po.ItemsQueryVo;
import com.ssm.Service.ItemsService;

/**
 * 使用注解开发Handler（注意：注解映射器和注解适配器也要同时搭配使用）
 * 不用再实现接口，因此可以添加不止add()的其他方法
 */
@org.springframework.stereotype.Controller
@RequestMapping("/items")
public class ItemsController {
	
	@Autowired
	private ItemsService itemsService;
	// 商品查询列表
	//使用@RequestMapping来实现queryItems（）方法和url一一映射
	//一般建议将url和方法名称写成一样
	@RequestMapping("/queryItems.action")
	public ModelAndView queryItems(ItemsQueryVo itemsQueryVo) throws Exception {

		// 调用service查询数据库
		List<ItemsCustom> itemsList = itemsService.findItemsList(itemsQueryVo);
		// 返回ModelAndView
		ModelAndView modelAndView = new ModelAndView();
		// 相当 于request的setAttribut，在jsp页面中通过itemsList取数据
		modelAndView.addObject("itemsList", itemsList);

		// 指定视图
		modelAndView.setViewName("items/itemsList");

		return modelAndView;
	}
	
	//商品修改页面展示
	//@RequestMapping("/editItem.action")
	//限制http请求路径方法为get和post
	@RequestMapping(value="/editItem.action" ,method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView editItem(Integer id)throws Exception {
		
		// 调用service根据id查询商品信息
		ItemsCustom itemsCustom = itemsService.fingItemById(id);
		
		// 返回ModelAndView
		ModelAndView modelAndView = new ModelAndView();
		
		//商品信息放入Model
		modelAndView.addObject("itemsCustom", itemsCustom);
		
		// View指定视图
		modelAndView.setViewName("items/editItems");
		
		return modelAndView;
	}
	//提交修改页面
	//如果不使用@RequestParam，要求request传入参数名称和controller方法的形参名称一致，方可绑定成功。
	//如果使用@RequestParam，不用限制request传入参数名称和controller方法的形参名称一致。
	@RequestMapping("/editItemsSubmit.action")
	public String editItemSubmit(HttpServletRequest request,@RequestParam(value="id",required=true)Integer Itemsid,ItemsCustom itemsCustom)throws Exception {
		
		itemsService.updateItems(Itemsid, itemsCustom);
		return "successEdit";
	}
	
	//批量修改页面
	@RequestMapping("/deleteItems.action")
	public String deleteItems(Integer[] items_id) throws Exception{
		
		itemsService.deleteItems(items_id);
		return "successDelete";
	}
	
	//批量编辑页面展示
	@RequestMapping("/editItemsAll.action")
	public ModelAndView editItemsAll(ItemsQueryVo itemsQueryVo) throws Exception{
		
		// 调用service查询数据库
		List<ItemsCustom> itemsList = itemsService.findItemsList(itemsQueryVo);
		// 返回ModelAndView
		ModelAndView modelAndView = new ModelAndView();
		// 相当 于request的setAttribut，在jsp页面中通过itemsList取数据
		modelAndView.addObject("itemsList", itemsList);

		// 指定视图
		modelAndView.setViewName("items/editItemsAll");

		return modelAndView;
	}
	
	//批量编辑提交
	@RequestMapping("/editItemsAllSubmit.action")
	public String editItemsAllSubmit(ItemsQueryVo itemsQueryVo) throws Exception{
		for(ItemsCustom itemsCustom:itemsQueryVo.getItemsCustomList()){
			Integer Itemsid=itemsCustom.getId();
	     	itemsService.updateItems(Itemsid, itemsCustom);
		}
		return "successEdit";
	}
}
