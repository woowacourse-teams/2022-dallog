import { rest } from 'msw';

import { CategoryType } from '@/@types/category';
import { ScheduleType } from '@/@types/schedule';
import { SubscriptionType } from '@/@types/subscription';

import { API, API_URL } from '@/constants';

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
    categoryDB.categories.push({
      ...req.body,
      id: categoryDB.categories.length + 1,
      createdAt: new Date().toISOString().slice(0, -5),
      creator: tigerProfileDB,
    });
    myCategoryDB.categories.push({
      ...req.body,
      id: categoryDB.categories.length + 1,
      createdAt: new Date().toISOString().slice(0, -5),
      creator: tigerProfileDB,
    });

    return res(ctx.status(201));
  }),

  rest.patch<Pick<CategoryType, 'name'>>(
    `${API_URL}${categoryApi.endpoint.entire}/:id`,
    (req, res, ctx) => {
      const { id } = req.params;
      const categoryId = parseInt(id as string);
      const categoryIndex = categoryDB.categories.findIndex((el) => el.id === categoryId);
      const myCategoryIndex = myCategoryDB.categories.findIndex((el) => el.id === categoryId);

      if (categoryIndex < 0 || myCategoryIndex < 0) {
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

      if (categoryIndex < 0 || myCategoryIndex < 0) {
        return res(ctx.status(400));
      }

      categoryDB.categories.splice(categoryIndex, 1);
      myCategoryDB.categories.splice(myCategoryIndex, 1);

      return res(ctx.status(204));
    }
  ),

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

    tigerProfileDB.displayName = (req.body as { displayName: string }).displayName;

    return res(ctx.status(204));
  }),

  rest.get(API_URL + scheduleApi.endpoint, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(getScheduleDB));
  }),

  rest.post<Omit<ScheduleType, 'id'>>(API_URL + scheduleApi.endpoint, (req, res, ctx) => {
    scheduleDB.push({ id: scheduleDB.length + 1, ...req.body });

    return res(ctx.status(201));
  }),

  rest.get(API_URL + subscriptionApi.endpoint.get, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(subscriptionDB));
  }),

  rest.post<Pick<SubscriptionType, 'color'>>(
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
        color: req.body.color,
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
