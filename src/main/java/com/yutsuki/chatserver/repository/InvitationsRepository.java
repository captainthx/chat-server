package com.yutsuki.chatserver.repository;

import com.yutsuki.chatserver.entity.Invitations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationsRepository extends JpaRepository<Invitations,Long> {
}
