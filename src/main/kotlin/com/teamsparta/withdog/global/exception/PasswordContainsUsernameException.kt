package com.teamsparta.withdog.global.exception

class PasswordContainsUsernameException : RuntimeException("비밀번호에 아이디를 포함할 수 없습니다.")