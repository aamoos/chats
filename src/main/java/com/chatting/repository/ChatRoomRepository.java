package com.chatting.repository;
import com.chatting.entity.ChatRoom;
import com.chatting.entity.FriendsInvite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatRoomRepository extends CrudRepository<ChatRoom, Long> {
    ChatRoom findByChatRoomIdx(Long chatRoomIdx);
}
