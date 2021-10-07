package com.buka.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.buka.dao.MemberDao;
import com.buka.pojo.Member;
import com.buka.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if(password!=null){
            //加密
        }
        memberDao.add(member);
    }
}
