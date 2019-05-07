/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */
package cn.vbill.middleware.porter.manager.service.impl;

import cn.vbill.middleware.porter.manager.core.dto.ControlPageVo;
import cn.vbill.middleware.porter.manager.core.dto.OwnerVo;
import cn.vbill.middleware.porter.manager.core.entity.CUser;
import cn.vbill.middleware.porter.manager.core.entity.DicControlTypePlugin;
import cn.vbill.middleware.porter.manager.core.entity.NodesOwner;
import cn.vbill.middleware.porter.manager.core.mapper.NodesOwnerMapper;
import cn.vbill.middleware.porter.manager.service.CUserService;
import cn.vbill.middleware.porter.manager.service.DicControlTypePluginService;
import cn.vbill.middleware.porter.manager.service.DictService;
import cn.vbill.middleware.porter.manager.service.NodesOwnerService;
import cn.vbill.middleware.porter.manager.web.page.Page;
import cn.vbill.middleware.porter.manager.web.rcc.RoleCheckContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 节点所有权控制表 服务实现类
 *
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-20 16:29:06
 */
@Service
public class NodesOwnerServiceImpl implements NodesOwnerService {

    @Autowired
    private NodesOwnerMapper nodesOwnerMapper;

    @Autowired
    private CUserService cUserService;

    @Autowired
    private DictService dictService;

    @Autowired
    protected DicControlTypePluginService dicControlTypePluginService;

    @Override
    public ControlPageVo makeControlPage(Long nodeId) {

        // ownerType=1:节点所有者  shareType=2:节点共享者
        OwnerVo owner = checkOwner(cUserService.selectOwnersByNodeId(nodeId, 1));
        List<OwnerVo> shareOwner = checkShares(cUserService.selectOwnersByNodeId(nodeId, 2));

        // 操作类型枚举
        Map<String, Object> dictControlType = dictService.dictControlType();

        // 查询当前登录人type
        Boolean isManager = checkLoginRole();
        Integer type = isManager ? 0 : nodesOwnerMapper.findOwnerTypeByNodeIdAndUserId(nodeId, RoleCheckContext.getUserIdHolder().getUserId());

        // 根据Type查出操作按钮字典
        List<DicControlTypePlugin> dicControlTypePlugins = dicControlTypePluginService.findByType(type);

        // 组装ControlPageVo并返回
        ControlPageVo controlPageVo = new ControlPageVo(owner, shareOwner, dictControlType, dicControlTypePlugins);
        return controlPageVo;
    }

    /**
     * 组装任务所有者
     *
     * @author MurasakiSeiFu
     * @date 2019-05-07 13:55
     * @param: [userOwner]
     * @return: cn.vbill.middleware.porter.manager.core.dto.OwnerVo
     */
    private OwnerVo checkOwner(List<CUser> userOwner) {
        return userOwner.isEmpty() ? null : new OwnerVo(userOwner.get(0), 1);
    }

    /**
     * 组装任务共享者
     *
     * @author MurasakiSeiFu
     * @date 2019-05-07 13:59
     * @param: [userShares]
     * @return: java.util.List<cn.vbill.middleware.porter.manager.core.dto.OwnerVo>
     */
    private List<OwnerVo> checkShares(List<CUser> userShares) {
        if (userShares.isEmpty()) {
            return null;
        }
        List<OwnerVo> shareOnwer = new ArrayList<>();
        for (CUser userShare : userShares) {
            OwnerVo owner = new OwnerVo(userShare, 2);
            shareOnwer.add(owner);
        }
        return shareOnwer;
    }

    /**
     * 判断当前登录用户是否为管理员
     *
     * @author MurasakiSeiFu
     * @date 2019-04-04 13:48
     * @param: []
     * @return: java.lang.Boolean
     */
    private Boolean checkLoginRole() {
        String roleCode = RoleCheckContext.getUserIdHolder().getRoleCode();
        return ("A0001".equals(roleCode) || "A0002".equals(roleCode));
    }

    @Override
    public void insertByNodes(Long nodeId) {
        NodesOwner nodesOwner = new NodesOwner();
        nodesOwner.setNodeId(nodeId);
        nodesOwner.setOwnerId(RoleCheckContext.getUserIdHolder().getUserId());
        nodesOwnerMapper.insert(nodesOwner);
    }
}