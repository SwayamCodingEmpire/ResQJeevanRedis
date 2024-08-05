package com.spm.resqjeevanredis.service;

import com.spm.resqjeevanredis.dto.RequesterDto;
import com.spm.resqjeevanredis.dto.ResourceDepotDto;
import com.spm.resqjeevanredis.entity.ResourceDepot;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface ControlRoomService {
    HashMap<String,Long> distributeRequestedResources(RequesterDto requesterDto);
}
