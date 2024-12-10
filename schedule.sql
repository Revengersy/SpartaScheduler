-- 데이터베이스 사용
USE schedule;

-- Writer 테이블 생성
CREATE TABLE Writer (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '쓴 사람 id', -- 기본 키, 자동 증가
                        writer_name VARCHAR(100) NOT NULL COMMENT '작성자 이름',
                        email VARCHAR(255) COMMENT '이메일',
                        registered_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
                        updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일'
);

-- Schedule 테이블 생성
CREATE TABLE Schedule (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '스케줄id', -- 기본 키, 자동 증가
                          task VARCHAR(255) NOT NULL COMMENT '일정',          -- 작업 내용 (255자 제한)
                          writer_id BIGINT NOT NULL COMMENT '작성자 ID',      -- Writer 테이블과 연결하는 외래 키
                          password VARCHAR(255) NOT NULL COMMENT '비밀번호',   -- 비밀번호 (255자 제한)
                          written_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '작성 시간',
                          edited_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시간',
                          FOREIGN KEY (writer_id) REFERENCES Writer(id) -- 외래 키 제약 조건
);