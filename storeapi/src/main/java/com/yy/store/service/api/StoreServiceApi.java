package com.yy.store.service.api;

import java.util.Date;

public interface StoreServiceApi {

    //查询版本号
    int selectVersion(String supplierId, String goodsId);

    //更新库存
    int updateStoreCountByVersion(int version, String supplierId, String goodsId, String updateBy, Date updateTime);

    //查询库存
    int selectStoreCount(String supplierId, String goodsId);
}
