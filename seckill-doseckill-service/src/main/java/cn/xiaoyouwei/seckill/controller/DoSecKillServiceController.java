package cn.xiaoyouwei.seckill.controller;


import cn.xiaoyouwei.seckill.entity.Order;
import cn.xiaoyouwei.seckill.exception.SecKillException;
import cn.xiaoyouwei.seckill.service.DoSecKillService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;


import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
public class DoSecKillServiceController {
    @Resource
    DoSecKillService doSecKillService;
    @Resource
    RestTemplate restTemplate;

    String orderNo = "";

    @RequestMapping("/seckill")
    @ResponseBody //json化输出
    public Map processSecKill(Long psid, String userid){
        Map result = new HashMap();
        try {
            doSecKillService.processSecKill(psid,userid,1);
//            抢购确权成功后，将订单发送给消息队列
            orderNo = doSecKillService.sendOrderToQueue(userid);
            Map data = new HashMap();
            data.put("orderNo",orderNo);
            //封装操作成功代码
            result.put("code","0");
            result.put("message","success");
            result.put("data",data);
        }catch (SecKillException e){
            //操作失败逻辑
            result.put("code","500");
            result.put("message",e.getMessage());
        }

        return result;

    }

    @GetMapping("/checkorder")
    @CrossOrigin
    public ModelAndView checkOrder(@RequestParam("orderNo") String orderNo){

        //逻辑：检查数据库里是不是有订单记录，有记录说明已经创建成成功
        Order order = restTemplate.getForObject("http://localhost:8004/getOrder/"+orderNo,Order.class);
//        Order order = promotionSecKillService.checkOrder(orderNo);
        ModelAndView mav = new ModelAndView();
        if (order != null) {

            mav.addObject("order",order);
            mav.setViewName("/order");

        }else {
            mav.addObject("orderNo",orderNo);
            mav.setViewName("/waiting");
        }

        return mav;


    }

    @RequestMapping("/doSecKill")
    public ModelAndView doSecKill(){
        ModelAndView mv = new ModelAndView("seckill.html");
        return mv;
    }

}
