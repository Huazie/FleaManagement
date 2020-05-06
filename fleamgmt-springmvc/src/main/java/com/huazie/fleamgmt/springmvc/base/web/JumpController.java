package com.huazie.fleamgmt.springmvc.base.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p> 页面跳转Controller </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Controller
public class JumpController extends FleaMgmtCommonController{

    private static final Logger LOGGER = LoggerFactory.getLogger(JumpController.class);

    /**
     * <p> 跳转首页 </p>
     *
     * @return 首页字符串
     * @since 1.0.0
     */
    @RequestMapping("jump!index.flea")
    public String index() {
        LOGGER.debug("JumpController##index() start");
        LOGGER.debug("JumpController##index() Just For jumping to index.html");
        LOGGER.debug("JumpController##index() end");
        return "index";
    }
}