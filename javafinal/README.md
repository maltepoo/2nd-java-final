# API 명세
- GET 교수자가 담당하고 있는 강의의 신청 현황을 조회
  - /apply?lectureId={id}?memberId={id}
- POST 사전수강신청
  - /basket?lectureId={id}?memberId={id}
- DELETE 사전수강신청취소
  - /basket?lectureId={id}?memberId={id}
- POST 본수강신청
  - /apply?lectureId={id}?memberId={id}
- DELETE 본수강신청취소
  - /apply?lectureId={id}?memberId={id}
** (사전, 일반)수강신청 실패시 exception으로 response에는 값이 없음
