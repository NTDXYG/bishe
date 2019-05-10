package com.yg.bishe.util;

import com.yg.bishe.bean.Admin_Role;
import com.yg.bishe.bean.Banner;
import com.yg.bishe.bean.ResBody;
import com.yg.bishe.service.AdminService;
import com.yg.bishe.service.BannerService;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DateUtilTest {


    @Autowired
    AdminService adminService;
    @Autowired
    BannerService bannerService;

    @Test
    public void getAdminList() {
        ResBody resBody = new ResBody();
        int count = adminService.getCount();
        List<Admin_Role> admin_roles= adminService.findAllAdmin(1, 10);
        resBody.setCount(count);
        resBody.setData(admin_roles);
        resBody.setCode(0);
        assertNotNull(resBody.getData());
    }

    @Test
    public void getBannerList(){
        ResBody resBody = new ResBody();
        int count = bannerService.getCount();
        List<Banner> bannerList= bannerService.findAllBanner(1, 10);
        resBody.setCount(count);
        resBody.setData(bannerList);
        resBody.setCode(0);
        assertNotNull(resBody.getData());
    }

    @Test
    public void getCommendList() throws Exception {
        ResBody resBody = new ResBody();
        DataModel model =new FileDataModel(new File("C:/ratingdata.txt"));
        UserSimilarity similarity =new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood =new NearestNUserNeighborhood(2,similarity,model);
        Recommender recommender= new GenericUserBasedRecommender(model,neighborhood,similarity);
        List list = new ArrayList();
        for (int i = 1;i<=5;i++){
            Map map = new HashMap();
            List<RecommendedItem> recommendations =recommender.recommend(i, 1);//为用户1推荐1个ItemID
            for (RecommendedItem item:recommendations){
                if (item != null){
                    map.put("itemid",item.getItemID());
                    map.put("value",item.getValue());
                }
            }
            if (map.get("itemid") == null){
                map.put("itemid","暂无推荐");
                map.put("value","0");
            }
            map.put("userid",i);
            list.add(map);
        }
        resBody.setData(list);
        resBody.setCode(0);
        resBody.setCount(5);
        assertNotNull(resBody.getData());
    }
}