package com.acciojobs.bms_august.services;

import com.acciojobs.bms_august.models.GlobalRecord;

public interface EntityService<T>{
    public T saveOrUpdate(T record);
}
