import palette from '@styles/colors';

// TODO: 페이지별에서 onClick 함수 다르게 설정하기
export const DETAIL_STATUS_MENU = [
  {
    id: 0,
    title: '판매중 상태로 전환',
    style: `color: ${palette.neutral.text};`,
  },
  {
    id: 1,
    title: '예약중 상태로 전환',
    style: `color: ${palette.neutral.text};`,
  },
  {
    id: 2,
    title: '판매 완료 상태로 전환',
    style: `color: ${palette.neutral.text}`,
  },
];

export const DETAIL_VIEWMORE_MENU = [
  {
    id: 'edit',
    title: '게시글 수정',
    style: `color: ${palette.system.default}`,
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
    style: `color: ${palette.system.default}`,
  },
  {
    id: 'onSale',
    title: '판매중 상태로 전환',
    style: `color: ${palette.system.default}`,
  },
  {
    id: 'soldOut',
    title: '판매 완료 상태로 전환',
    style: `color: ${palette.system.default}`,
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
    style: `color: ${palette.system.default}`,
  },
  {
    id: 'report',
    title: '신고하기',
    style: `color: ${palette.system.default}`,
  },
  {
    id: 'quitChat',
    title: '채팅방 나가기',
    style: `color: ${palette.system.default}`,
  },
];

export const REGION_MENU = [
  {
    id: 'selectLocale',
    title: '내 동네 설정하기',
    style: 'font-weight: 400',
  },
];
