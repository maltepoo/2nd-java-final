package starters.javafinal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import starters.javafinal.entity.Member;

@Repository
public interface MemberRepository  extends JpaRepository<Member, Long> {
}