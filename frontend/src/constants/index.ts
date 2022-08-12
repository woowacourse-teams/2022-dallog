const API = {
  CATEGORY_GET_SIZE: 4,
};

const API_URL = process.env.API_URL;

const ATOM_KEY = {
  SNACK_BAR: 'snackBarState',
  SIDE_BAR: 'sideBarState',
  USER: 'userState',
};

const CACHE_KEY = {
  AUTH: 'auth',
  CATEGORIES: 'categories',
  ENTER: 'enter',
  MY_CATEGORIES: 'myCategories',
  PROFILE: 'profile',
  SCHEDULES: 'schedules',
  SUBSCRIPTIONS: 'subscriptions',
};

const CONFIRM_MESSAGE = {
  DELETE_CATEGORY: '정말 삭제하시겠습니까?',
  UNSUBSCRIBE: '구독을 해제하시겠습니까?',
  LOGOUT: '로그아웃하시겠습니까?',
};

const DAYS = ['일', '월', '화', '수', '목', '금', '토'];

const SELECTOR_KEY = {
  SIDE_BAR: 'sideBarSelector',
};

const STORAGE_KEY = {
  ACCESS_TOKEN: 'accessToken',
};

const PALETTE = [
  '#AD1457',
  '#D81B60',
  '#D50000',
  '#E67C73',
  '#F4511E',
  '#EF6C00',
  '#F09300',
  '#F6BF26',
  '#E4C441',
  '#C0CA33',
  '#7CB342',
  '#33B679',
  '#0B8043',
  '#009688',
  '#039BE5',
  '#4285F4',
  '#3F51B5',
  '#7986CB',
  '#B39DDB',
  '#9E69AF',
  '#8E24AA',
  '#795548',
  '#616161',
  '#A79B8E',
];

const PATH = {
  MAIN: '/',
  AUTH: '/oauth',
  CATEGORY: '/category',
};

const VALIDATION_MESSAGE = {
  STRING_LENGTH: (min: number, max: number) => `${min}자 ~ ${max}자로 입력해주세요`,
  EARLY_END_DATE_TIME: '시작날짜보다 이후로 설정해주세요.',
};

export {
  API,
  API_URL,
  ATOM_KEY,
  CACHE_KEY,
  CONFIRM_MESSAGE,
  DAYS,
  SELECTOR_KEY,
  STORAGE_KEY,
  PALETTE,
  PATH,
  VALIDATION_MESSAGE,
};
