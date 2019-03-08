package com.tensquare.friend.controller;

import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 37269 on 2018/7/11.
 */
@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/like/{friendid}/{type}" ,method = RequestMethod.PUT)
    public Result addFriend(@PathVariable String friendid,@PathVariable String type){
        //获取当前登录人信息
        Claims claims=(Claims)request.getAttribute("user_claims");
        if(claims==null){
            return new Result(false, StatusCode.ACCESSERROR,"请登录！");
        }

        if(type.equals("1")){//添加好友
            if(friendService.addFriend(claims.getId(),friendid)==0){
                return new Result(false,StatusCode.REPERROR,"你已经添加此好友");
            }
        }else{//添加非好友
            friendService.addNoFriend(claims.getId(),friendid);
        }
        return new Result(true,StatusCode.OK,"操作成功");

    }

    @RequestMapping(value = "/{friendid}" ,method = RequestMethod.DELETE)
    public Result deleteFriend(@PathVariable  String friendid){
        //获取当前登录人信息
        Claims claims=(Claims)request.getAttribute("user_claims");
        if(claims==null){
            return new Result(false, StatusCode.ACCESSERROR,"请登录");
        }

        friendService.deleteFriend( claims.getId(), friendid);
        return new Result(true,StatusCode.OK,"删除成功");
    }

}
