FROM ubuntu:latest

# 비밀번호 없는 사용자 추가 (보안 강화)
RUN useradd -m coder

# 필요한 패키지 설치
RUN apt-get update && \
    apt-get install -y openjdk-11-jdk python3 python3-pip g++ time && \
    pip3 install numpy && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Python 실행 파일 경로 추가
ENV PATH="/usr/bin/python3:${PATH}"

# 작업 디렉토리 설정 및 사용자 변경
WORKDIR /home/coder
USER coder

# 컨테이너 실행 시 실행할 명령어
CMD ["bash"]
