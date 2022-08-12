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

export { API, API_URL, ATOM_KEY, CACHE_KEY, SELECTOR_KEY, STORAGE_KEY, PATH };
