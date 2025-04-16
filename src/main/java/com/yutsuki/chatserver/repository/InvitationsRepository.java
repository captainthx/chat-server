package com.yutsuki.chatserver.repository;

import com.yutsuki.chatserver.entity.Invitations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitationsRepository extends JpaRepository<Invitations,Long> {

}
