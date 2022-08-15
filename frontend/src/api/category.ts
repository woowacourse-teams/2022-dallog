import { CategoriesGetResponseType, CategoryType } from '@/@types/category';

import dallogApi from './';

const categoryApi = {
  endpoint: {
    entire: '/api/categories',
    my: '/api/categories/me',
  },
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
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

  getSingle: async (categoryId: number | undefined) => {
    const response = await dallogApi.get(`${categoryApi.endpoint.entire}/${categoryId}`, {
      headers: { ...categoryApi.headers },
    });

    return response;
  },

  getMy: async (accessToken: string) => {
    const response = await dallogApi.get<CategoryType[]>(categoryApi.endpoint.my, {
      headers: { ...categoryApi.headers, Authorization: `Bearer ${accessToken}` },
      transformResponse: (res) => JSON.parse(res).categories,
    });

    return response;
  },

  post: async (accessToken: string, body: Pick<CategoryType, 'name'>) => {
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

  delete: async (accessToken: string, categoryId: number) => {
    const response = await dallogApi.delete(`${categoryApi.endpoint.entire}/${categoryId}`, {
      headers: { ...categoryApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },
};

export default categoryApi;
