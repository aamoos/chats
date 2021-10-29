package com.chatting.common;

/**
 * API URL 정의
 */
public final class Url {

    /** 로그인 */
    public static final class AUTH {

        /** 로그인 url */
        public static final String LOGIN = "/auth/login";

        /** 로그인 html */
        public static final String LOGIN_HTML = "auth/login";

        /** 회원가입 url */
        public static final String JOIN = "/auth/join";

        /** 회원가입 HTML */
        public static final String JOIN_HTML = "auth/join";

        /** 중복체크 */
        public static final String DUPLICATE_CHECK = "/auth/duplicateCheck";

        /** 인증번호 저장 */
        public static final String SAVE_SERIAL_NO = "/auth/saveSerialNo";

        /** 인증번호 체크 */
        public static final String SERIAL_NO_CHECK = "/auth/serialNoCheck";

        /** 인증번호 확인 화면 */
        public static final String JOIN_CHECK = "/auth/serialNoCheck";

        /** 회원가입 회원체크 HTML */
        public static final String JOIN_CHECK_HTML = "auth/joinCheck";

        /** 로그인 인증 요청 */
        public static final String LOGIN_PROC = "/auth/login-proc";

        /** 로그아웃 요청 */
        public static final String LOGOUT_PROC = "/auth/logout-proc";

        /** 이메일 발송 */
        public static final String SEND_EMAIL = "/auth/sendEmail";

        /** 아이디 찾기 */
        public static final String FIND_ID_CHECK = "/auth/findIdCheck";

        /** 아이디 찾기 html */
        public static final String FIND_ID_CHECK_HTML = "auth/findIdCheck";

        /** 아이디 찾기 결과 html */
        public static final String FIND_ID_CHECK_RESULT_HTML = "auth/findIdCheckResult";

        /** 비밀번호 찾기 */
        public static final String FIND_PWD_CHECK = "/auth/findPwdCheck";

        /** 비밀번호 찾기 html */
        public static final String FIND_PWD_CHECK_HTML = "auth/findPwdCheck";

        /** 비밀번호 찾기 결과 html */
        public static final String FIND_PWD_CHECK_RESULT_HTML = "auth/findPwdCheckResult";

        /** 비밀번호 초기화 */
        public static final String PWD_RESET = "/auth/pwdReset";
    }

    /** 공통 */
    public static final class COMMON {

        /** 파일 업로드 */
        public static final String FILE_UPLOAD = "/file-upload";

        /** 파일 다운로드 */
        public static final String FILE_DOWNLOAD = "/file-download";

        /** 푸시 발송 */
        public static final String PUSH_SEND = "/pushSend";
    }

    /** 메인 */
    public static final class MAIN {

        /** 메인 */
        public static final String MAIN = "/main";
        public static final String MAIN_HTML = "chat/main";

        /** 이미지 저장 */
        public static final String SAVE_PROFILE_IMG = "/saveProfileImg";

        /** 친구 추가 요청 */
        public static final String FRIENDS_INVITE = "/friendsInvite";

        /** 친구요청 보내는 아이디가 실제로 존재하는지 체크 */
        public static final String FRIENDS_EXIST_CHECK = "/friendsExistCheck";

        /** 친구요청 플래그 업데이트 */
        public static final String RESPONSE_YN_UPDATE = "/responseYnUpdate";

        /** 친구 검색 */
        public static final String SEARCH_FRIENDS = "/searchFriends";

        /** 친구 block 하기 */
        public static final String BLOCK_FRIENDS_DELETE = "/blockFriendsDelete";

        /** 회원탈퇴 */
        public static final String WITHDRAWAL = "/withdrawal";

        /** 채팅 */
        public static final String CHAT = "/chat";
        public static final String CHAT_HTML = "chat/chat";
    }

}
