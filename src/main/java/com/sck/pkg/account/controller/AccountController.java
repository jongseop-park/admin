package com.sck.pkg.account.controller;

import com.sck.core.domain.Paging;
import com.sck.core.domain.Search;
import com.sck.domain.Admin;
import com.sck.domain.Member;
import com.sck.pkg.account.service.AdminAccService;
import com.sck.pkg.account.service.MemberAccService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AdminAccService adminAccService;

    @Autowired
    private MemberAccService memberAccService;

    @RequestMapping("/{type}/list")
    public String list(@ModelAttribute("conn")Search condition, Model model, @PathVariable("type") String type){
        String path = "account/"+type+"/list";
        Paging paging = new Paging();
        paging.setCriteria(condition);

        if(type.equals("admin")){ // 회원타입 admin
            List<Admin> result = adminAccService.page(condition);
            model.addAttribute("result",result);
            paging.setTotalCount(adminAccService.listTotalCount(condition));
        } else if(type.equals("member")){ // 회원타입 member
            List<Member> result = memberAccService.page(condition);
            model.addAttribute("result",result);
            paging.setTotalCount(memberAccService.listTotalCount(condition));
        }

        model.addAttribute("paging",paging);
        return path;
    }

    @RequestMapping("/{type}/form")
    public String form(@PathVariable String type,Model model,Admin admin, Member member){
        String path = "account/"+type+"/form";


        if (type.equals("admin")) {
            model.addAttribute("result",adminAccService.findByDetail(admin));
        } else if (type.equals("member")) {
            model.addAttribute("result",memberAccService.findByDetail(member));
        }
        
        return path;
    }

    @RequestMapping("/{type}/update")
    public String failCntReset(@PathVariable String type, @RequestParam Map<String, String> params, Model model){
        String path = "account/" + type + "/form";
        String id = params.get("id");
        String btnType = params.get("btnType");

        if (type.equals("admin")){ // 타입 Admin
            switch (btnType) {
                case "failCntReset":  // 비밀번호 실패횟수 초기화
                    adminAccService.failCntReset(id);
                    break;
                case "passwordReset":
                    adminAccService.passwordReset(id);
                    break;
                case "lockYn":
                    adminAccService.accountIsLock(id);
                    break;
            }
        } else if (type.equals("member")) {

        }

        return path;
    }

}
