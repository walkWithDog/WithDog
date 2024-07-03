package com.teamsparta.withdog.domain.exception

class UsernameInvalidException : RuntimeException("아이디는 최소 3자 이상 10자 이하, 알파벳 대소문자, 숫자를 포함합니다.")