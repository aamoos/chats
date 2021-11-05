package com.chatting.repository;

import com.chatting.entity.ChatRoomContent;
import com.chatting.entity.ChatRoomContentFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomContentFileRepository extends CrudRepository<ChatRoomContentFile, Long> {
}
