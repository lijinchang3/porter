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
package cn.vbill.middleware.porter.manager.service;

import cn.vbill.middleware.porter.manager.core.dto.ControlPageVo;
import cn.vbill.middleware.porter.manager.core.entity.NodesOwner;
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 节点所有权控制表 服务接口类
 *
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-20 16:29:06
 */
public interface NodesOwnerService {

    /**
     * 权限设置页面数据组
     *
     * @param nodeId
     * @return
     */
    ControlPageVo makeControlPage(Long nodeId);

    /**
     * 新增节点同时更新节点所有权控制
     *
     * @param nodeId
     */
    void insertByNodes(Long nodeId);
}