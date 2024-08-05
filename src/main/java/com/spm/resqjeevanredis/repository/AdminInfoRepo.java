package com.spm.resqjeevanredis.repository;

import com.spm.resqjeevanredis.entity.AdminInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminInfoRepo extends CrudRepository<AdminInfo, String> {
    AdminInfo findByAdminName(String adminName);
}
