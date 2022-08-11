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
  CATEGORY: 'category',
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

const PATH = {
  MAIN: '/',
  AUTH: '/oauth',
  CATEGORY: '/category',
};

const VALIDATION_MESSAGE = {
  ONE_TO_TWENTY_LENGTH: '1자 ~ 20자로 입력해주세요.',
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
  PATH,
  VALIDATION_MESSAGE,
};
