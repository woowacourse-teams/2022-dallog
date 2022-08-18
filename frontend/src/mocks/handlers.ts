import { rest } from 'msw';

import { CategoryType } from '@/@types/category';
import { ProfileType } from '@/@types/profile';
import { ScheduleType } from '@/@types/schedule';
import { SubscriptionType } from '@/@types/subscription';

import { API, API_URL } from '@/constants/api';
import { CATEGORY_TYPE } from '@/constants/category';

import categoryApi from '@/api/category';
import profileApi from '@/api/profile';
import scheduleApi from '@/api/schedule';
import subscriptionApi from '@/api/subscription';

import {
  categoryDB,
  getScheduleDB,
  myCategoryDB,
  scheduleDB,
  subscriptionDB,
  tigerProfileDB,
} from './data';

const handlers = [
  rest.get(API_URL + categoryApi.endpoint.entire, (req, res, ctx) => {
    const page = parseInt(req.url.searchParams.get('page') as string);
    const size = API.CATEGORY_GET_SIZE;
    const name = req.url.searchParams.get('name');

    if (name === null || name === '') {
      const entireCategories = categoryDB.categories.slice(page * size, page * size + size);

      return res(ctx.status(200), ctx.json({ page, categories: entireCategories }));
    }

    const filteredCategories = categoryDB.categories
      .filter((category) => category.name.includes(name))
      .slice(page * size, page * size + size);

    return res(ctx.status(200), ctx.json({ page, categories: filteredCategories }));
  }),

  rest.post<Pick<CategoryType, 'name'>>(API_URL + categoryApi.endpoint.entire, (req, res, ctx) => {
    const newCategory: CategoryType = {
      ...req.body,
      id: categoryDB.categories.length + 1,
      createdAt: new Date().toISOString().slice(0, -5),
      creator: tigerProfileDB,
      categoryType: CATEGORY_TYPE.NORMAL,
    };

    categoryDB.categories.push(newCategory);
    myCategoryDB.categories.push(newCategory);

    return res(ctx.status(201), ctx.json(newCategory));
  }),

  rest.patch<Pick<CategoryType, 'name'>>(
    `${API_URL}${categoryApi.endpoint.entire}/:id`,
    (req, res, ctx) => {
      const { id } = req.params;
      const categoryId = parseInt(id as string);
      const categoryIndex = categoryDB.categories.findIndex((el) => el.id === categoryId);
      const myCategoryIndex = myCategoryDB.categories.findIndex((el) => el.id === categoryId);
      const subscriptionIndex = subscriptionDB.subscriptions.findIndex(
        (el) => el.category.id === categoryId
      );

      if (categoryIndex < 0 || myCategoryIndex < 0 || subscriptionIndex < 0) {
        return res(ctx.status(404));
      }

      categoryDB.categories[categoryIndex] = {
        ...categoryDB.categories[categoryIndex],
        ...req.body,
      };
      myCategoryDB.categories[myCategoryIndex] = {
        ...myCategoryDB.categories[myCategoryIndex],
        ...req.body,
      };
      subscriptionDB.subscriptions[subscriptionIndex].category = {
        ...subscriptionDB.subscriptions[subscriptionIndex].category,
        ...req.body,
      };

      return res(ctx.status(201));
    }
  ),

  rest.delete<Pick<CategoryType, 'name'>>(
    `${API_URL}${categoryApi.endpoint.entire}/:id`,
    (req, res, ctx) => {
      const { id } = req.params;
      const categoryId = parseInt(id as string);
      const categoryIndex = categoryDB.categories.findIndex((el) => el.id === categoryId);
      const myCategoryIndex = myCategoryDB.categories.findIndex((el) => el.id === categoryId);
      const subscriptionIndex = subscriptionDB.subscriptions.findIndex(
        (el) => el.category.id === categoryId
      );

      if (categoryIndex < 0 || myCategoryIndex < 0 || subscriptionIndex < 0) {
        return res(ctx.status(400));
      }

      categoryDB.categories.splice(categoryIndex, 1);
      myCategoryDB.categories.splice(myCategoryIndex, 1);
      subscriptionDB.subscriptions.splice(subscriptionIndex, 1);

      return res(ctx.status(204));
    }
  ),

  rest.get(`${API_URL}${categoryApi.endpoint.entire}/:id`, (req, res, ctx) => {
    const { id } = req.params;
    const categoryId = parseInt(id as string);
    const category = categoryDB.categories.find((el) => el.id === categoryId);

    if (!category) {
      return res(ctx.status(404));
    }

    return res(ctx.status(201), ctx.json(category));
  }),

  rest.get(API_URL + categoryApi.endpoint.my, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(myCategoryDB));
  }),

  rest.get(API_URL + profileApi.endpoint.get, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(tigerProfileDB));
  }),

  rest.patch(API_URL + profileApi.endpoint.patch, (req, res, ctx) => {
    if (!req.body) {
      return res(ctx.status(404));
    }

    tigerProfileDB.displayName = (req.body as Pick<ProfileType, 'displayName'>).displayName;

    return res(ctx.status(204));
  }),

  rest.get(API_URL + scheduleApi.endpoint.get, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(getScheduleDB));
  }),

  rest.post<Omit<ScheduleType, 'id' | 'categoryId' | 'colorCode'>>(
    `${API_URL}/api/categories/:id/schedules`,
    (req, res, ctx) => {
      const { id } = req.params;
      const categoryId = parseInt(id as string);
      const newSchedule = { id: `${scheduleDB.length + 1}`, categoryId, ...req.body };

      scheduleDB.push(newSchedule);

      return res(ctx.status(201));
    }
  ),

  rest.patch<Omit<ScheduleType, 'id' | 'categoryId' | 'colorCode'>>(
    `${API_URL}/api/schedules/:id`,
    (req, res, ctx) => {
      const { id: scheduleId } = req.params;

      const scheduleIndex = scheduleDB.findIndex((el) => el.id === scheduleId);

      if (scheduleIndex < 0) {
        return res(ctx.status(404));
      }

      const scheduleGetIndex: { [key: string]: number } = {
        longTerms: getScheduleDB.longTerms.findIndex((el) => el.id === scheduleId),
        allDays: getScheduleDB.allDays.findIndex((el) => el.id === scheduleId),
        fewHours: getScheduleDB.fewHours.findIndex((el) => el.id === scheduleId),
      };
      const scheduleType = Object.keys(scheduleGetIndex).find(
        (key) => scheduleGetIndex[key] !== -1
      );

      if (scheduleType === undefined) {
        return res(ctx.status(404));
      }

      scheduleDB[scheduleIndex] = { ...scheduleDB[scheduleIndex], ...req.body };
      getScheduleDB[scheduleType][scheduleGetIndex[scheduleType]] = {
        ...getScheduleDB[scheduleType][scheduleGetIndex[scheduleType]],
        ...req.body,
      };

      return res(ctx.status(204));
    }
  ),

  rest.delete(`${API_URL}/api/schedules/:id`, (req, res, ctx) => {
    const { id: scheduleId } = req.params;

    const scheduleIndex = scheduleDB.findIndex((el) => el.id === scheduleId);

    if (scheduleIndex < 0) {
      return res(ctx.status(404));
    }

    const scheduleGetIndex: { [key: string]: number } = {
      longTerms: getScheduleDB.longTerms.findIndex((el) => el.id === scheduleId),
      allDays: getScheduleDB.allDays.findIndex((el) => el.id === scheduleId),
      fewHours: getScheduleDB.fewHours.findIndex((el) => el.id === scheduleId),
    };
    const scheduleType = Object.keys(scheduleGetIndex).find((key) => scheduleGetIndex[key] !== -1);

    if (scheduleType === undefined) {
      return res(ctx.status(404));
    }

    scheduleDB.splice(scheduleIndex, 1);
    getScheduleDB[scheduleType].splice(scheduleGetIndex[scheduleType], 1);

    return res(ctx.status(204));
  }),

  rest.get(API_URL + subscriptionApi.endpoint.get, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(subscriptionDB));
  }),

  rest.post<Pick<SubscriptionType, 'colorCode'>>(
    `${API_URL}/api/members/me/categories/:id/subscriptions`,
    (req, res, ctx) => {
      const { id } = req.params;
      const categoryId = parseInt(id as string);
      const newSubscription = {
        id: subscriptionDB.subscriptions.length + 1,
        category: {
          id: categoryDB.categories[categoryId - 1].id,
          name: categoryDB.categories[categoryId - 1].name,
          creator: tigerProfileDB,
          createdAt: categoryDB.categories[categoryId - 1].createdAt,
        },
        colorCode: req.body.colorCode,
        checked: true,
      };

      subscriptionDB.subscriptions.push(newSubscription);

      return res(ctx.status(201), ctx.json(newSubscription));
    }
  ),

  rest.delete(`${API_URL}/api/members/me/subscriptions/:id`, (req, res, ctx) => {
    const { id } = req.params;
    const subscriptionId = parseInt(id as string);
    const subscriptionIndex = subscriptionDB.subscriptions.findIndex(
      (el) => el.id === subscriptionId
    );

    if (subscriptionIndex > -1) {
      subscriptionDB.subscriptions.splice(subscriptionIndex, 1);
    }

    return res(ctx.status(204));
  }),
];

export { handlers };
