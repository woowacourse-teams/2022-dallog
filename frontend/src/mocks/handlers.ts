import { rest } from 'msw';

import { Schedule } from '@/@types';
import { CategoryType } from '@/@types/category';
import { SubscriptionType } from '@/@types/subscription';

import { API_URL } from '@/constants';

import categoryApi from '@/api/category';
import profileApi from '@/api/profile';
import scheduleApi from '@/api/schedule';
import subscriptionApi from '@/api/subscription';

import { categoryDB, profileDB, scheduleDB, subscriptionDB } from './data';

const handlers = [
  rest.get(categoryApi.endpoint, (req, res, ctx) => {
    const page = parseInt(req.url.searchParams.get('page') as string);
    const size = parseInt(req.url.searchParams.get('size') as string);
    const slicedCategories = categoryDB.categories.slice(page * size, page * size + size);

    return res(
      ctx.status(200),
      ctx.json({
        page,
        categories: slicedCategories,
      })
    );
  }),

  rest.post<Pick<CategoryType, 'name'>>(categoryApi.endpoint, (req, res, ctx) => {
    categoryDB.categories.push({
      ...req.body,
      id: categoryDB.categories.length + 1,
      createdAt: new Date().toISOString().slice(0, -5),
      creator: profileDB,
    });

    return res(ctx.status(201));
  }),

  rest.get(profileApi.endpoint, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(profileDB));
  }),

  rest.get(scheduleApi.endpoint, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json({ schedules: scheduleDB }));
  }),

  rest.post<Omit<Schedule, 'id'>>(scheduleApi.endpoint, (req, res, ctx) => {
    scheduleDB.push({ id: scheduleDB.length + 1, ...req.body });

    return res(ctx.status(201));
  }),

  rest.get(subscriptionApi.endpoint.get, (req, res, ctx) => {
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
          creator: profileDB,
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
