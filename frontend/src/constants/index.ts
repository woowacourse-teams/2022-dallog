const API = {
  CATEGORY_GET_SIZE: 4,
};

const API_URL = process.env.API_URL;

const ATOM_KEY = {
  USER: 'userState',
};

const CACHE_KEY = {
  AUTH: 'auth',
  CATEGORIES: 'categories',
  ENTER: 'enter',
  SCHEDULES: 'schedules',
  PROFILE: 'profile',
  SUBSCRIPTIONS: 'subscriptions',
};

const CONFIRM_MESSAGE = {
  UNSUBSCRIBE: '구독을 해제하시겠습니까?',
};

const DAYS = ['일', '월', '화', '수', '목', '금', '토'];

const STORAGE_KEY = {
  ACCESS_TOKEN: 'accessToken',
};

const PATH = {
  MAIN: '/',
  AUTH: '/oauth',
  CATEGORY: '/category',
  PROFILE: '/profile',
};

export { API, API_URL, ATOM_KEY, CACHE_KEY, CONFIRM_MESSAGE, DAYS, STORAGE_KEY, PATH };
