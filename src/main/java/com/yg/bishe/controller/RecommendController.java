package com.yg.bishe.controller;

import com.yg.bishe.bean.ResBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Api(tags = "推荐接口")
public class RecommendController {
    private final Logger logger = LoggerFactory.getLogger(RecommendController.class);

    @ApiOperation("基于用户的协同过滤")
    @ResponseBody
    @GetMapping(value = "/User_based")
    public ResBody User_based() throws Exception {
        ResBody resBody = new ResBody();
        // step:1 构建模型 2 计算相似度 3 查找k紧邻 4 构造推荐引擎
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
        return resBody;
    }

    @ApiOperation("基于物品的协同过滤")
    @ResponseBody
    @GetMapping(value = "/Item_based")
    public ResBody Item_based() throws Exception {

        ResBody resBody = new ResBody();
        // step:1 构建模型 2 计算相似度 3 查找k紧邻 4 构造推荐引擎
        DataModel  model =new FileDataModel(new File("C:/ratingdata.txt"));
        ItemSimilarity similarity =new PearsonCorrelationSimilarity(model);
        Recommender recommender= new GenericItemBasedRecommender(model,similarity);
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
        return resBody;

    }

}
