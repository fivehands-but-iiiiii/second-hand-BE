import palette from '@styles/colors';

export const DETAIL_STATUS_MENU = [
  {
    id: 0,
    title: '판매중 상태로 전환',
  },
  {
    id: 1,
    title: '예약중 상태로 전환',
  },
  {
    id: 2,
    title: '판매 완료 상태로 전환',
  },
];

export const DETAIL_VIEWMORE_MENU = [
  {
    id: 'edit',
    title: '게시글 수정',
  },
  {
    id: 'delete',
    title: '삭제',
    style: `color: ${palette.system.warning}`,
  },
];

export const SALESHISTORY_VIEWMORE_MENU = [
  {
    id: 'edit',
    title: '게시글 수정',
  },
  {
    id: 'onSale',
    title: '판매중 상태로 전환',
  },
  {
    id: 'soldOut',
    title: '판매 완료 상태로 전환',
  },
  {
    id: 'delete',
    title: '삭제',
    style: `color: ${palette.system.warning}`,
  },
];

export const CHAT_VIEWMORE_MENU = [
  {
    id: 'alarmOff',
    title: '알람끄기',
  },
  {
    id: 'report',
    title: '신고하기',
  },
  {
    id: 'quitChat',
    title: '채팅방 나가기',
  },
];

export const REGION_MENU = [
  {
    id: 'selectLocale',
    title: '내 동네 설정하기',
  },
];
