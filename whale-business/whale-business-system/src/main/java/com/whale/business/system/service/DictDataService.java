package com.whale.business.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whale.business.system.domain.DictData;


import java.util.List;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 * @author entfrm
 * @since 2019-01-30
 */
public interface DictDataService extends IService<DictData> {

    public List<DictData> getDictDataList(String dictType);
}
