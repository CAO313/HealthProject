package com.buka.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.buka.constant.RedisConstant;
import com.buka.dao.CheckGroupDao;
import com.buka.dao.CheckItemDao;
import com.buka.dao.TSetmealMapper;
import com.buka.entity.PageResult;
import com.buka.entity.QueryPageBean;
import com.buka.pojo.CheckGroup;
import com.buka.pojo.CheckItem;
import com.buka.pojo.TSetmeal;
import com.buka.pojo.TSetmealExample;
import com.buka.service.SetMealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private TSetmealMapper tSetmealMapper;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private CheckGroupDao checkGroupDao;
    @Autowired
    private CheckItemDao checkItemDao;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${out_put_path}")
    private String outputPath;
    @Override
    public void add(Integer[] checkGroupIds, TSetmeal tSetmeal) {
            tSetmealMapper.insertSelective(tSetmeal);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,tSetmeal.getImg());
            int setMealID = tSetmeal.getId();
        Map<String,Integer> map = new HashMap<>();
        map.put("setMealID",setMealID);
        for (Integer checkGroupId : checkGroupIds) {
            map.put("checkGroupID",checkGroupId);
            tSetmealMapper.addCheckGroups(map);
        }
        this.staticHTML();
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<TSetmeal> page = tSetmealMapper.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<TSetmeal> getAll() {
        TSetmealExample tSetmealExample = new TSetmealExample();
        List<TSetmeal> list =  tSetmealMapper.selectByExample(tSetmealExample);
        return list;
    }

    @Override
    public TSetmeal findByID(Integer id) {
        TSetmeal tSetmeal = tSetmealMapper.selectByPrimaryKey(id);
        List<CheckGroup> checkGroups = new ArrayList<>();

       Integer[] checkGroupIDS  = tSetmealMapper.findCheckGoupsByID(id);
        for (Integer checkGroupID : checkGroupIDS) {
            List<CheckItem> checkItems = new ArrayList<>();
          CheckGroup checkGroup = checkGroupDao.findById(checkGroupID);
          Integer[] checkItemIDs = checkGroupDao.getCheckItemIdsById(checkGroupID);
            for (Integer itemID : checkItemIDs) {
                CheckItem checkItem = checkItemDao.findById(itemID);
                checkItems.add(checkItem);
            }
            checkGroup.setCheckItems(checkItems);
            checkGroups.add(checkGroup);
            System.out.println(checkGroup);
        }
        tSetmeal.setCheckGroups(checkGroups);
        System.out.println(tSetmeal);
        return tSetmeal;
    }
    //生成静态页面
    public void staticHTML(){
        List<TSetmeal> list = this.getAll();
        this.staticSetMealListHTML(list);
        this.staticSetMealDetailHTML(list);

    }
    //生成套餐列表页
    public  void staticSetMealListHTML(List<TSetmeal> list){
        Map<String,List<TSetmeal>> dataMap = new HashMap<>();
        dataMap.put("setmealList",list);
        this.generateHTML("mobile_setmeal.ftl","m_setmeal.html",dataMap);
    }
    //生成套餐详情页
    public void staticSetMealDetailHTML(List<TSetmeal> list){
        for (TSetmeal tSetmeal : list) {
            Map<String,Object> dataMap = new HashMap();
            dataMap.put("setmeal",this.findByID(tSetmeal.getId()));
            this.generateHTML("mobile_setmeal_detail.ftl","setmeal_detail_"+tSetmeal.getId()+".html",dataMap);
        }
    }
    
    //生成静态页面通用
    public void generateHTML(String templateName, String pageName, Map map){
       Configuration configuration = freeMarkerConfigurer.getConfiguration();
       configuration.setClassicCompatible(true);
       try{
           Template template = configuration.getTemplate(templateName);
           File docFile = new File(outputPath+"/"+pageName);
           Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
           template.process(map,out);
           out.close();
       }catch (Exception e){
           e.printStackTrace();
       }
    }
}
