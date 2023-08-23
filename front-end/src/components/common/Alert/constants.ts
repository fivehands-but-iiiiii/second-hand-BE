// TODO: 해당 파일에서 사용하지 않는 type, interface 위치 수정
export type AlertActionType =
  | 'delete'
  | 'cancel'
  | 'home'
  | 'login'
  | 'logout'
  | 'leave';

export interface AlertActionsProps {
  id: AlertActionType;
  action: string;
}

export interface AlertActions {
  [key: string]: AlertActionsProps[];
}

export const ALERT_TITLE = {
  DELETE: (subject: string) => `${subject}을 삭제하시겠어요?`,
  EXIT: '채팅방을 나가시겠어요?',
  LOGOUT: '로그아웃을 하시겠어요?',
  LOGIN: '로그인이 필요해요.',
};

export const ALERT_ACTIONS: AlertActions = {
  DELETE: [
    { id: 'cancel', action: '취소' },
    { id: 'delete', action: '삭제' },
  ],
  EXIT: [
    { id: 'cancel', action: '취소' },
    { id: 'leave', action: '나가기' },
  ],
  LOGOUT: [
    { id: 'cancel', action: '취소' },
    { id: 'logout', action: '로그아웃' },
  ],
  LOGIN: [
    { id: 'home', action: '홈으로' },
    { id: 'login', action: '로그인' },
  ],
};
