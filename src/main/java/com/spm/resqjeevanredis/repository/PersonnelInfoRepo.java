package com.spm.resqjeevanredis.repository;

import com.spm.resqjeevanredis.entity.PersonnelInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonnelInfoRepo extends CrudRepository<PersonnelInfo,String> {
}
