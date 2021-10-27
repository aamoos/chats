package com.chatting.repository;


import com.chatting.entity.File;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {
    File findByFileIdx(Long fileIdx);
}
