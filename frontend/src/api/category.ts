import {
  CategoriesGetResponseType,
  CategoryRoleType,
  CategorySubscriberType,
  CategoryType,
} from '@/@types/category';

import dallogApi from './';

const categoryApi = {
  endpoint: {
    admin: '/api/categories/me/admin',
    editable: '/api/categories/me/schedule-editable',
    entire: '/api/categories',
    my: '/api/categories/me',
    schedules: (categoryId: number) => `/api/categories/${categoryId}/schedules`,
    subscribers: (categoryId: number) => `/api/categories/${categoryId}/subscribers`,
    role: (categoryId: number, memberId: number) =>
      `/api/categories/${categoryId}/subscribers/${memberId}/role`,
  },
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  getAdmin: async (accessToken: string) => {
    const response = await dallogApi.get(categoryApi.endpoint.admin, {
      headers: { ...categoryApi.headers, Authorization: `Bearer ${accessToken}` },
      transformResponse: (res) => JSON.parse(res).categories,
    });

    return response;
  },

  getEditable: async (accessToken: string) => {
    const response = await dallogApi.get(categoryApi.endpoint.editable, {
      headers: { ...categoryApi.headers, Authorization: `Bearer ${accessToken}` },
      transformResponse: (res) => JSON.parse(res).categories,
    });

    return response;
  },

  getEntire: async (name: string, page: number, size: number) => {
    const response = await dallogApi.get<CategoriesGetResponseType>(
      categoryApi.endpoint.entire,
      name === ''
        ? {
            params: { page, size },
            headers: categoryApi.headers,
          }
        : {
            params: { name, page, size },
            headers: categoryApi.headers,
          }
    );

    return response;
  },

  getSingle: async (categoryId?: number) => {
    const response = await dallogApi.get(`${categoryApi.endpoint.entire}/${categoryId}`, {
      headers: { ...categoryApi.headers },
    });

    return response;
  },

  getSubscribers: async (accessToken: string, categoryId: number) => {
    const response = await dallogApi.get<CategorySubscriberType[]>(
      categoryApi.endpoint.subscribers(categoryId),
      {
        headers: { ...categoryApi.headers, Authorization: `Bearer ${accessToken}` },
        transformResponse: (res) => JSON.parse(res).subscribers,
      }
    );

    return response;
  },

  getMy: async (accessToken: string) => {
    const response = await dallogApi.get<CategoryType[]>(categoryApi.endpoint.my, {
      headers: { ...categoryApi.headers, Authorization: `Bearer ${accessToken}` },
      transformResponse: (res) => JSON.parse(res).categories,
    });

    return response;
  },

  getSchedules: async (categoryId: number, startDateTime: string, endDateTime: string) => {
    const response = await dallogApi.get(
      `${categoryApi.endpoint.schedules(
        categoryId
      )}?startDateTime=${startDateTime}&endDateTime=${endDateTime}`,
      {
        headers: { ...categoryApi.headers },
      }
    );

    return response;
  },

  post: async (accessToken: string, body: Pick<CategoryType, 'name' | 'categoryType'>) => {
    const response = await dallogApi.post(categoryApi.endpoint.entire, body, {
      headers: { ...categoryApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },

  patch: async (accessToken: string, categoryId: number, body: Pick<CategoryType, 'name'>) => {
    const response = await dallogApi.patch(`${categoryApi.endpoint.entire}/${categoryId}`, body, {
      headers: { ...categoryApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },

  patchRole: async (
    accessToken: string,
    categoryId: number,
    memberId: number,
    body: { categoryRoleType: CategoryRoleType }
  ) => {
    const response = await dallogApi.patch(categoryApi.endpoint.role(categoryId, memberId), body, {
      headers: { ...categoryApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },

  delete: async (accessToken: string, categoryId: number) => {
    const response = await dallogApi.delete(`${categoryApi.endpoint.entire}/${categoryId}`, {
      headers: { ...categoryApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },
};

export default categoryApi;
