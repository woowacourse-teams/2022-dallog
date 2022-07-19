const API = {
  CATEGORY_GET_SIZE: 4,
};

const CACHE_KEY = {
  AUTH: 'auth',
  CATEGORIES: 'categories',
  ENTER: 'enter',
  SCHEDULES: 'schedules',
  PROFILE: 'profile',
};

const STORAGE_KEY = {
  ACCESS_TOKEN: 'accessToken',
};

const PATH = {
  CALENDAR_PAGE: '/auth',
  CATEGORY: '/category',
  START_PAGE: '/start',
  PROFILE: '/profile',
};

const DAYS = ['일', '월', '화', '수', '목', '금', '토'];

export { API, CACHE_KEY, DAYS, STORAGE_KEY, PATH };
