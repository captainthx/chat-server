package com.yutsuki.chatserver.repository;

import com.yutsuki.chatserver.entity.RoomMembers;
import com.yutsuki.chatserver.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomMembersRepository extends JpaRepository<RoomMembers,Long> {
    List<RoomMembers> findByRoomId(Long roomId);

    Page<RoomMembers> findAllByUser(User user, Pageable pageable);
}
