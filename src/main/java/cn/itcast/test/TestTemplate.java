package cn.itcast.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import cn.itcast.pojo.TbItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext-solr.xml")
public class TestTemplate {


	@Autowired
	private SolrTemplate solrTemplate;
	
	//如果ID不存在添加，存在则修改
	@Test
	public void testAdd(){
		TbItem item=new TbItem();
		item.setId(100L);
		item.setBrand("华为");
		item.setCategory("手机");
		item.setGoodsId(1L);
		item.setSeller("华为2号专卖店");
		item.setTitle("华为Mate100");
		item.setPrice(new BigDecimal(2000));		
		solrTemplate.saveBean(item);
		solrTemplate.commit();
	}
	//查询
	@Test
	public void testFindOne(){
		TbItem item = solrTemplate.getById(3, TbItem.class);
		System.out.println(item.getTitle());
	}
	//删除
	@Test
	public void testDelete(){
		solrTemplate.deleteById("0");	
		solrTemplate.commit();
	}
	//批量添加数据
	@Test
	public void testAddList(){
		List<TbItem> list=new ArrayList();
		for(Long i=0L;i<100;i++){
			TbItem item=new TbItem();
			item.setId(i);
			item.setBrand("华为");
			item.setCategory("手机");
			item.setGoodsId(1L);
			item.setSeller("华为2号专卖店");
			item.setTitle("华为Mate"+i);
			item.setPrice(new BigDecimal(2000+i));
			list.add(item);
		}
		
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
	}
	//分页查询
	@Test
	public void testPageQuery(){
		Query query=new SimpleQuery("*:*");
		query.setOffset(0);//开始索引（默认0）
		query.setRows(100);//每页记录数(默认10)
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		System.out.println("总记录数："+page.getTotalElements());
		List<TbItem> list = page.getContent();
		showList(list);
	}	
	//显示记录数据
	private void showList(List<TbItem> list){		
		for(TbItem item:list){
			System.out.println(item.getTitle() +"\t\t"+item.getPrice());
		}		
	}
	//条件查询
	@Test
	public void testPageQueryMutil(){	
		Query query=new SimpleQuery("*:*");
		//模糊查询
		Criteria criteria=new Criteria("item_category").contains("手");
		criteria=criteria.and("item_title").contains("25");		
		query.addCriteria(criteria);
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		System.out.println("总记录数："+page.getTotalElements());
		List<TbItem> list = page.getContent();
		showList(list);
	}
	//全部删除
	@Test
	public void testDeleteAll(){
		Query query=new SimpleQuery("*:*");
		
		solrTemplate.delete(query);
		solrTemplate.commit();
	}
	//条件删除
	@Test
	public void testDeleteAllMutil(){
		Query query=new SimpleQuery("*:*");
		Criteria criteria=new Criteria("item_category").contains("手");
		criteria=criteria.and("item_title").contains("25");		
		query.addCriteria(criteria);
		solrTemplate.delete(query);
		solrTemplate.commit();
	}
}




















