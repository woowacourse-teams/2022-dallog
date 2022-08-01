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
  LOG_OUT: '로그아웃하시겠습니까?',
};

const DAYS = ['일', '월', '화', '수', '목', '금', '토'];

const SELECTOR_KEY = {
  SIDE_BAR: 'sideBarSelector',
};

const STORAGE_KEY = {
  ACCESS_TOKEN: 'accessToken',
};

const PALETTE = [
  '#FABEC0',
  '#F85C70',
  '#F37970',
  '#E43D40',
  '#F9D876',
  '#FBE39D',
  '#FEDA15',
  '#CBAE11',
  '#3D550C',
  '#81B622',
  '#ECF87F',
  '#59981A',
  '#145DA0',
  '#0C2D48',
  '#2E8BC0',
  '#B1D4E0',
  '#BEAFC2',
  '#695E93',
  '#8155BA',
  '#E43480',
  '#EAD4C0',
  '#BBC4C2',
  '#464033',
  '#7E7C73',
];

const PATH = {
  MAIN: '/',
  AUTH: '/oauth',
  CATEGORY: '/category',
  LOGIN: '/login',
  PROFILE: '/profile',
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
};
