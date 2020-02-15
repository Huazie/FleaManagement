package com.huazie.fleamgmt.springmvc.login.web;

import com.huazie.fleamgmt.constant.FleaMgmtConstants;
import com.huazie.fleamgmt.springmvc.base.web.FleaMgmtCommonController;
import com.huazie.frame.auth.base.user.entity.FleaAccount;
import com.huazie.frame.auth.common.pojo.user.login.FleaUserLoginPOJO;
import com.huazie.frame.auth.common.service.interfaces.IFleaUserLoginSV;
import com.huazie.frame.common.FleaFrameManager;
import com.huazie.frame.common.IFleaUser;
import com.huazie.frame.common.exception.CommonException;
import com.huazie.frame.common.pojo.OutputCommonData;
import com.huazie.frame.common.util.ObjectUtils;
import com.huazie.frame.jersey.client.core.FleaJerseyClientConfig;
import com.huazie.frame.jersey.common.FleaUserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p> 跳主登录Controller </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Controller
public class FleaMgmtLoginController extends FleaMgmtCommonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FleaMgmtLoginController.class);

    private IFleaUserLoginSV fleaUserLoginSV;

    @Resource(name = "fleaUserLoginSV")
    public void setFleaUserLoginSV(IFleaUserLoginSV fleaUserLoginSV) {
        this.fleaUserLoginSV = fleaUserLoginSV;
    }

    @RequestMapping("fleaMgmtLogin!login.flea")
    @ResponseBody
    public OutputCommonData login(@RequestParam("fleaUserLoginPOJO.accountCode") String accountCode,
                                  @RequestParam("fleaUserLoginPOJO.accountPwd") String accountPwd,
                                  HttpServletRequest request,
                                  HttpSession session) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("FleaMgmtLoginController##login() start");
        }

        OutputCommonData result = new OutputCommonData();

        try {
            // 跳主的登录
            FleaUserLoginPOJO fleaUserLoginPOJO = new FleaUserLoginPOJO();
            fleaUserLoginPOJO.setAccountCode(accountCode);
            fleaUserLoginPOJO.setAccountPwd(accountPwd);
            FleaAccount fleaAccount = fleaUserLoginSV.login(fleaUserLoginPOJO);

            if (ObjectUtils.isNotEmpty(fleaAccount)) {
                // 初始化用户信息
                initUserInfo(fleaAccount);

                // 在这边记录登陆日志
                fleaUserLoginSV.saveLoginLog(fleaAccount.getAccountId(), request);
                result.setRetCode(FleaMgmtConstants.ReturnCodeConstants.RETURN_CODE_Y);
                result.setRetMess("亲，恭喜您登录成功呦");
            }

        } catch (CommonException e) {
            result.setRetCode(FleaMgmtConstants.ReturnCodeConstants.RETURN_CODE_N);
            result.setRetMess(e.getMessage());
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("FleaMasterLoginAction##login() end");
        }

        return result;
    }

    /**
     * <p> 初始化用户信息 </p>
     *
     * @param fleaAccount 账户信息
     * @since 1.0.0
     */
    private void initUserInfo(FleaAccount fleaAccount) {
        IFleaUser fleaUser = FleaFrameManager.getManager().getUserInfo();
        if (ObjectUtils.isEmpty(fleaUser)) {
            fleaUser = new FleaUserImpl();

            if (ObjectUtils.isNotEmpty(fleaAccount)) {
                fleaUser.setAcctId(fleaAccount.getAccountId());
                String systemAcctId = FleaJerseyClientConfig.getSystemAcctId();
                if (ObjectUtils.isNotEmpty(systemAcctId)) {
                    fleaUser.setSystemAcctId(Long.valueOf(systemAcctId));
                }
            }

            FleaFrameManager.getManager().setUserInfo(fleaUser);
        }
    }

}
