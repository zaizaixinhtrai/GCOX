package com.gcox.fansmeet.domain.models;

import java.util.List;

/**
 * Created by thanhbc on 10/25/17.
 */

public class BasePagingModel<T> {
    public int nextId;
    public boolean isEnd;
    public int totalRecords;
    public List<T> data;
}
