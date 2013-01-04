/*
 * Copyright Notice ====================================================
 * This file contains proprietary information of Hewlett-Packard Co.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2009   All rights reserved. ======================
 */

package com.hp.sbs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.hp.sbs.mail.bean.MailSenderInfo;

public class EmailServiceTest
{
    private static EmailService service;

    @BeforeClass
    public static void setUpBeforeClass()
    {
        service = new EmailService();
    }

    @Test
    public void testSendEmailOneByOne()
    {
        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setFromAddress("wen-bin.zhang@hp.com");
        mailInfo.setSubject("Test Send Email One by One");
        mailInfo.setToAddress(createEmailList());
        mailInfo.setTemplateName("emailReminder.ftl");
        mailInfo.setTemplateMapping(createTemplate());
        service.sendFreemarkEmail(mailInfo, false);
    }

    @Test
    public void testSendEmailInGroup()
    {
        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setFromAddress("wen-bin.zhang@hp.com");
        mailInfo.setSubject("Test Send Email In Group");
        mailInfo.setToAddress(createEmailList());
        mailInfo.setContent("This is a test");
        service.sendFreemarkEmail(mailInfo, true);
    }

    private List<String> createEmailList()
    {
        List<String> emailAddress = new ArrayList<String>();
        emailAddress.add("wen-bin.zhang@hp.com");
        emailAddress.add("kid_zhwb@163.com");
        emailAddress.add("zhwbqd@gmail.com");
        return emailAddress;
    }

    private Map<String, Object> createTemplate()
    {
        Map<String, Object> template = new HashMap<String, Object>();
        template.put("nowDate", new Date());
        template.put("companyName", "ABC");
        template.put("userName", "Jack");
        template.put("telphone", "021-38898775");
        template.put("location", "ZhangJiang HighTech, Jinke Road #2517, Shanghai, China");
        return template;

    }
}