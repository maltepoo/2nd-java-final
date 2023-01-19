package starters.javafinal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import starters.javafinal.entity.Apply;

import java.util.List;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {
    List<Apply> findAllByMemberId(Long memberId);

    List<Apply> findAllByLectureId(Long lectureId);

    Integer countAllByLectureId(Long lectureId);
}
