import DAILY from '@/assets/image/icons/DAILY.png';
import UNIVERSITY from '@/assets/image/icons/UNIVERSITY.png';
import COMMUTE from '@/assets/image/icons/COMMUTE.png';
import CONCERT from '@/assets/image/icons/CONCERT.png';
import AIRPORT from '@/assets/image/icons/AIRPORT.png';
import TRAVEL from '@/assets/image/icons/TRAVEL.png';
import RESERVIST from '@/assets/image/icons/RESERVIST.png';

// NOTE : 바텀시트가 최소로 내려갔을 때의 y 값
// export const BOTTOM_SHEET_MIN_Y = 64 - 53;
// NOTE :  바텀시트가 최대로 높이 올라갔을 때의 y 값
// export const BOTTOM_SHEET_MAX_Y = window.innerHeight - 215;
// NOTE 바텀시트의 세로 길이
// export const BOTTOM_SHEET_HEIGHT = window.innerHeight + 200;
// NOTE : 윈도우의 높이
export const WINDOW_HEIGHT = window.innerHeight;
// NOTE : 상단 헤더의 높이
export const HEADER_HEIGHT = 64;

export const DRAWER_HEIGHT = 200;
// NOTE : 카테고리 모음
export const Category = [
  {
    tag: 'DAILY',
    name: '일상',
  },
  {
    tag: 'UNIVERSITY',
    name: '대학교',
  },
  {
    tag: 'COMMUTE',
    name: '출퇴근',
  },
  {
    tag: 'CONCERT',
    name: '콘서트',
  },
  {
    tag: 'AIRPORT',
    name: '공항',
  },
  {
    tag: 'TRAVEL',
    name: '여행',
  },
  {
    tag: 'RESERVIST',
    name: '예비군',
  },
];

export const categoryIcons = [DAILY, UNIVERSITY, COMMUTE, CONCERT, AIRPORT, TRAVEL, RESERVIST];
