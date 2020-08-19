package cn.xiaoyouwei.seckill.web;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
public class AntiRefreshInterceptor implements HandlerInterceptor {

    @Resource
    RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = request.getRemoteAddr();
        //单独使用IP作为判断标准太过宽泛，应该在细化条件，应该加入当前用户的浏览器信息
        String userAgent = request.getHeader("User-Agent");
        String key1 = clientIp + "_" + userAgent;
        //key1太长，利用Common-codes包转成MD5摘要
        String key = "anti:refresh:"+DigestUtils.md5Hex(key1);
        response.setContentType("text/html;charset=utf-8");
        //查看当前访问IP是否存在黑名单中
        if (redisTemplate.hasKey("anti:refresh:blacklist")){
            if (redisTemplate.opsForSet().isMember("anti:refresh:blacklist",clientIp)){
                return false;
            }
        }

        //获取该地址访问的次数，Redis做计数器
        Integer nums = (Integer) redisTemplate.opsForValue().get(key);
        if (nums == null){ //第一次访问
            redisTemplate.opsForValue().set(key,1l,60,TimeUnit.SECONDS);
        }else { //不是第一次访问，则访问次数加1

            if (nums > 30 && nums < 100){ //访问过于频繁
                response.getWriter().println("请求过与频繁，请稍后再试！");
                redisTemplate.opsForValue().increment(clientIp,1l);
                return false;
            }else if (nums >= 100){ //恶意攻击
                response.getWriter().println("检测到您的IP访问异常，已被加入黑名单");
                //利用Redis实现黑名单.一天不能访问
                redisTemplate.opsForSet().add("anti:refresh:blacklist",clientIp,24, TimeUnit.HOURS);
                return false;
            }else { //正常访问
                redisTemplate.opsForValue().increment(key,1l);

            }

        }
        return true;

    }
}
