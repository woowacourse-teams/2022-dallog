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
  SCHEDULER: 'scheduler',
  SCHEDULES: 'schedules',
  SUBSCRIPTIONS: 'subscriptions',
  VALIDATE: 'validate',
};

const RESPONSE = {
  STATUS: {
    UNAUTHORIZED: 401,
  },
};

export { API, API_URL, CACHE_KEY, RESPONSE };
