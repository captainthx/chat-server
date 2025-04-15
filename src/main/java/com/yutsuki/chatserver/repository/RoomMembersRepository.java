package com.yutsuki.chatserver.repository;

import com.yutsuki.chatserver.entity.RoomMembers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomMembersRepository extends JpaRepository<RoomMembers,Long> {
}
