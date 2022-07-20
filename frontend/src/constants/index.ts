const API = {
  CATEGORY_GET_SIZE: 4,
};

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

const STORAGE_KEY = {
  ACCESS_TOKEN: 'accessToken',
};

const PATH = {
  MAIN: '/',
  AUTH: '/oauth',
  CATEGORY: '/category',
  PROFILE: '/profile',
};

const DAYS = ['일', '월', '화', '수', '목', '금', '토'];

export { API, ATOM_KEY, CACHE_KEY, DAYS, STORAGE_KEY, PATH };
