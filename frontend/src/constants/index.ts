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
  GOOGLE_CALENDAR: 'googleCalendar',
  MY_CATEGORIES: 'myCategories',
  PROFILE: 'profile',
  SCHEDULE: 'schedule',
  SCHEDULES: 'schedules',
  SUBSCRIPTIONS: 'subscriptions',
  VALIDATE: 'validate',
};

const CALENDAR = {
  MAX_VIEW: 10,
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

const VALIDATION_SIZE = {
  MIN_LENGTH: 1,
  SCHEDULE_MEMO_MAX_LENGTH: 255,
  SCHEDULE_TITLE_MAX_LENGTH: 50,
  CATEGORY_NAME_MAX_LENGTH: 20,
};

export { ATOM_KEY, CACHE_KEY, CALENDAR, SELECTOR_KEY, STORAGE_KEY, PATH, VALIDATION_SIZE };
