const API = {
  AUTH_CODE_KEY: 'code',
  CATEGORY_GET_SIZE: 4,
};

const API_URL = process.env.API_URL;

const CACHE_KEY = {
  CATEGORIES: 'categories',
  CATEGORY: 'category',
  ENTER: 'enter',
  GOOGLE_CALENDAR: 'googleCalendar',
  MY_CATEGORIES: 'myCategories',
  PROFILE: 'profile',
  SCHEDULE: 'schedule',
  SCHEDULES: 'schedules',
  SUBSCRIPTIONS: 'subscriptions',
  SUBSCRIBERS: 'subscribers',
  VALIDATE: 'validate',
  ADMIN_CATEGORIES: 'adminCategories',
  EDITABLE_CATEGORIES: 'editableCategories',
  LOGIN_AGAIN: 'loginAgain',
};

const RESPONSE = {
  STATUS: {
    UNAUTHORIZED: 401,
  },
};

export { API, API_URL, CACHE_KEY, RESPONSE };
