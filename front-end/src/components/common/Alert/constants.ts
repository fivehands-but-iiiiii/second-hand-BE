export const ALERT_TITLE = {
  DELETE: (subject: string) => `${subject}을 삭제하시겠어요?`,
  EXIT: '채팅방을 나가시겠어요?',
  LOGOUT: '로그아웃을 하시겠어요?',
  LOGIN: '로그인이 필요해요.',
};

export const ALERT_ACTIONS = {
  DELETE: [
    { id: 'cancel', action: '취소' },
    { id: 'delete', action: '삭제' },
  ],
  EXIT: [
    { id: 1, action: '취소' },
    { id: 2, action: '나가기' },
  ],
  LOGOUT: [
    { id: 1, action: '취소' },
    { id: 2, action: '로그아웃' },
  ],
  LOGIN: [
    { id: 1, action: '홈으로' },
    { id: 2, action: '로그인' },
  ],
};
